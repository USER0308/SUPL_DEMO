package com.formssi.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.formssi.entity.ReturnJson;
import com.formssi.entity.User;
import com.formssi.service.impl.UserServiceImpl;

import utils.Base64Utils;
import utils.Utils;

/**
 * Created by Jerry on 2018/4/8.
 */
@Controller
//@RequestMapping("/login")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String login(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		User userInput=User.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		try {
			//通过userService去数据库查该id的用户对象。
			User user=userService.getById(userInput.getUserId());
			//查到映射成的对象如果为空，表示该用户不存在
			if ("".equals(user) || null==user) {
				returnJson.setSuccess(false);
				returnJson.setMessage("用户不存在！");
				return returnJson.toJSON();
			}else if (!Utils.stringIsNull(userInput.getPassword()) && Base64Utils.encode(userInput.getPassword().getBytes()).equals(user.getPassword())) {
				//用户密码不为空，且密码一致。登录成功
				returnJson.setSuccess(true);
				returnJson.setMessage("登录成功！");
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
	
	@RequestMapping(value = "/addUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addUser(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
		
		User user=User.parse(date);
		
		ReturnJson returnJson = new ReturnJson();
		
		if (null == user || "".equals(user.getUserId()) || "".equals(user.getPassword()) ) {
			returnJson.setSuccess(false);
			returnJson.setMessage("用户不存在！");
			return returnJson.toJSON();
		}
		
		try {
			//密码不存明文，存编码后的值
			user.setPassword(Base64Utils.encode(user.getPassword().getBytes()));
			
			userService.add(user);
			returnJson.setSuccess(true);
			returnJson.setMessage("添加用户成功！");
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("添加用户失败！");
			return returnJson.toJSON();
		}
		
		return user.toJSON();
	}
	
}
