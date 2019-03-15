package com.tobi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.tobi.junit.UserServiceTest.TestUserServiceImpl;
import com.tobi.user.service.UserService;

@Configuration
@Profile("test")
public class TestAppContext {
	@Bean
	public UserService testUserService() {
		return new TestUserServiceImpl();
	}
}
