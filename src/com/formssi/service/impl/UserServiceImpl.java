package com.formssi.service.impl;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formssi.dao.UserDao;
import com.formssi.entity.User;
import com.formssi.service.FileShareService;
import com.formssi.service.UserService;

import utils.RSAUtils;
import utils.Utils;

@Service
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入Service依赖
	@Autowired
	private UserDao userDao;
	
	@Transactional(rollbackFor={RuntimeException.class, Exception.class})
	public void add(User user) throws Exception {
		String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"/files/keys/";//获取要写入的文件路径
		File file=new File(basePath);
		if(!file.exists()  && !file.isDirectory()) {
			file.mkdirs();
		}
		String pubKeyFileName=basePath+user.getUserId()+"PUBKEY";
		userDao.add(user);
		Map<String, Object> keyMap=RSAUtils.genKeyPair();
		RSAUtils.keyFiles(keyMap,pubKeyFileName,basePath+user.getUserId()+"PRIKEY");
		//创建用户时，如下三个字段不是页面传递过来的，需要服务端来创建并写入区块链但不需要写入数据库
		user.setPubKey(RSAUtils.getPublicKey(keyMap));
		user.setCreateTime(Utils.getCurrentDate());
		user.setUpdateTime(user.getCreateTime());
		try {
			String respJson=FileShareService.addUser(user);
			logger.debug("addUser blockchain response:",respJson);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("addUser to blockchain filed!!");
			throw new Exception("add user to blockchain made a exception!");
		}
		
	}

	public User getById(String id) {
		return userDao.queryById(id);
	}

//	@Transactional	//该注解是开启事务的注解
//    public int addBooks(BookTest book) throws InterruptedException {
//        System.out.println("test====================================");
//	    int changeInt=0;
//			System.out.println("addBooks");
//	        changeInt +=bookDao.addBooks(book);
//            System.out.println("changeInt"+changeInt);
//
//        return changeInt;
//    }

}
