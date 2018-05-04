package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class FileReq {
	
	private String requestId;	//	请求Id	上链	REQ+userId+当前时间毫秒数[new Date().getTime()]
	private String userId;		//	用户Id	上链
	private String fileId;		//	文件Id	上链
	private String requestTime;	//	请求时间	上链	yyyyMMdd hh:mm:ss
	
	@JSONField(name = "requestId")
	public String getRequestId() {
		return requestId;
	}
	@JSONField(name = "requestId")
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	@JSONField(name = "userId")
	public String getUserId() {
		return userId;
	}
	@JSONField(name = "userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JSONField(name = "fileId")
	public String getFileId() {
		return fileId;
	}
	@JSONField(name = "fileId")
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	@JSONField(name = "requestTime")
	public String getRequestTime() {
		return requestTime;
	}
	@JSONField(name = "requestTime")
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public static FileReq parse(String json) {
		FileReq object=JSON.parseObject(json, FileReq.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	
	@Override
	public String toString() {
		return "FileReq [requestId=" + requestId + ", userId=" + userId + ", fileId=" + fileId + ", requestTime="
				+ requestTime + "]";
	}

}
