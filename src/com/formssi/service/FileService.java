package com.formssi.service;

import java.util.List;

import com.formssi.entity.ShareFile;

public interface FileService {
	
	ShareFile getById(String fileId);

	void add(ShareFile shareFile,String upFileName) throws Exception;
	
	List<ShareFile> queryAll(ShareFile shareFile);
	
	String dowloadFile(ShareFile shareFile) throws Exception;//登录用户ID
}
