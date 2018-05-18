package com.formssi.service.impl;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.formssi.entity.Block;
import com.formssi.service.BlockService;
import com.formssi.service.FileShareService;

@Service
public class BlockServiceImpl implements BlockService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public int getBlockHigh() throws Exception{
		int high = 0;
		try {
			BigInteger blockHigh = FileShareService.getBlockHigh();
			high = Integer.valueOf(String.valueOf(blockHigh));
			logger.debug("getBlockHigh blockchain response:",blockHigh);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getBlockHigh to blockchain filed!!");
			throw new Exception("getBlockHigh to blockchain made a exception!");
		}
		return high;
	}

	public Block getBlockTransLogInfo(int blockNumber) throws Exception{
		Block block = null;
		try {
			block = FileShareService.getBlockTransLogInfo(BigInteger.valueOf(blockNumber));
			logger.debug("getBlockTransLogInfo blockchain response:",block);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getBlockTransLogInfo to blockchain filed!!");
			throw new Exception("getBlockTransLogInfo to blockchain made a exception!");
		}
		return block;
	}
}
