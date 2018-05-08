package com.formssi.service.impl;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formssi.dao.UserDao;
import com.formssi.entity.User;
import com.formssi.service.FileShareService;
import com.formssi.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入Service依赖
	@Autowired
	private UserDao userDao;
	
	@Transactional
	public void add(User user) throws Exception {
		userDao.add(user);
		try {
			FileShareService.addUser(user);
		} catch (InterruptedException | ExecutionException e) {
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
