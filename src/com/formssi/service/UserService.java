package com.formssi.service;

import com.formssi.entity.User;

public interface UserService {
	
	User getById(String id);

	public void add(User user);
}
