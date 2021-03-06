package com.tobi.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import com.tobi.config.JUnitContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JUnitContext.class)
@ActiveProfiles("test")
public class JUnitTest {
	@Autowired
	private ApplicationContext context;

	static Set<JUnitTest> testObjects = new HashSet<>();
	static ApplicationContext contextObject = null;

	@Test
	public void test1() {
		assertThat(contextObject == null || contextObject == this.context, is(true));
		contextObject = this.context;

		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
	}

	@Test
	public void test2() {
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;

		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
	}

	@Test
	public void test3() {
		assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
		contextObject = this.context;

		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
	}
}
