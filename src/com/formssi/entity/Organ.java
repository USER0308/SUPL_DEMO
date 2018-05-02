package com.formssi.entity;

import java.math.BigInteger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Organ {

	private String orgID;		//机构号
	private String orgName;		//机构名称
	private BigInteger IouLimit;	//白条额度
	private String createTime;	//创建时间
	private String updateTime;	//更新时间

	@JSONField(name = "_orgId")
	public String getOrgID() {
		return orgID;
	}

	@JSONField(name = "_orgId")
	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	@JSONField(name = "_orgName")
	public String getOrgName() {
		return orgName;
	}

	@JSONField(name = "_orgName")
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@JSONField(name = "_iouLimit")
	public BigInteger getIouLimit() {
		return IouLimit;
	}

	@JSONField(name = "_iouLimit")
	public void setIouLimit(BigInteger iouLimit) {
		IouLimit = iouLimit;
	}

	@JSONField(name = "_createTime")
	public String getCreateTime() {
		return createTime;
	}

	@JSONField(name = "_createTime")
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@JSONField(name = "_updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	@JSONField(name = "_updateTime")
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public static Organ parse(String json) {
		Organ object=JSON.parseObject(json, Organ.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	
}
