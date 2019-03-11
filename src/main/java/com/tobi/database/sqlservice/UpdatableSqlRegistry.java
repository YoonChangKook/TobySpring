package com.tobi.database.sqlservice;

import java.util.Map;

import com.tobi.database.exception.SqlUpdateFailureException;

public interface UpdatableSqlRegistry extends SqlRegistry {
	void updateSql(String key, String sql) throws SqlUpdateFailureException;

	void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
