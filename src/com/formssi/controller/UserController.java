package com.formssi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.formssi.entity.ReturnJson;
import com.formssi.entity.User;
import com.formssi.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.jmx.snmp.Timestamp;

import utils.MD5Util;
import utils.MySessionContext;
import utils.Token;
import utils.Utils;

@Controller
//@RequestMapping("/login")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String login(@RequestParam("data") String date, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		User userInput = User.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		try {
			//通过userService去数据库查该id的用户对象。
			User user = userService.getById(userInput.getUserId());
			//查到映射成的对象如果为空，表示该用户不存在
			if (null == user || Utils.stringIsNull(user.getUserId())) {
				returnJson.setSuccess(false);
				returnJson.setMessage("用户不存在！");
				return returnJson.toJSON();
			}else if (!Utils.stringIsNull(userInput.getPassword()) && MD5Util.MD5Encode(userInput.getPassword(), "UTF-8").equals(user.getPassword())) {
				//用户密码不为空，且密码一致。登录成功
				returnJson.setSuccess(true);
				returnJson.setMessage("登录成功！");
				//登录成功返回token
				String token = Token.getToken();
				String sessionId = session.getId();
				
				//带时间的token存入session，便于拦截器失效校验--正规做法是token存入redis，设置失效时间，拦截器查询token与页面传过来的做比较校验。
				session.setAttribute(sessionId, token + "," + new Timestamp().getDateTime());
				System.out.println("login sessionid: "+sessionId);
				System.out.println("login token: "+session.getAttribute(sessionId));
				
				returnJson.setObj("{\"token\":\"" + token + "\",\"sessionId\":\""+session.getId()+"\",\"rank\":"+user.getRank()+",\"department\":"+user.getDepartment()+"}");
			}else {//其他情况一律登录失败
				returnJson.setSuccess(false);
				returnJson.setMessage("用户名或密码错误！");
				return returnJson.toJSON();
			}
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("登录失败！");
			return returnJson.toJSON();
		}
		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/logout", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String logout(@RequestParam("data") String date, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		ReturnJson returnJson = new ReturnJson();
		
		String sessionId = request.getParameter("sessionId");
        if(sessionId == null) {
        	String data = request.getParameter("data");
            sessionId = (String)JSON.parseObject(data).get("sessionId");
        }

        HttpSession session = MySessionContext.getSession(sessionId);
        
        if(session == null || session.getAttribute(sessionId) == null) {
        	returnJson.setSuccess(false);
            returnJson.setMessage("已登出");
            
            return returnJson.toJSON();
        }
		
        MySessionContext.DelSession(session);
		
		returnJson.setSuccess(true);
		returnJson.setMessage("登出成功！");

		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/addUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addUser(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		User user = User.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == user || Utils.stringIsNull(user.getUserId()) || Utils.stringIsNull(user.getPassword()) ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("用户数据不能为空！");
			return returnJson.toJSON();
		}
		
		try {
			//密码不存明文，存MD5加密后的值
			user.setPassword(MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
			
			userService.add(user);
			returnJson.setSuccess(true);
			returnJson.setMessage("添加用户成功！");
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("添加用户失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/checkUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkUser(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		User user = User.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == user || Utils.stringIsNull(user.getUserId())) {
			returnJson.setSuccess(false);
			returnJson.setMessage("用户编号不能为空！");
			return returnJson.toJSON();
		}
		
		try {
			User userResult = userService.getById(user.getUserId());
			if(userResult == null) {
				returnJson.setSuccess(true);
				returnJson.setMessage("检查用户成功，无重复用户！");
			}else {
				returnJson.setSuccess(false);
				returnJson.setMessage("检查用户失败，用户已存在！");
			}
			
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("检查用户失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/queryUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String queryUser(@RequestParam("data") String data, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		ReturnJson returnJson = new ReturnJson();
		try {
			User user = User.parse(data);
			PageHelper.startPage(user.getPageNum(), user.getPageSize());
			List<User> userList = userService.queryUser();
			PageInfo page = new PageInfo(userList, user.getPageSize());
			
	        returnJson.setObj(page);
			returnJson.setSuccess(true);
			returnJson.setMessage("查询用户列表成功！");
		} catch (Exception e) {
			// TODO: handle exception
			returnJson.setSuccess(false);
			returnJson.setMessage("查询用户列表失败！");
			return returnJson.toJSON();
		}
		
        
		return returnJson.toJSON();
	}
}
