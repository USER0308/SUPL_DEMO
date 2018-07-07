package com.formssi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formssi.dao.IIouLimitEntityDao;
import com.formssi.dao.ITransactionDao;
import com.formssi.entity.IouLimitEntity;
import com.formssi.entity.Transaction;
import com.formssi.service.ITransactionService;

import utils.Utils;


@Service
public class TransactionServiceImpl implements ITransactionService {
	@Autowired(required=false)
	private ITransactionDao itransactionDao;
	@Autowired(required=false)  
	private IIouLimitEntityDao iiouLimitEntityDao;
	@Override
	public boolean addTransactionRecord(Transaction transaction) {
		// TODO Auto-generated method stub
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
		List<Transaction> allTransaction = itransactionDao.getAllTransaction();
//		System.out.println("读取所有transaction...");
		List<Transaction> result = new ArrayList<>();
		for(int i=pageNo*pageSize;i<(pageNo+1)*pageSize;i++) {
			result.add(allTransaction.get(i));
		}
//		System.out.println("复制transaction...得到的size为");
		return result;
	}

}
