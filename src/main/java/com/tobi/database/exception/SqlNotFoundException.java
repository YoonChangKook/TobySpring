package com.tobi.database.exception;

public class SqlNotFoundException extends RuntimeException {
	public SqlNotFoundException(String message) {
		super(message);
	}
}
