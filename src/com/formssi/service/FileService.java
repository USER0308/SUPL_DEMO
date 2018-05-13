package com.formssi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.formssi.entity.ShareFile;

public interface FileService {
	
	ShareFile getById(String fileId);

	void add(ShareFile shareFile,String upFileName) throws Exception;
	
	List<ShareFile> queryAll(ShareFile shareFile);
	
	//void dowloadFile(ShareFile shareFile);//登录用户ID
	void dowloadFile(ShareFile shareFile, HttpServletRequest request, HttpServletResponse response);
}
