package com.tobi.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.tobi.config.JUnitContext;
import com.tobi.junit.obj.TestObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JUnitContext.class)
@ActiveProfiles("test")
public class BeanTest {
	@Autowired
	private TestObject testBean1;
	@Autowired
	private TestObject testBean2;
	@Autowired
	private TestObject testBean3;

	@Test
	public void valueTest() {
		assertThat(testBean1.getNum(), is(1));
		assertThat(testBean2.getNum(), is(2));
		assertThat(testBean3.getNum(), is(3));
	}

	@Test
	public void sameTest() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.register(JUnitContext.class);
		context.refresh();

		TestObject testBean1 = context.getBean("testBean1", TestObject.class);
		assertThat(this.testBean1.getNum(), is(testBean1.getNum()));
		TestObject testBean2 = context.getBean("testBean2", TestObject.class);
		assertThat(this.testBean2.getNum(), is(testBean2.getNum()));
		TestObject testBean3 = context.getBean("testBean3", TestObject.class);
		assertThat(this.testBean3.getNum(), is(testBean3.getNum()));
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void existTest() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.register(JUnitContext.class);
		context.refresh();

		TestObject testBean4 = context.getBean("testBean4", TestObject.class);
	}
}
