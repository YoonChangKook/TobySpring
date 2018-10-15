package com.tobi.user.service;

import com.tobi.user.dao.UserDao;

public class UserService {
	UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
