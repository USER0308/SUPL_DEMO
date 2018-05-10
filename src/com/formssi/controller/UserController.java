package com.formssi.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.formssi.entity.ReturnJson;
import com.formssi.entity.User;
import com.formssi.service.UserService;
import com.sun.jmx.snmp.Timestamp;

import utils.MD5Util;
import utils.Token;
import utils.Utils;

@Controller
//@RequestMapping("/login")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String login(@RequestParam("data") String date, HttpServletResponse response, HttpSession session) {
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
				returnJson.setObj(token);
				
				//带时间的token存入session，便于拦截器失效校验--正规做法是token存入redis，设置失效时间，拦截器查询token与页面传过来的做比较校验。
				session.setAttribute(user.getUserId(), token + "," + new Timestamp().getDateTime());
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
	public String logout(@RequestParam("data") String date, HttpServletResponse response, HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		User user = User.parse(date);
		session.removeAttribute(user.getUserId());
		
		ReturnJson returnJson = new ReturnJson();
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
	
}
