package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Jerry on 2018/4/9.
 */


public class User {
	private String username="";
	private String password="";
	private String orgname="";
	private String usertype="";
	private String success="false";
	private String message="";
	
	
	
	@JSONField(name = "message")
	public String getMessage() {
		return message;
	}
	@JSONField(name = "message")
	public void setMessage(String message) {
		this.message = message;
	}
	@JSONField(name = "success")
	public String getSuccess() {
		return success;
	}
	@JSONField(name = "success")
	public void setSuccess(String success) {
		this.success = success;
	}
	
	@JSONField(name = "orgname")
	public String getOrgname() {
		return orgname;
	}
	@JSONField(name = "orgname")
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	@JSONField(name = "username")
	public String getUsername() {
		return username;
	}
	@JSONField(name = "username")
	public void setUsername(String username) {
		this.username = username;
	}
	@JSONField(name = "password")
	public String getPassword() {
		return password;
	}
	@JSONField(name = "password")
	public void setPassword(String password) {
		this.password = password;
	}
	@JSONField(name = "usertype")
	public String getUsertype() {
		return usertype;
	}
	@JSONField(name = "usertype")
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public static User parse(String json) {
		User object=JSON.parseObject(json, User.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", orgname=" + orgname + ", usertype="
				+ usertype + ", success=" + success + "]";
	}
	
}
