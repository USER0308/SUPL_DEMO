package com.formssi.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.formssi.entity.ReturnJson;
import com.formssi.entity.ShareFile;
import com.formssi.entity.User;
import com.formssi.service.FileService;
import com.formssi.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import utils.Utils;

@Controller
//@RequestMapping("/login")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private UserService userService;
 
	@RequestMapping(value = "/uploadFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		String documentFileName = file.getOriginalFilename();
    	String extension = documentFileName.substring(documentFileName.lastIndexOf("."));//获取文件后缀
    	String path=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"\\files\\";//获取要写入的文件路径
    	System.out.println(path);
    	
    	String fileName = path+String.valueOf(System.currentTimeMillis()) + extension;//拼装文件路径和名字
    	System.out.println(fileName);
        
    	ReturnJson returnJson = new ReturnJson();
    	
    	try {
    		//从request中获取上传的文件，然后写入到目标文件中
			file.transferTo(new File(fileName));
		}  catch (Exception e1) {
			returnJson.setSuccess(false);
			returnJson.setMessage("文件上传失败！");
			return returnJson.toJSON();
		}
    	
    	ShareFile shareFile = new ShareFile();
    	String fileId = request.getParameter("fileId");
    	shareFile.setFileId(fileId);
    	shareFile.setFileAddr(request.getParameter("fileAddr"));
    	shareFile.setPubKeyToSymkey(request.getParameter("pubKeyToSymkey"));
    	shareFile.setAllowDep(request.getParameter("allowDep"));
    	shareFile.setAllowRank(Integer.parseInt(request.getParameter("allowRank")));
    	shareFile.setDepartment(Integer.parseInt(request.getParameter("department")));
    	shareFile.setDescription(request.getParameter("description"));
    	shareFile.setUserId(request.getParameter("userId"));
		
		if (null == shareFile || Utils.stringIsNull(fileId) ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("文件号不能为空！");
			return returnJson.toJSON();
		}
		
		try {
			fileService.add(shareFile);
			returnJson.setSuccess(true);
			returnJson.setMessage("上传文件成功！");
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("上传文件失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/dowloadFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String dowloadFile(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		ShareFile shareFile = ShareFile.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == shareFile || Utils.stringIsNull(shareFile.getFileId()) ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("文件不存在！");
			return returnJson.toJSON();
		}
		
		try {
			fileService.dowloadFile(shareFile);
			returnJson.setSuccess(true);
			returnJson.setMessage("文件下载成功！");
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("文件下载失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/checkFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkFile(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		ShareFile shareFile = ShareFile.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == shareFile || Utils.stringIsNull(shareFile.getFileId()) ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("文件号不能为空！");
			return returnJson.toJSON();
		}
		
		try {
			ShareFile shareFileResult = fileService.getById(shareFile.getFileId());
			if(shareFileResult == null) {
				returnJson.setSuccess(true);
				returnJson.setMessage("文件检查成功，无重复文件！");
			}else {
				returnJson.setSuccess(false);
				returnJson.setMessage("文件检查失败，文件已存在！");
			}
			
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("文件检查失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/queryAllFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String queryAllFile(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		ShareFile shareFile = ShareFile.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == shareFile || Utils.stringIsNull(shareFile.getUserId()) ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("查询条件不存在！");
			return returnJson.toJSON();
		}
		
		try {
			//当前登录人只允许查询出允许当前登录人查询的信息（当前登录人的军衔和部门在文件字段里允许的军衔和允许的部门里的文件查询出来）
			User user = userService.getById(shareFile.getUserId());
			shareFile.setAllowRank(user.getRank());
			shareFile.setAllowDep(String.valueOf(user.getDepartment()));
			
			//从第一条开始 每页查询五条数据
	        PageHelper.startPage(shareFile.getPageNum(), shareFile.getPageSize());
	        List<ShareFile> list = fileService.queryAll(shareFile);
	        //将信息放入PageInfo对象里
	        PageInfo page = new PageInfo(list, shareFile.getPageSize());
	        
	        returnJson.setObj(page);
			returnJson.setSuccess(true);
			returnJson.setMessage("查询文件列表成功！");
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("查询文件列表失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
}
