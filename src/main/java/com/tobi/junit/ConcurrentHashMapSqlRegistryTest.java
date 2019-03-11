package com.tobi.junit;

import org.junit.Before;
import org.junit.Test;

import com.tobi.database.exception.SqlNotFoundException;
import com.tobi.database.exception.SqlUpdateFailureException;
import com.tobi.database.sqlservice.ConcurrentHashMapSqlRegistry;
import com.tobi.database.sqlservice.UpdatableSqlRegistry;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class ConcurrentHashMapSqlRegistryTest {
	private UpdatableSqlRegistry sqlRegistry;

	@Before
	public void setUp() {
		this.sqlRegistry = new ConcurrentHashMapSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}

	@Test
	public void find() {
		checkFindResult("SQL1", "SQL2", "SQL3");
	}

	private void checkFindResult(String expected1, String expected2, String expected3) {
		assertEquals(expected1, this.sqlRegistry.findSql("KEY1"));
		assertEquals(expected2, this.sqlRegistry.findSql("KEY2"));
		assertEquals(expected3, this.sqlRegistry.findSql("KEY3"));
	}

	@Test(expected = SqlNotFoundException.class)
	public void unknownKeyTest() {
		this.sqlRegistry.findSql("SQL1234");
	}

	@Test
	public void updateSingle() {
		this.sqlRegistry.updateSql("KEY2", "Modified2");
		checkFindResult("SQL1", "Modified2", "SQL3");
	}

	@Test
	public void updateMultiTest() {
		Map<String, String> sqlmap = new HashMap<>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");

		this.sqlRegistry.updateSql(sqlmap);
		checkFindResult("Modified1", "SQL2", "Modified3");
	}

	@Test(expected = SqlUpdateFailureException.class)
	public void updateWithNotExistingKeyTest() {
		this.sqlRegistry.updateSql("SQL1234", "Modified2");
	}
}
