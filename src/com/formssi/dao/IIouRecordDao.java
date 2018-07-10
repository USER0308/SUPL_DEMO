package com.formssi.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.formssi.entity.IouRecord;



public interface IIouRecordDao {
	public void addIouRecord(IouRecord iouRecord);  // 新增白条记录
	public void updateIouStatusByIouId(@Param(value="iouId")String iouId, @Param(value="iouStatus")String iouStatus);  // 更新白条状态 --C,已还清--P,未还清
	public void updateIouRecord(IouRecord iouRecord);
	public IouRecord queryIouRecordByIouId(@Param(value="iouId")String iouId);  // 根据白条id查询白条
	public List<IouRecord> queryAllIouRecordByFromOrg(@Param(value="fromOrg")String fromOrg);  //根据发行机构查询所有白条
	public List<IouRecord> queryAllIouRecord();  // 获取所有 iouRecord

}
