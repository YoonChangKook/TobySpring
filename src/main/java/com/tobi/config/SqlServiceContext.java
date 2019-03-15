package com.tobi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tobi.database.sqlservice.BaseSqlService;
import com.tobi.database.sqlservice.ConcurrentHashMapSqlRegistry;
import com.tobi.database.sqlservice.JaxbXmlSqlReader;
import com.tobi.database.sqlservice.SqlReader;
import com.tobi.database.sqlservice.SqlRegistry;
import com.tobi.database.sqlservice.SqlService;

@Configuration
public class SqlServiceContext {
	@Bean
	public SqlService sqlService() {
		BaseSqlService sqlService = new BaseSqlService();
		sqlService.setSqlReader(sqlReader());
		sqlService.setSqlRegistry(sqlRegistry());
		return sqlService;
	}

	@Bean
	public SqlReader sqlReader() {
		JaxbXmlSqlReader sqlReader = new JaxbXmlSqlReader();
		sqlReader.setSqlmapFile("sqlmap.xml");
		return sqlReader;
	}

	@Bean
	public SqlRegistry sqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
}
