package com.tobi.database.sqlservice;

import com.tobi.database.exception.SqlRetrievalFailureException;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
