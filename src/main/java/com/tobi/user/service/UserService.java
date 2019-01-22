package com.tobi.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.tobi.user.dto.User;

@Transactional
public interface UserService {
	@Transactional(readOnly = true)
	List<User> getAll();
	void add(User user);
	void upgradeLevels();
	void propagationTest();
}
