package com.formssi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formssi.dao.FileDao;
import com.formssi.entity.ShareFile;
import com.formssi.service.FileService;
import com.formssi.service.FileShareService;

@Service
public class FileServiceImpl implements FileService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入Service依赖
	@Autowired
	private FileDao fileDao;

	@Override
	public ShareFile getById(String fileId) {
		return fileDao.queryById(fileId);
	}

	@Override
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public void add(ShareFile shareFile) throws Exception {
		fileDao.add(shareFile);
		try {
			FileShareService.UploadFile(shareFile);
		} catch (Exception e) {
			logger.error("Upload File to blockchain filed!!");
			throw new Exception("add file to blockchain made a exception!");
		}
		
	}

	@Override
	public List<ShareFile> queryAll(ShareFile shareFile) {
		
		return fileDao.queryAll(shareFile);
	}

	@Override
	public void dowloadFile(ShareFile shareFile) {
		// TODO Auto-generated method stub
		//调区块链请求
	}

}
