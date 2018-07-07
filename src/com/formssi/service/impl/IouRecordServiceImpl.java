package com.formssi.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formssi.dao.IIouLimitEntityDao;
import com.formssi.dao.IIouRecordDao;
import com.formssi.dao.ITransactionDao;
import com.formssi.entity.IouLimitEntity;
import com.formssi.entity.IouRecord;
import com.formssi.service.IIouRecordService;



@Service
public class IouRecordServiceImpl implements IIouRecordService{

	@Autowired(required=false)
	private IIouRecordDao iiouRecordDao;
	@Autowired(required=false)
	private ITransactionDao itransactionDao;
	@Autowired(required=false)  
	private IIouLimitEntityDao iiouLimitEntityDao;

	@Override
	public List<IouRecord> getIouRecordList(int pageNo,int pageSize) {
		// TODO Auto-generated method stub
		pageNo--;
		List<IouRecord> allIouRecord = iiouRecordDao.queryAllIouRecord();
		List<IouRecord> result = new ArrayList<>();
		for(int i=pageNo*pageSize;i<allIouRecord.size()&&i<(pageNo+1)*pageSize;i++) {
			result.add(allIouRecord.get(i));
		}
		return result;
	}

	@Override
	public IouRecord queryIouRecordByIouId(String iouId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IouRecord> queryIouRecordByFromOrg(String fromOrg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IouRecord> queryIouRecordByRecvOrg(String recvOrg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addIouRecord(IouRecord iouRecord) {
		// TODO Auto-generated method stub
		if(iouRecord.getAmount()<= 0) {
			// 确保数据的合理性
			System.out.println("白条金额不可小于0");
			return;
		}
		IouLimitEntity tmpBuy = iiouLimitEntityDao.queryIouLimitEntityByOrgID(iouRecord.getFromOrg());
		IouLimitEntity tmpSale = iiouLimitEntityDao.queryIouLimitEntityByOrgID(iouRecord.getRecvOrg());
		if(tmpBuy==null||tmpSale==null) {
			//不存在该机构
			System.out.println("机构不存在");
			return;
		}else {
			iiouRecordDao.addIouRecord(iouRecord);
			System.out.println("创建白条");
			return;
		}
	}

	@Override
	public List<IouRecord> getAllIouRecord() {
		// TODO Auto-generated method stub
		return iiouRecordDao.queryAllIouRecord();
	}
	
}
