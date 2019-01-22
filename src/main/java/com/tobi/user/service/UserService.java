package com.tobi.user.service;

import java.util.List;

import com.tobi.user.dto.User;

public interface UserService {
	List<User> getAll();
	void add(User user);
	void upgradeLevels();
}
