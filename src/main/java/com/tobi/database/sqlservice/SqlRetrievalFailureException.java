package com.tobi.database.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {
	public SqlRetrievalFailureException(RuntimeException e) {
		super(e);
	}
}
