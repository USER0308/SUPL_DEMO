package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class FileRes {
	
	private String responseId;		//	响应Id				上链	RES+[请求id去掉前3位]
	private String requestId;		//	请求Id				上链
	private String fileId;			//	文件Id				上链
	private String pubKeyToSymkey;	//	请求的公钥对对称密钥加密	上链
	private String fileAddr;		//	文件地址				上链
	
	@JSONField(name = "responseId")
	public String getResponseId() {
		return responseId;
	}
	@JSONField(name = "responseId")
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	
	@JSONField(name = "requestId")	
	public String getRequestId() {
		return requestId;
	}
	@JSONField(name = "requestId")
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	@JSONField(name = "fileId")
	public String getFileId() {
		return fileId;
	}
	@JSONField(name = "fileId")
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	@JSONField(name = "pubKeyToSymkey")
	public String getPubKeyToSymkey() {
		return pubKeyToSymkey;
	}
	@JSONField(name = "pubKeyToSymkey")
	public void setPubKeyToSymkey(String pubKeyToSymkey) {
		this.pubKeyToSymkey = pubKeyToSymkey;
	}
	
	@JSONField(name = "fileAddr")
	public String getFileAddr() {
		return fileAddr;
	}
	@JSONField(name = "fileAddr")
	public void setFileAddr(String fileAddr) {
		this.fileAddr = fileAddr;
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
		return "FileRes [responseId=" + responseId + ", requestId=" + requestId + ", fileId=" + fileId
				+ ", pubKeyToSymkey=" + pubKeyToSymkey + ", fileAddr=" + fileAddr + "]";
	}
}
