package com.tobi.junit;

import static com.tobi.user.service.UserService.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.tobi.user.dao.UserDao;
import com.tobi.user.dto.Level;
import com.tobi.user.dto.User;
import com.tobi.user.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {
	static class TestUserService extends UserService {
		private String id;

		private TestUserService(String id) {
			this.id = id;
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
	private UserDao userDao;

	@Autowired
	private PlatformTransactionManager transactionManager;

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
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}

	@Test
	public void upgradeLevels() throws Exception {
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

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpgrade = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpgrade.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpgrade.getLevel(), is(user.getLevel()));
		}
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
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setTransactionManager(this.transactionManager);

		this.userDao.deleteAll();
		for (User user : users) {
			this.userDao.add(user);
		}

		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {}

		checkLevelUpgraded(users.get(1), false);
	}
}
