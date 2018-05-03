package com.formssi.controller;
import java.io.File;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.formssi.entity.Transaction;
import com.formssi.service.IOUService;

import utils.Utils;


@Controller
public class TranController {
    
//    @RequestMapping(value = "/getTranList", produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public String getTranList (@RequestParam("data") String data,HttpServletResponse response) throws Exception {
//    	response.setHeader("Access-Control-Allow-Origin", "*");
//    	String startPage=JSONObject.parseObject(data).getString("startPage");
//    	String pageSize=JSONObject.parseObject(data).getString("pageSize");
//    	String tranListString=IOUService.queryTransList(Integer.parseInt(startPage), Integer.parseInt(pageSize));
//		JSONArray jsonArray=JSON.parseArray(tranListString);
//    	return jsonArray.toJSONString();
//    }
//
//    //根据id获取tran信息,produces是表明以json文件来进行处理
//    @RequestMapping(value = "/findTranById", produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public String findTranById (@RequestParam("data") String data,HttpServletResponse response) throws Exception {
//    	//跨域访问，必须加
//    	response.setHeader("Access-Control-Allow-Origin", "*");
//    	//解析传递过来的json数据，提取信息
//    	String conID=JSONObject.parseObject(data).getString("conID");
//    	
//    	//使用IOUService来操作链
//		String tranListString=IOUService.queryTransByConId(conID);
//		return tranListString;
//    }
//    
//    //新增交易，获取上传文件和一些其他的必须信息然后保存文件，提取hash值。创建transaction对象并上链。
//    @RequestMapping(value = "/addTransaction")
//    @ResponseBody
//    public String addTransaction(@RequestParam("file") MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception 
//    { 
//    	response.setHeader("Access-Control-Allow-Origin", "*");
//    	
//    	//获取文件名，带后缀
//    	String documentFileName = file.getOriginalFilename();
//    	String extension = documentFileName.substring(documentFileName.lastIndexOf("."));//获取文件后缀
//    	String path=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"\\files\\";//获取要写入的文件路径
//    	System.out.println(path);
//    	
//    	String fileName = path+String.valueOf(System.currentTimeMillis()) + extension;//拼装文件路径和名字
//    	System.out.println(fileName);
//        
//    	file.transferTo(new File(fileName));//从request中获取上传的文件，然后写入到目标文件中
//        
//    	//获取文件的hash值
//        String conHash=Utils.getFileSHA256Str(fileName);
//        
//        //创建transaction对象
//        Transaction transaction=new Transaction();
//        String conID=request.getParameter("cid");
//        transaction.setConID(conID);
//        transaction.setSaleOrg(request.getParameter("compa"));
//        transaction.setBuyOrg(request.getParameter("compb"));
//        transaction.setTransType(request.getParameter("type"));
//        transaction.setAmount(new BigInteger(request.getParameter("limit")));
//        transaction.setConHash(conHash);
//        transaction.setLatestStatus(request.getParameter("latestStatus"));
//        transaction.setTransTime(Utils.getCurrentDate());
//        
//        //吧transaction对象上链
//        IOUService.addTransaction(transaction);
//        
//        //检查是否上链成功，返回相应信息给前端
//        String json=IOUService.queryTransByConId(conID);
//        Transaction transaction2=new Transaction();
//        transaction2=Transaction.parse(json);
//        boolean result=false;
//        if ((conID!=null || "".equals(conID))&&(conID.equals(transaction2.getConID()))) {
//        	result=true;
//        }
//        return "{success:"+result+"}"; 
//    }
//    
//    
//    @RequestMapping(value = "/updateTransStatus", produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public String updateTransStatus (@RequestParam("data") String data,HttpServletResponse response) throws Exception {
//    	response.setHeader("Access-Control-Allow-Origin", "*");
//    	Transaction transaction=new Transaction();
//    	transaction.setConID(JSONObject.parseObject(data).getString("conID"));
//    	transaction.setLatestStatus(JSONObject.parseObject(data).getString("latestStatus"));
//    	IOUService.updateTransStatus(transaction);
//		return "";
//    }
    

}