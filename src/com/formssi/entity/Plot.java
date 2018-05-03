package com.formssi.entity;

import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Plot {
	
	
	private String plotId;				//策略Id
	private int rank;					//策略规定的最小可以查看的军衔
	private List<String> department;	//指定可以查看的部门
	
	@JSONField(name = "plotId")
	public String getPlotId() {
		return plotId;
	}
	@JSONField(name = "plotId")
	public void setPlotId(String plotId) {
		this.plotId = plotId;
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
	public List<String> getDepartment() {
		return department;
	}
	@JSONField(name = "department")
	public void setDepartment(List<String> department) {
		this.department = department;
	}
	
	public static Plot parse(String json) {
		Plot object=JSON.parseObject(json, Plot.class);
		return object;
	}
	
	public String toJSON() {
		return JSONObject.toJSONString(this);
	}
	@Override
	public String toString() {
		return "Plot [plotId=" + plotId + ", rank=" + rank + ", department=" + department + "]";
	}

}
