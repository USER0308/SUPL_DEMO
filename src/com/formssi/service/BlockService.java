package com.formssi.service;

import com.formssi.entity.Block;

public interface BlockService {
	
	int getBlockHigh() throws Exception;

	Block getBlockTransLogInfo(int blockNumber) throws Exception;
}
