package com.tobi.user.dao;

import java.util.List;

import javax.sql.DataSource;

import com.tobi.user.dto.User;

public interface UserDao {
	void setDataSource(DataSource dataSource);

	void add(User user);

	User get(String id);

	List<User> getAll();

	void deleteAll();

	int getCount();

	void update(User user);
}
