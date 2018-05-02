package com.formssi.entity;

import java.math.BigInteger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Transaction {
	private String conID;		//合同号
	private String saleOrg;		//销售方机构
	private String buyOrg;		//购买方机构
	private String transType;	//交易类型 
	private BigInteger amount;	//白条金额
	private String conHash;		//合同hash
	private String latestStatus;//最新状态
	private String transTime;	//交易时间
	private String updateTime;	//更新时间

	


	@Override
	public String toString() {
		return "Transaction [conID=" + conID + ", saleOrg=" + saleOrg + ", buyOrg=" + buyOrg + ", transType="
				+ transType + ", amount=" + amount + ", conHash=" + conHash + ", latestStatus=" + latestStatus
				+ ", transTime=" + transTime + ", updateTime=" + updateTime + "]";
	}

	@JSONField(name = "conId")
	public String getConID() {
		return conID;
	}

	@JSONField(name = "conId")
	public void setConID(String conID) {
		this.conID = conID;
	}

	@JSONField(name = "saleOrg")
	public String getSaleOrg() {
		return saleOrg;
	}

	@JSONField(name = "saleOrg")
	public void setSaleOrg(String saleOrg) {
		this.saleOrg = saleOrg;
	}

	@JSONField(name = "buyOrg")
	public String getBuyOrg() {
		return buyOrg;
	}

	@JSONField(name = "buyOrg")
	public void setBuyOrg(String buyOrg) {
		this.buyOrg = buyOrg;
	}

	@JSONField(name = "transType")
	public String getTransType() {
		return transType;
	}

	@JSONField(name = "transType")
	public void setTransType(String transType) {
		this.transType = transType;
	}

	@JSONField(name = "amount")
	public BigInteger getAmount() {
		return amount;
	}

	@JSONField(name = "amount")
	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	@JSONField(name = "conHash")
	public String getConHash() {
		return conHash;
	}

	@JSONField(name = "conHash")
	public void setConHash(String conHash) {
		this.conHash = conHash;
	}

	@JSONField(name = "latestStatus")
	public String getLatestStatus() {
		return latestStatus;
	}

	@JSONField(name = "latestStatus")
	public void setLatestStatus(String latestStatus) {
		this.latestStatus = latestStatus;
	}

	@JSONField(name = "transTime")
	public String getTransTime() {
		return transTime;
	}

	@JSONField(name = "transTime")
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	@JSONField(name = "updateTime")
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	@JSONField(name = "updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	public static Transaction parse(String json) {
		Transaction object=JSON.parseObject(json, Transaction.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	
}
