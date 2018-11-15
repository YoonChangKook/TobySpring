package com.tobi.user.service;

import com.tobi.user.dto.User;

public interface UserService {
	void add(User user);
	void upgradeLevels();
}
