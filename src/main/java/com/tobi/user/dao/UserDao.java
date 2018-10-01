package com.tobi.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import com.tobi.user.dto.User;

public class UserDao {
	private DataSource dataSource;
	private JdbcContext jdbcContext;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}

	public void add(final User user) throws SQLException {
		this.jdbcContext.executeSql("insert into users(id, name, password) values (?, ?, ?)",
			user.getId(),
			user.getName(),
			user.getPassword());
	}

	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();

		User user = null;
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}

		rs.close();
		ps.close();
		c.close();

		if (user == null) {
			throw new EmptyResultDataAccessException(1);
		}

		return user;
	}

	public void deleteAll() throws SQLException {
		this.jdbcContext.executeSql("delete from users");
	}

	public int getCount() throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		ResultSet rs = ps.executeQuery();

		rs.next();
		int count = rs.getInt(1);

		rs.close();
		ps.close();
		c.close();

		return count;
	}
}
