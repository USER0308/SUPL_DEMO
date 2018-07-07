package com.formssi.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.formssi.entity.IouLimitEntity;
import com.formssi.entity.IouRecord;
import com.formssi.service.IIouLimitEntityService;
import com.formssi.service.IIouRecordService;
import com.formssi.service.ITransactionService;
import com.formssi.service.impl.IOUService;
//import com.formssi.entity.ReturnJson;
//import com.formssi.entity.User;
//import com.formssi.service.UserService;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
import com.sun.jmx.snmp.Timestamp;

import utils.Utils;

//import utils.MD5Util;
//import utils.MySessionContext;
//import utils.Token;
//import utils.Utils;

@Controller
//@RequestMapping("/login")
public class UserController {
	
	@Autowired
	private IIouLimitEntityService iouLimitEntityService;
	@Autowired
	private IIouRecordService iouRecordService;
	@Autowired
	private ITransactionService transactionService;
	

	@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String orgID=request.getParameter("orgID");
		String orgName=request.getParameter("orgName");
		String password=request.getParameter("password");
		
		//登录操作
		
		HttpSession session=request.getSession();
		session.setAttribute("orgID", orgID);
		
		JSONObject res=new JSONObject();
		res.put("status", "1");
		return res.toJSONString();
	}

	@RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String orgID=request.getParameter("orgID");
		String orgName=request.getParameter("orgName");
		String password=request.getParameter("password");
		int iouLimit = Integer.parseInt(request.getParameter("iouLimit"));
		
		//注册操作
//		IOUService.initObj();
//		IOUService.initIouLimitData(orgID, orgName, password, iouLimit);
		
		boolean isSuccess = iouLimitEntityService.addIouLimitEntity(orgID,orgName,password,iouLimit);
		
		
		HttpSession session=request.getSession();
		session.setAttribute("orgID", orgID);
		
		JSONObject res=new JSONObject();
		if(isSuccess)
			res.put("status", "1");
		else 
			res.put("status", "0");
		return res.toJSONString();
	}
	
	
	@RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		System.out.println(request.getParameter("username")+"   @@@@@");
		return "666";
	}
	
	@RequestMapping(value = "/logout", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		HttpSession session=request.getSession();
		session.invalidate();
		
		JSONObject res=new JSONObject();
		res.put("status", "1");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/ioulist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String ioulist(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		int pageNum=Integer.parseInt(request.getParameter("pageNum"));
		int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		//String pageSize=request.getParameter("pageSize");
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		
		//获取iou列表操作
		List<IouRecord> tem = iouRecordService.getIouRecordList(pageNum, pageSize);
		
		JSONArray res=new JSONArray();
		for (int i=0;i<tem.size();i++) {
			JSONObject xxx = new JSONObject();
			xxx.put("iouId", tem.get(i).getIouId());
			xxx.put("fromOrg", tem.get(i).getFromOrg());
			xxx.put("recOrg", tem.get(i).getRecvOrg());
			xxx.put("transTime", tem.get(i).getTransTime());
			xxx.put("updateTime", tem.get(i).getUpdateTime());
			xxx.put("amount", tem.get(i).getAmount());
			xxx.put("paidAmt", tem.get(i).getPaidAmt());
			xxx.put("iouStatus", tem.get(i).getIouStatus());
			
			res.add(xxx);
		}
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/get_ioulist_num", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String get_ioulist_num(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		
		//获取ioulist数目
		
		JSONObject res=new JSONObject();
		res.put("amount", "99");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/recycle_iou", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String recycle_iou(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String iouId=request.getParameter("iouId");
		//String amount=request.getParameter("amount");
		int amount=Integer.parseInt(request.getParameter("amount"));
		
		//HttpSession session=request.getSession();
		//String orgID = (String) session.getAttribute("orgID");
		try {
			iouLimitEntityService.recycleIou(iouId, amount);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject res=new JSONObject();
		res.put("status", "1");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/update_iou_limit", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String update_iou_limit(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		int amount=Integer.parseInt(request.getParameter("amount"));
		
		HttpSession session=request.getSession();
		String orgID = (String) session.getAttribute("orgID");
		
		//更新白条额度操作
		String transTime = Utils.sdf(System.currentTimeMillis());
		boolean isSuccess=false;
		try {
			isSuccess = iouLimitEntityService.setIouLimit(amount, transTime, orgID);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//(orgID,orgName,password,iouLimit);
		
		
		
		JSONObject res=new JSONObject();
		if(isSuccess)
			res.put("status", "1");
		else 
			res.put("status", "0");
		return res.toJSONString();
		
	}
	
	@RequestMapping(value = "/transactionlist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String transactionlist(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String pageNum=request.getParameter("pageNum");
		String pageSize=request.getParameter("pageSize");
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		
		//交易列表操作
		
		JSONArray res=new JSONArray();
		for (int i=0;i<2;i++) {
			//JSONObject xxx = new JSONObject;
			// 各种put
			//res.add(xxx);
		}
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/get_transaction_num", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String get_transaction_num(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		HttpSession session=request.getSession();
		String orgID=(String) session.getAttribute("orgID");
		
		//获取get_transaction_num数目
		
		JSONObject res=new JSONObject();
		res.put("amount", "99");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/add_transaction", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String add_transaction(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		String saleOrg=request.getParameter("saleOrg");
		String buyOrg=request.getParameter("buyOrg");
		String transType=request.getParameter("transType");
		String amount=request.getParameter("amount");
		String conFile=request.getParameter("conFile");
		
		HttpSession session=request.getSession();
		String orgID = (String) session.getAttribute("orgID");
		
		//添加交易操作
		
		JSONObject res=new JSONObject();
		res.put("status", "1");
		return res.toJSONString();
	}
	
	@RequestMapping(value = "/get_transaction/{con_id}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String get_transaction(@PathVariable("con_id") String con_id, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		System.out.println("Product Id  ff : " + con_id); 
		
		//根据合同获得交易信息
		
		
		JSONObject res=new JSONObject();
		res.put("zzz", "xxxx");
		//te.fluentAdd(zz);
		return res.toJSONString();
	}
	
//	@RequestMapping(value="/product/{productId}", produces = "application/json;charset=UTF-8") 
//	public String getProduct(@PathVariable("productId") String productId, HttpServletResponse response, HttpSession session){ 
//	    System.out.println("Product Id : " + productId); 
//	    return "hello"; 
//	} 
	

	
	@RequestMapping(value = "/product/{productId}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String test(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		System.out.println("Product Id  ff : " + productId); 
		JSONObject zz=new JSONObject();
		zz.put("zzz", "xxxx");
		System.out.println(zz.toJSONString()); 
		JSONArray te=new JSONArray();
		te.add(zz);
		for(int i=0;i<2;i++) {
		JSONObject zzz =new JSONObject();
		zzz.put("ddd", "ddsss");
		te.add(zzz);
		}
		//te.fluentAdd(zz);
		return te.toJSONString();
	}

}
