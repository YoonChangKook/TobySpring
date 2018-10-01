package com.tobi.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.tobi.user.strategy.StatementStrategy;

public class JdbcContext {
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		try (Connection c = this.dataSource.getConnection();
			 PreparedStatement ps = stmt.makePreparedStatement(c)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}
}
