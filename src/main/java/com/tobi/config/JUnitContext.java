package com.tobi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tobi.junit.obj.TestObject;

@Configuration
public class JUnitContext {
	@Bean
	public TestObject testBean1() {
		TestObject testObject = new TestObject();
		testObject.setNum(1);
		return testObject;
	}

	@Bean
	public TestObject testBean2() {
		TestObject testObject = new TestObject();
		testObject.setNum(2);
		return testObject;
	}

	@Bean
	public TestObject testBean3() {
		TestObject testObject = new TestObject();
		testObject.setNum(3);
		return testObject;
	}
}
