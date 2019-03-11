package com.tobi.database.sqlservice;

import java.util.HashMap;
import java.util.Map;

import com.tobi.database.exception.SqlNotFoundException;

public class HashMapSqlRegistry implements SqlRegistry {
	private Map<String, String> sqlMap;

	public HashMapSqlRegistry() {
		this.sqlMap = new HashMap<>();
	}

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = this.sqlMap.get(key);
		if (sql == null) {
			throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
		}

		return sql;
	}
}
