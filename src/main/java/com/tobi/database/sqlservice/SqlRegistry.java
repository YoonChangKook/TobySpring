package com.tobi.database.sqlservice;

import com.tobi.database.exception.SqlNotFoundException;

public interface SqlRegistry {
	void registerSql(String key, String sql);
	String findSql(String key) throws SqlNotFoundException;
}
