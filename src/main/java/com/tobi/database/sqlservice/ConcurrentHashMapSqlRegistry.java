package com.tobi.database.sqlservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tobi.database.exception.SqlNotFoundException;
import com.tobi.database.exception.SqlUpdateFailureException;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
	private Map<String, String> sqlmap = new ConcurrentHashMap<>();

	public String findSql(String key) throws SqlNotFoundException {
		String sql = this.sqlmap.get(key);
		if (sql == null) {
			throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		}
		else {
			return sql;
		}
	}

	public void registerSql(String key, String sql) {
		this.sqlmap.put(key, sql);
	}

	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		if (this.sqlmap.get(key) == null) {
			throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
		}

		this.sqlmap.put(key, sql);
	}

	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
			this.updateSql(entry.getKey(), entry.getValue());
		}
	}
}
