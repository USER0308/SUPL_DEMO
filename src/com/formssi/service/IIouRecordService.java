package com.formssi.service;
import java.util.List;

import com.formssi.entity.IouRecord;


public interface IIouRecordService {

	public void addIouRecord(IouRecord iouRecord);  // 添加白条记录
	
	public IouRecord queryIouRecordByIouId(String iouId);  // 通过iouId获取白条交易记录
	
	public List<IouRecord> queryIouRecordByFromOrg(String fromOrg);  // 通过发行方id获取白条交易记录
	
	public List<IouRecord> queryIouRecordByRecvOrg(String recvOrg);  // 通过接受方id获取白条交易记录
	
	public List<IouRecord> getIouRecordList(int pageNo,int pageSize);  //查询所有的白条记录
	
	public List<IouRecord> getAllIouRecord();  //查询所有白条记录
	
}
