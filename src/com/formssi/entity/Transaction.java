package com.formssi.entity;

public class Transaction {
	private String conID;               // 合同号
    private String saleOrg;             // 销售方机构 
    private String buyOrg;              // 购买方机构
    private String transType;           // 交易类型
    private long amount;                 // 白条金额
    private String conHash;             // 合同hash
    private String latestStatus;        // 最新状态 --U,未发货--A,已发货
    private String transTime;           // 交易时间
    private String updateTime;          // 更新时间
    
    // getter methods
    public String getConID() {
    	return this.conID;
    }
    
    public String getSaleOrg() {
    	return this.saleOrg;
    }
    
    public String getBuyOrg() {
    	return this.buyOrg;
    }
    
    public String getTransType() {
    	return this.transType;
    }
    
    public long getAmount() {
    	return this.amount;
    }
    
    public String getConHash() {
    	return this.conHash;
    }
    
    public String getLatestStatus() {
    	return this.latestStatus;
    }
    
    public String getTransTime() {
    	return this.transTime;
    }
    
    public String getUpdateTime() {
    	return this.updateTime;
    }
    
    public Transaction getTransaction() {
    	return this;
    }
    // setter methods
    
    public void setConID(String _conID) {
    	this.conID = _conID;
    }
    
    public void setSaleOrg(String _saleOrg) {
    	this.saleOrg = _saleOrg;
    }
    
    public void setBuyOrg(String _buyOrg) {
    	this.buyOrg = _buyOrg;
    }
    
    public void setTransType(String _transType) {
    	this.transType = _transType;
    }
    
    public void setAmount(long _amount) {
    	this.amount = _amount;
    }
    
    public void setConHash(String _conHash) {
    	this.conHash = _conHash;
    }
    
    public void setLatestStatus(String _latestStatus) {
    	this.latestStatus = _latestStatus;
    }
    
    public void setTransTime(String _transTime) {
    	this.transTime = _transTime;
    }
    
    public void setUpdateTime(String _updateTime) {
    	this.updateTime = _updateTime;
    }
    
    public void setTransaction(Transaction transaction) {
    	this.conID = transaction.conID;
    	this.buyOrg = transaction.buyOrg;
    	this.saleOrg = transaction.saleOrg;
    	this.transType = transaction.transType;
    	this.amount = transaction.amount;
    	this.conHash = transaction.conHash;
    	this.latestStatus = transaction.latestStatus;
    	this.transTime = transaction.transTime;
    	this.updateTime = transaction.updateTime;
    }
    // to json
    
    // from json

}
