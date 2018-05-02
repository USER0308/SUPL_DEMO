package com.formssi.entity;

import java.math.BigInteger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class IOU {
	
	private String 	iouId;			//白条ID	
	private String 	fromOrg;		//发行机构	
	private String 	recvOrg;		//接收机构	
	private String 	transTime;		//交易时间	
	private BigInteger 	amount;		//交易金额	
	private BigInteger 	paidAmt;	//已还金额	
	private String 	iouStatus;		//白条状态	已还清/未还清  C/P
	private String 	updateTime;		//更新时间	

	@Override
	public String toString() {
		return "IOU [iouId=" + iouId + ", fromOrg=" + fromOrg + ", recvOrg=" + recvOrg + ", transTime=" + transTime
				+ ", amount=" + amount + ", paidAmt=" + paidAmt + ", iouStatus=" + iouStatus + ", updateTime="
				+ updateTime + "]";
	}
	
	@JSONField(name = "iouId")
	public String getIouId() {
		return iouId;
	}

	@JSONField(name = "iouId")
	public void setIouId(String iouId) {
		this.iouId = iouId;
	}

	@JSONField(name = "fromOrg")
	public String getFromOrg() {
		return fromOrg;
	}

	@JSONField(name = "fromOrg")
	public void setFromOrg(String fromOrg) {
		this.fromOrg = fromOrg;
	}

	@JSONField(name = "recvOrg")
	public String getRecvOrg() {
		return recvOrg;
	}

	@JSONField(name = "recvOrg")
	public void setRecvOrg(String recvOrg) {
		this.recvOrg = recvOrg;
	}

	@JSONField(name = "transTime")
	public String getTransTime() {
		return transTime;
	}

	@JSONField(name = "transTime")
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	@JSONField(name = "amount")
	public BigInteger getAmount() {
		return amount;
	}

	@JSONField(name = "amount")
	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	@JSONField(name = "paidAmt")
	public BigInteger getPaidAmt() {
		return paidAmt;
	}

	@JSONField(name = "paidAmt")
	public void setPaidAmt(BigInteger paidAmt) {
		this.paidAmt = paidAmt;
	}

	@JSONField(name = "iouStatus")
	public String getIouStatus() {
		return iouStatus;
	}

	@JSONField(name = "iouStatus")
	public void setIouStatus(String iouStatus) {
		this.iouStatus = iouStatus;
	}

	@JSONField(name = "updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	@JSONField(name = "updateTime")
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public static IOU parse(String json) {
		IOU object=JSON.parseObject(json, IOU.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	
}
