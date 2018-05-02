package com.formssi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formssi.dao.UserDao;
import com.formssi.entity.User;
import com.formssi.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入Service依赖
	@Autowired
	private UserDao userDao;

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
