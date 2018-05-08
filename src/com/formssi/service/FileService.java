package com.formssi.service;

import java.util.List;

import com.formssi.entity.ShareFile;

public interface FileService {
	
	ShareFile getById(String fileId);

	void add(ShareFile shareFile) throws Exception;
	
	List<ShareFile> queryAll(ShareFile shareFile);
	
	void dowloadFile(ShareFile shareFile);
}
