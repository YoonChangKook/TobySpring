package com.tobi.junit;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class PointcutExpressionTest {
	public interface TargetInterface {
		void hello();
		void hello(String a);
		int minus(int a, int b) throws RuntimeException;
		int plus(int a, int b);
		void method();
	}

	public class Target implements TargetInterface {
		@Override
		public void hello() {}
		@Override
		public void hello(String a) {}
		@Override
		public int minus(int a, int b) throws RuntimeException { return 0; }
		@Override
		public int plus(int a, int b) { return 0; }
		@Override
		public void method() {}
	}

	public class Bean {
		public void method() throws RuntimeException {}
	}

	@Test
	public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public int com.tobi.junit.PointcutExpressionTest.Target.minus(int, int)) throws java.lang.RuntimeException");

		// Target.minus()
		assertThat(pointcut.getClassFilter().matches(Target.class) &&
			pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null), is(true));

		// Target.plus()
		assertThat(pointcut.getClassFilter().matches(Target.class) &&
			pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null), is(false));

		// Bean.method()
		assertThat(pointcut.getClassFilter().matches(Bean.class) &&
			pointcut.getMethodMatcher().matches(Target.class.getMethod("method"), null), is(false));
	}
}
