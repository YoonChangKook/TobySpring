package com.tobi.database.sqlservice;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
