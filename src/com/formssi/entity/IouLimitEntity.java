package com.formssi.entity;

public class IouLimitEntity {
	private String orgID;                 // 机构号
    private String orgName;               // 机构名称 
    private String password;              // 登录密码
    private int iouLimit;                 // 白条额度
    private String createTime;            // 创建时间
    private String updateTime;            // 更新时间

    // getter methods
    public String getOrgID() {
    	return this.orgID;
    }
    
    public String getOrgName() {
    	return this.orgName;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    public int getIouLimit() {
    	return this.iouLimit;
    }
    
    public String getCreateTime() {
    	return this.createTime;
    }
    
    public String getUpdateTime() {
    	return this.updateTime;
    }
    
    public IouLimitEntity getIouLimitEntity() {
    	return this;
    }
    // setter methods
    public void setOrgID(String _orgID) {
    	this.orgID = _orgID;
    }
    
    public void setOrgName(String _orgName) {
    	this.orgName = _orgName;
    }
    
    public void setPassword(String _password) {
    	this.password = _password;
    }
    
    public void setIouLimit(int _iouLimit) {
    	this.iouLimit = _iouLimit;
    }
    
    public void setCreateTime(String _createTime) {
    	this.createTime = _createTime;
    }
    
    public void setUpdateTime(String _updateTime) {
    	this.updateTime = _updateTime;
    }
    
    public void setIouLimitEntity(IouLimitEntity _iouLimitEntity) {
    	this.orgID = _iouLimitEntity.orgID;
    	this.orgName = _iouLimitEntity.orgName;
    	this.password = _iouLimitEntity.password;
    	this.iouLimit = _iouLimitEntity.iouLimit;
    	this.createTime = _iouLimitEntity.createTime;
    	this.updateTime = _iouLimitEntity.updateTime;
    }
    // to json
    
    // from json
}
