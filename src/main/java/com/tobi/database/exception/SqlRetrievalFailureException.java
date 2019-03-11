package com.tobi.database.exception;

public class SqlRetrievalFailureException extends RuntimeException {
	public SqlRetrievalFailureException(RuntimeException e) {
		super(e);
	}
}
