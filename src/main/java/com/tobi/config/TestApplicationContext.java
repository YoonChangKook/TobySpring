package com.tobi.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tobi.database.sqlservice.BaseSqlService;
import com.tobi.database.sqlservice.ConcurrentHashMapSqlRegistry;
import com.tobi.database.sqlservice.JaxbXmlSqlReader;
import com.tobi.database.sqlservice.SqlReader;
import com.tobi.database.sqlservice.SqlRegistry;
import com.tobi.database.sqlservice.SqlService;
import com.tobi.junit.UserServiceTest;
import com.tobi.user.dao.UserDao;
import com.tobi.user.dao.UserDaoJdbc;
import com.tobi.user.service.UserService;
import com.tobi.user.service.UserServiceImpl;

@Configuration
@EnableTransactionManagement
public class TestApplicationContext {
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:3306/tobi?useUnicode=true&characterEncoding=utf8");
		dataSource.setUsername("tobi");
		dataSource.setPassword("tobi123");

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}

	@Bean
	public UserDao userDao() {
		UserDaoJdbc userDao = new UserDaoJdbc();
		userDao.setDataSource(dataSource());
		userDao.setSqlService(sqlService());
		return userDao;
	}

	@Bean
	public UserService userService() {
		UserServiceImpl userService = new UserServiceImpl();
		userService.setUserDao(userDao());
		return userService;
	}

	@Bean
	public UserService testUserService() {
		UserServiceTest.TestUserServiceImpl testUserService = new UserServiceTest.TestUserServiceImpl();
		testUserService.setUserDao(userDao());
		return testUserService;
	}

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
