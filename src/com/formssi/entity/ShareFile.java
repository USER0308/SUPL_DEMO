package com.formssi.entity;

import java.sql.Timestamp;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 策略上链部分需要处理。  x,x,x,x
 * 第一位是allowRank，后面的就是allowDep遍历出来的结果
 * @author Administrator
 */
public class ShareFile {
	
	private String fileId;			//	文件ID			上链&存数据库
	private String fileAddr;		//	文件地址 			上链
	private String pubKeyToSymkey;	//	对称密钥（加密后）	上链
	private int allowRank;			//	允许访问的军衔		处理上链&存数据库
	private List<Integer> allowDep;	//	允许访问的部门		处理上链&存数据库
	private int department; 		//  上传部门			上链&存数据库
	private String userId; 			//  上传人			上链&存数据库
	private Timestamp uploadTime;	//	上传时间			上链&存数据库
	
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
	
	@JSONField(name = "allowRank")
	public int getAllowRank() {
		return allowRank;
	}
	@JSONField(name = "allowRank")
	public void setAllowRank(int allowRank) {
		this.allowRank = allowRank;
	}
	
	@JSONField(name = "allowDep")
	public List<Integer> getAllowDep() {
		return allowDep;
	}
	@JSONField(name = "allowDep")
	public void setAllowDep(List<Integer> allowDep) {
		this.allowDep = allowDep;
	}
	
	@JSONField(name = "department")
	public int getDepartment() {
		return department;
	}

	@JSONField(name = "department")
	public void setDepartment(int department) {
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
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	@JSONField(name = "updateTime")
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public static ShareFile parse(String json) {
		ShareFile object=JSON.parseObject(json, ShareFile.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	@Override
	public String toString() {
		return "ShareFile [fileId=" + fileId + ", fileAddr=" + fileAddr + ", pubKeyToSymkey=" + pubKeyToSymkey
				+ ", allowRank=" + allowRank + ", allowDep=" + allowDep + ", department=" + department + ", userId="
				+ userId + ", uploadTime=" + uploadTime + "]";
	}

	
}
