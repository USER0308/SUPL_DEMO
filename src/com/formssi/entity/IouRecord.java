package com.formssi.entity;

public class IouRecord {
	private String iouId;               // 白条ID
    private String fromOrg;             // 发行机构 
    private String recvOrg;             // 接收机构
    private String transTime;           // 交易时间
    private int amount;                 // 交易金额
    private int paidAmt;                // 已还金额
    private String iouStatus;           // 白条状态 --C,已还清--P,未还清
    private String updateTime;          // 更新时间
    
    // getter methods
    public String getIouId() {
    	return this.iouId;
    }
    
    public String getFromOrg() {
    	return this.fromOrg;
    }
    
    public String getRecvOrg() {
    	return this.recvOrg;
    }
    
    public String getTransTime() {
    	return this.transTime;
    }
    
    public int getAmount() {
    	return this.amount;
    }
    
    public int getPaidAmt() {
    	return this.paidAmt;
    }
    
    public String getIouStatus() {
    	return this.iouStatus;
    }
    
    public String getUpdateTime() {
    	return this.updateTime;
    }
    
    public IouRecord getIouRecord() {
    	return this;
    }
    // setter methods
    
    public void setIouId(String _iouId) {
    	this.iouId = _iouId;
    }
    
    public void setFromOrg(String _fromOrg) {
    	this.fromOrg = _fromOrg;
    }
    
    public void setRecvOrg(String _recvOrg) {
    	this.recvOrg = _recvOrg;
    }
    
    public void setTransTime(String _transTime) {
    	this.transTime = _transTime;
    }
    
    public void setAmount(int _amount) {
    	this.amount = _amount;
    }
    
    public void setPaidAmt(int _paidAmt) {
    	this.paidAmt = _paidAmt;
    }
    
    public void setIouStatus(String _iouStatus) {
    	this.iouStatus = _iouStatus;
    }
    
    public void setUpdateTime(String _updateTime) {
    	this.updateTime = _updateTime;
    }
    
    public void setIouRecord(IouRecord iouRecord) {
    	this.iouId = iouRecord.iouId;
    	this.fromOrg = iouRecord.fromOrg;
    	this.recvOrg = iouRecord.recvOrg;
    	this.transTime = iouRecord.transTime;
    	this.amount = iouRecord.amount;
    	this.paidAmt = iouRecord.paidAmt;
    	this.iouStatus = iouRecord.iouStatus;
    	this.updateTime = iouRecord.updateTime;
    }
    // to json
    
    // from json

}
