package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class File {
	
	private String fileId;			//	文件ID
	private String fileAddr;		//	文件地址
	private String pubKeyToSymkey;	//	对称密钥（加密后）
	private String strategyId;		//	策略    x|x|x|  第一个数字是军衔，后面的字段是部门
	private String department; 		//  上传部门
	private String userId; 			//  上传人
	private String uploadTime;		//	上传时间
	
	@JSONField(name = "fileId")
	public String getFileId() {
		return fileId;
	}
	@JSONField(name = "fileId")
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	@JSONField(name = "fileAddr")
	public String getFileAddr() {
		return fileAddr;
	}
	@JSONField(name = "fileAddr")
	public void setFileAddr(String fileAddr) {
		this.fileAddr = fileAddr;
	}
	
	@JSONField(name = "pubKeyToSymkey")
	public String getPubKeyToSymkey() {
		return pubKeyToSymkey;
	}
	@JSONField(name = "pubKeyToSymkey")
	public void setPubKeyToSymkey(String pubKeyToSymkey) {
		this.pubKeyToSymkey = pubKeyToSymkey;
	}
	
	@JSONField(name = "strategyId")
	public String getStrategyId() {
		return strategyId;
	}
	@JSONField(name = "strategyId")
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	
	@JSONField(name = "department")
	public String getDepartment() {
		return department;
	}


	@JSONField(name = "department")
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@JSONField(name = "userId")
	public String getUserId() {
		return userId;
	}
	@JSONField(name = "userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JSONField(name = "updateTime")
	public String getUploadTime() {
		return uploadTime;
	}
	@JSONField(name = "updateTime")
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public static File parse(String json) {
		File object=JSON.parseObject(json, File.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	
	@Override
	public String toString() {
		return "File [fileId=" + fileId + ", fileAddr=" + fileAddr + ", pubKeyToSymkey=" + pubKeyToSymkey + ", strategyId="
				+ strategyId + ", department=" + department + ", userId=" + userId + ", uploadTime=" + uploadTime + "]";
	}

}
