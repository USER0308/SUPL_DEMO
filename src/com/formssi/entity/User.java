package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class User {
	
	private String userId;		//	用户Id
	private String pubKey;		//	用户公钥
	private int rank;			//	军衔 TODO 需要详细讨论数据
	private String department;	//	所属部门
	private String createTime;	//	创建时间
	private String updateTime;	//	更新时间

	@JSONField(name = "userId")
	public String getUserId() {
		return userId;
	}
	@JSONField(name = "userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JSONField(name = "pubKey")
	public String getPubKey() {
		return pubKey;
	}
	@JSONField(name = "pubKey")
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
	
	@JSONField(name = "rank")
	public int getRank() {
		return rank;
	}
	@JSONField(name = "rank")
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@JSONField(name = "department")
	public String getDepartment() {
		return department;
	}
	@JSONField(name = "department")
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@JSONField(name = "createTime")
	public String getCreateTime() {
		return createTime;
	}
	@JSONField(name = "createTime")
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@JSONField(name = "updateTime")
	public String getUpdateTime() {
		return updateTime;
	}
	@JSONField(name = "updateTime")
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
		return "User [userId=" + userId + ", pubKey=" + pubKey + ", rank=" + rank + ", department=" + department
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	
}
