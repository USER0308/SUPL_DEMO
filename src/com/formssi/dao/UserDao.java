package com.formssi.dao;

import org.springframework.stereotype.Component;

import com.formssi.entity.User;

@Component
public interface UserDao {

	User queryById(String id);

	void add(User user);
}
