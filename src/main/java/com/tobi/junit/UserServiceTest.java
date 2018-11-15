package com.tobi.junit;

import static com.tobi.user.service.UserServiceImpl.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.tobi.user.dao.UserDao;
import com.tobi.user.dto.Level;
import com.tobi.user.dto.User;
import com.tobi.user.service.UserServiceImpl;
import com.tobi.user.service.UserServiceTx;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	static class TestUserService extends UserServiceImpl {
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

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserDao userDao;

	@Mock
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
	public void upgradeLevels() {
		when(userDao.getAll()).thenReturn(this.users);

		userService.upgradeLevels();

		verify(userDao, times(2)).update(any(User.class));
		verify(userDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(userDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));
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

		verify(userDao, times(2)).add(any(User.class));
	}

	@Test
	public void upgradeAllOrNothing() {
		when(userDao.getAll()).thenReturn(this.users);

		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);

		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);

		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {}

		verify(userDao, times(1)).update(any(User.class));
	}
}
