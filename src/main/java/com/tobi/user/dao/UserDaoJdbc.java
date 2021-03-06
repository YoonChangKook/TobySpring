package com.tobi.user.dao;

import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tobi.database.sqlservice.SqlService;
import com.tobi.user.dto.Level;
import com.tobi.user.dto.User;

@Repository
public class UserDaoJdbc implements UserDao {
	private final SqlService sqlService;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDaoJdbc(SqlService sqlService, DataSource dataSource) {
		this.sqlService = sqlService;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private final RowMapper<User> userMapper = (ResultSet rs, int rowNum) -> {
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		user.setLevel(Level.valueOf(rs.getInt("level")));
		user.setLogin(rs.getInt("login"));
		user.setRecommend(rs.getInt("recommend"));
		return user;
	};

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void add(final User user) {
		this.jdbcTemplate.update(this.sqlService.getSql("userAdd"),
			user.getId(),
			user.getName(),
			user.getPassword(),
			user.getLevel().intValue(),
			user.getLogin(),
			user.getRecommend());
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"),
			new Object[] {id},
			this.userMapper);
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper);
	}

	public void deleteAll() {
		this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
	}

	public int getCount() {
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
	}

	public void update(User user) {
		this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
			user.getName(),
			user.getPassword(),
			user.getLevel().intValue(),
			user.getLogin(),
			user.getRecommend(),
			user.getId());
	}
}
