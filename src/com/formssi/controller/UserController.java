package com.formssi.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.formssi.entity.User;
import com.formssi.service.impl.UserServiceImpl;

import utils.Utils;

/**
 * Created by Jerry on 2018/4/8.
 */
@Controller
//@RequestMapping("/login")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
//	@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public String login(@RequestParam("data") String date,HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问
//		User userInput=User.parse(date);
//		//通过userService去数据库查改id的用户对象。此处密码可以优化，数据库中不应该存密码明文
//		User user=userService.getById(userInput.getUsername());//登录的 username ==>account表里面的id
//		//查到映射成的对象如果为空，表示该用户不存在
//		if ("".equals(user) || null==user) {
//			user=new User();
//			user.setSuccess("false");
//			user.setMessage("用户不存在！");
//			return user.toJSON();
//		}else if (!Utils.stringIsNull(userInput.getPassword()) && userInput.getPassword().equals(user.getPassword())) {
//			//用户密码不为空，且密码一致。登录成功
//			user.setSuccess("true");
//		}else {//其他情况一律登录失败
//			user.setSuccess("false");
//			user.setMessage("用户名或密码错误！");
//			return user.toJSON();
//		}
//		return user.toJSON();
//	}
	
}
