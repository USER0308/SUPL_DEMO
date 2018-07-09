package com.formssi.service;

import java.util.concurrent.ExecutionException;

import com.formssi.entity.IouLimitEntity;

public interface IIouLimitEntityService {
	
	public boolean addIouLimitEntity(String orgID,String orgName,String password,int iouLimit) throws InterruptedException, ExecutionException;  // 新增机构
	
	public boolean setIouLimit(int amount,String updateTime,String orgID) throws InterruptedException, ExecutionException;  // 设定白条额度
	
	public int getIouLimit(String orgID);  // 获取白条额度
	
	public boolean recycleIou(String iouId,int amount)throws InterruptedException, ExecutionException;  // 回收白条

	public int checkPasswordByorgID(String password,String orgID,String Name);//检查password
	
	public IouLimitEntity getIouLimitEntityByConId(String orgID);//根据企业的ID来返回企业的信息

}
