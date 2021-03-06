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

	public void executeSql(final String query, final Object... args) throws SQLException {
		workWithStatementStrategy(
			(Connection c) -> {
				PreparedStatement ps = c.prepareStatement(query);
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i + 1, args[i]);
				}

				return ps;
			}
		);
	}
}
