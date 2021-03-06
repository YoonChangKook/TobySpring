package com.tobi.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tobi.config.AppContext;
import com.tobi.config.TestAppContext;
import com.tobi.user.dao.UserDaoJdbc;
import com.tobi.user.dto.Level;
import com.tobi.user.dto.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppContext.class, TestAppContext.class})
@ActiveProfiles("test")
public class UserDaoTest {
	@Autowired
	private UserDaoJdbc dao;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		this.user1 = new User("hwang", "황민식", "minsik123", Level.BASIC, 1, 0);
		this.user2 = new User("moon", "문기선", "gisun123", Level.SILVER, 55, 10);
		this.user3 = new User("kwon", "권혁내", "hyuknae123", Level.GOLD, 100, 40);

		System.out.println(this.dao);
		System.out.println(this);
	}

	@Test
	public void addAndGet() {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);

		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
	}

	@Test
	public void count() {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.get("unknown_id");
	}

	@Test
	public void getAll() {
		dao.deleteAll();

		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));

		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));

		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));

		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0));
		checkSameUser(user3, users3.get(1));
		checkSameUser(user2, users3.get(2));
	}

	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}

	@Test
	public void update() {
		dao.deleteAll();
		dao.add(user1);

		user1.setName("김성현");
		user1.setPassword("sunghyun123");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		dao.update(user1);

		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
	}
}
