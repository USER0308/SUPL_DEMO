package com.formssi.service;

import java.util.List;

import com.formssi.entity.User;

public interface UserService {
	
	User getById(String id);

	void add(User user) throws Exception;
	
	List<User> queryUser(User user);
	
	void updateUser(User user) throws Exception;
}
