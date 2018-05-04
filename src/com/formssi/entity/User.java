package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
/**
 * 
 * @author Administrator
 * 1    2    3    4    5    6    7    8    9    10
 * 上将     中将       少将      大校       上校        中校      少校       上尉        中尉         少尉
 *  
 *  1    2    3    4
 *  司令部   政治部   后勤部    装备部
 */

public class User {
	
	private String userId;		//	登录用户Id	上链&存数据库
	private String userName;	//	用户名	存数据库
	private String password;	//	登录密码	存数据库
	private String pubKey;		//	用户公钥	上链
	private int rank;			//	军衔  1~10	上链
	private int department;		//	所属部门	上链
	private String createTime;	//	创建时间	上链
	private String updateTime;	//	更新时间	上链

	@JSONField(name = "userId")
	public String getUserId() {
		return userId;
	}
	@JSONField(name = "userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JSONField(name = "userName")
	public String getUserName() {
		return userName;
	}
	@JSONField(name = "userName")
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@JSONField(name = "password")
	public String getPassword() {
		return password;
	}
	@JSONField(name = "password")
	public void setPassword(String password) {
		this.password = password;
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
	public int getDepartment() {
		return department;
	}
	@JSONField(name = "department")
	public void setDepartment(int department) {
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
