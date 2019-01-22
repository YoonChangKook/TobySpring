package com.tobi.junit;

import static com.tobi.user.service.UserServiceImpl.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tobi.user.dao.UserDao;
import com.tobi.user.dto.Level;
import com.tobi.user.dto.User;
import com.tobi.user.service.UserService;
import com.tobi.user.service.UserServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserServiceTest {
	static class TestUserServiceImpl extends UserServiceImpl {
		private String id = "minsik";

		@Transactional (propagation = Propagation.NEVER)
		@Override
		public void propagationTest() {
			upgradeLevels();
		}

		@Override
		public List<User> getAll() {
			for (User user : super.getAll()) {
				user.setId("1000");
				super.add(user);
			}
			return null;
		}

		@Override
		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}

			super.upgradeLevel(user);
		}
	}

	static class TestUserServiceException extends RuntimeException {}

	@Autowired
	private UserService userService;

	@Autowired
	private UserService testUserService;

	@Autowired
	private UserDao userDao;

	private List<User> users;

	@Before
	public void setUp() {
		this.users = Arrays.asList(
			new User("yoonchang", "국윤창", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
			new User("hangyul", "김한결", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("sunghyun", "김성현", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1),
			new User("minsik", "황민식", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
			new User("hyuknae", "권혁내", "p5", Level.GOLD, 100, 100)
		);
	}

	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}

		userService.upgradeLevels();

		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	@Test
	public void add() {
		userDao.deleteAll();

		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}

	@Test
	public void upgradeAllOrNothing() throws Exception {
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}

		try {
			this.testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException ex) {}

		checkLevelUpgraded(users.get(1), false);
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpgrade = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpgrade.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpgrade.getLevel(), is(user.getLevel()));
		}
	}

	@Test(expected = TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttributeTest() {
		testUserService.getAll();
	}

	@Test
	public void transactionProxyTest() {
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}

		try {
			this.testUserService.propagationTest();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException ex) {}

		checkLevelUpgraded(users.get(1), true);
	}
}
