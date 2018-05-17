package com.formssi.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.formssi.entity.Block;
import com.formssi.entity.ReturnJson;
import com.formssi.service.BlockService;

@Controller
//@RequestMapping("/login")
public class BlockController {
	
	@Autowired
	private BlockService blockService;
	
	@RequestMapping(value = "/queryLastBlockHigh", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String queryLastBlockHigh(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问

		ReturnJson returnJson = new ReturnJson();
		
		try {
			int blockHigh = blockService.getBlockHigh();
			returnJson.setSuccess(true);
			returnJson.setMessage("查询区块链块高成功！");
			returnJson.setObj(blockHigh);
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("查询区块链块高失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
	
	@RequestMapping(value = "/queryBlockInfoByHeight", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String queryBlockInfoByHeight(@RequestParam("data") String date, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");//跨域访问

		Block blockInput = Block.parse(date);
		ReturnJson returnJson = new ReturnJson();
		
		try {
			Block block = blockService.getBlockTransLogInfo(Integer.valueOf(blockInput.getBlockNumber()));
			returnJson.setSuccess(true);
			returnJson.setMessage("通过块高查询区块链信息成功！");
			returnJson.setObj(block);
		}catch(Exception e) {
			returnJson.setSuccess(false);
			returnJson.setMessage("通过块高查询区块链信息失败！");
			return returnJson.toJSON();
		}
		
		return returnJson.toJSON();
	}
	
}
