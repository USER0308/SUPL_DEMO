package com.formssi.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.formssi.entity.Transaction;



public interface ITransactionDao {
	public void addTransaction(Transaction transaction);  // 新增交易
	public void updateTransactionStatusByConID(@Param(value="conID")String conId,@Param(value="status")String status);  // 更新交易状态 --U,未发货--A,已发货
	public Transaction getTransactionByConID(@Param(value="conID")String conID);  // 根据合同号ID查询交易
	public List<Transaction> getAllTransaction();
}
