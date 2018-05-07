package com.formssi.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.formssi.entity.ShareFile;

@Component
public interface FileDao {

	ShareFile queryById(String fileId);

	void add(ShareFile shareFile);
	
	List<ShareFile> queryAll(ShareFile shareFile);
}
