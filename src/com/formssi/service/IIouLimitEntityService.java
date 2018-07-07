package com.formssi.service;

import java.util.concurrent.ExecutionException;

import com.formssi.entity.IouLimitEntity;

public interface IIouLimitEntityService {
	
	public boolean addIouLimitEntity(String orgID,String orgName,String password,int iouLimit) throws InterruptedException, ExecutionException;  // 新增机构
	
	public boolean setIouLimit(int amount,String updateTime,String orgID) throws InterruptedException, ExecutionException;  // 设定白条额度
	
	public int getIouLimit(String orgID);  // 获取白条额度
	
	public boolean recycleIou(String iouId,int amount)throws InterruptedException, ExecutionException;  // 回收白条

	public boolean checkPasswordByOrgID(String password,String orgID);//检查password
}
