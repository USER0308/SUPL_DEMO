package com.formssi.entity;

import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Strategy {
	
	
	private String strategyId;			//策略Id
	private int rank;					//策略规定的最小可以查看的军衔
	private List<Integer> department;	//指定可以查看的部门
	
	@JSONField(name = "strategyId")
	public String getStrategyId() {
		return strategyId;
	}
	@JSONField(name = "strategyId")
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	
	@JSONField(name = "rank")
	public int getRank() {
		return rank;
	}
	
	@JSONField(name = "rank")
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@JSONField(name = "department")
	public List<Integer> getDepartment() {
		return department;
	}
	@JSONField(name = "department")
	public void setDepartment(List<Integer> department) {
		this.department = department;
	}
	
	public static Strategy parse(String json) {
		Strategy object=JSON.parseObject(json, Strategy.class);
		return object;
	}
	

	public String toJSON() {
		return JSONObject.toJSONString(this);
	}


}
