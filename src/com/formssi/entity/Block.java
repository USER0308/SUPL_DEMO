package com.formssi.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Block {
	
	private String blockNumber;		//	区块号
	private String blockTime;		//	创建区块时间
	private String preBlockHash;	//	前区块hash
	private String currBlockHash;	//	当前区块hash
	private String transInfo;		//	交易信息
	
	@JSONField(name = "blockNumber")
	public String getBlockNumber() {
		return blockNumber;
	}

	@JSONField(name = "blockNumber")
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	@JSONField(name = "blockTime")
	public String getBlockTime() {
		return blockTime;
	}

	@JSONField(name = "blockTime")
	public void setBlockTime(String blockTime) {
		this.blockTime = blockTime;
	}
	
	@JSONField(name = "currBlockHash")
	public String getCurrBlockHash() {
		return currBlockHash;
	}

	@JSONField(name = "currBlockHash")
	public void setCurrBlockHash(String currBlockHash) {
		this.currBlockHash = currBlockHash;
	}

	@JSONField(name = "preBlockHash")
	public String getPreBlockHash() {
		return preBlockHash;
	}

	@JSONField(name = "preBlockHash")
	public void setPreBlockHash(String preBlockHash) {
		this.preBlockHash = preBlockHash;
	}

	@JSONField(name = "transInfo")
	public String getTransInfo() {
		return transInfo;
	}

	@JSONField(name = "transInfo")
	public void setTransInfo(String transInfo) {
		this.transInfo = transInfo;
	}


	public static Block parse(String json) {
		Block object=JSON.parseObject(json, Block.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	
	@Override
	public String toString() {
		return "Block [blockNumber=" + blockNumber + ", blockTime=" + blockTime + ", preBlockHash=" + preBlockHash +", currBlockHash=" + currBlockHash + ", transInfo=" + transInfo + "]";
	}
	
	
}
