package com.formssi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formssi.dao.IIouLimitEntityDao;
import com.formssi.dao.IIouRecordDao;
import com.formssi.dao.ITransactionDao;
import com.formssi.entity.IouLimitEntity;
import com.formssi.entity.IouRecord;
import com.formssi.entity.Transaction;
import com.formssi.service.ITransactionService;

import utils.Utils;


@Service
public class TransactionServiceImpl implements ITransactionService {
	@Autowired(required=false)
	private ITransactionDao itransactionDao;
	@Autowired(required=false)  
	private IIouLimitEntityDao iiouLimitEntityDao;
	@Autowired(required=false)
	private IIouRecordDao iiouRecordDao;
	
	
	@Override
	public boolean addTransactionRecord(String saleOrg,String buyOrg,String transType,long amount,String latestStatus,String conID,String conHash)throws InterruptedException, ExecutionException{  // 录入交易
		// TODO Auto-generated method stub
		
		long now=System.currentTimeMillis();
		String transTime = Utils.sdf(now);
//		String conID="conID"+transTime;               // 合同号
//	    String conHash="conHash";
		Transaction transaction = new Transaction();
		transaction.setConID(conID);
		transaction.setSaleOrg(saleOrg);
		transaction.setBuyOrg(buyOrg);
		transaction.setTransType(transType);
		transaction.setConID(conID);
		transaction.setConHash(conHash);
		transaction.setAmount(amount);
		transaction.setLatestStatus(latestStatus);
		transaction.setConHash(conHash);
		transaction.setTransTime(transTime);
		transaction.setUpdateTime(transTime);
		
		if(transaction.getAmount()<= 0) {
			// 确保数据的合理性
			System.out.println("白条金额不可小于0");
			return false;
		}
		IouLimitEntity tmpBuy = iiouLimitEntityDao.queryIouLimitEntityByOrgID(transaction.getBuyOrg());
		IouLimitEntity tmpSale = iiouLimitEntityDao.queryIouLimitEntityByOrgID(transaction.getSaleOrg());
		if(tmpBuy==null||tmpSale==null) {
			//不存在该机构
			System.out.println("机构不存在");
			return false;
		}else {
			itransactionDao.addTransaction(transaction);
			System.out.println("创建交易");
			
			
			IOUService.addTransaction(saleOrg, buyOrg, transType, amount, latestStatus,conID,conHash);
			
			IouRecord iouRecord =new IouRecord();
			iouRecord.setAmount((int)amount);
			iouRecord.setIouId(conID);
			iouRecord.setFromOrg(buyOrg);
			iouRecord.setRecvOrg(saleOrg);
			iouRecord.setTransTime(transTime);
			iouRecord.setPaidAmt(0);
			iouRecord.setIouStatus("P");
			iouRecord.setUpdateTime(transTime);
			
			
			
			iiouRecordDao.addIouRecord(iouRecord);
			
			IouLimitEntity iouLimitEntity=iiouLimitEntityDao.queryIouLimitEntityByOrgID(buyOrg);//queryEntityByOrgName(buyOrg);
			if(iouLimitEntity != null) {
				
				int tem = (int) (iouLimitEntity.getIouLimit()-amount);
				
				iiouLimitEntityDao.updateIouLimitByOrgID(tem, transTime, buyOrg);
			}
			else {
				return false;
			}
			return true;
		}
	}

	@Override
	public boolean updateTransactionStatusByConId(String conId,String status) {
		// TODO Auto-generated method stub
		Transaction tmp = itransactionDao.getTransactionByConID(conId);
		if(tmp==null) {
			//不存在该机构
			System.out.println("交易不存在");
			return false;
		}else {
			tmp.setLatestStatus(status);
			long now=System.currentTimeMillis();
			String updateTime = Utils.sdf(now);
			tmp.setUpdateTime(updateTime);
			itransactionDao.updateTransactionStatusByConID(conId, status, updateTime);
			try {
				IOUService.updateTransStatus(conId, status);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
	}

	@Override
	public Transaction getTransactionByConId(String conId) {
		// TODO Auto-generated method stub
		Transaction tmp = itransactionDao.getTransactionByConID(conId);
		if(tmp==null) {
			System.out.println("不存在该交易");
			return null;
		}
		return tmp;
	}

	@Override
	public List<Transaction> getAllTransaction() {
		// TODO Auto-generated method stub
		return itransactionDao.getAllTransaction();
	}

	@Override
	public List<Transaction> queryTransaction(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		pageNo--;
		List<Transaction> allTransaction = itransactionDao.getAllTransaction();
//		System.out.println("读取所有transaction...");
		List<Transaction> result = new ArrayList<>();
		for(int i=pageNo*pageSize;i<allTransaction.size()&&i<(pageNo+1)*pageSize;i++) {
			result.add(allTransaction.get(i));
		}
//		System.out.println("复制transaction...得到的size为");
		return result;
	}

}
