package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
/**
 * 
 * @author Administrator
 * 
 */

public class ReturnJson {
	
	private boolean success;	//	是否成功
	private String message;		//	成功失败信息
	
	@JSONField(name = "success")
	public boolean isSuccess() {
		return success;
	}

	@JSONField(name = "success")
	public void setSuccess(boolean success) {
		this.success = success;
	}

	@JSONField(name = "message")
	public String getMessage() {
		return message;
	}

	@JSONField(name = "message")
	public void setMessage(String message) {
		this.message = message;
	}

	public static ReturnJson parse(String json) {
		ReturnJson object=JSON.parseObject(json, ReturnJson.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	
	@Override
	public String toString() {
		return "User [success=" + success + ", message=" + message + "]";
	}
	
}
