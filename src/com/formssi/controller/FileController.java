package com.formssi.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.formssi.entity.ReturnJson;
import com.formssi.entity.ShareFile;
import com.formssi.service.impl.FileServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
//@RequestMapping("/login")
public class FileController {
	
	@Autowired
	private FileServiceImpl fileService;
 
	@RequestMapping(value = "/uploadFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String uploadFile(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		ShareFile shareFile = ShareFile.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == shareFile || "".equals(shareFile.getFileId()) ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("文件不存在！");
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
		
		if (null == shareFile || "".equals(shareFile.getFileId()) ) {
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
	
	@RequestMapping(value = "/queryAllFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String queryAllFile(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		ShareFile shareFile = ShareFile.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == shareFile ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("查询条件不存在！");
			return returnJson.toJSON();
		}
		
		try {
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
