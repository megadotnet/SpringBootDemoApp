package com.app;

import com.app.login.Application;
import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.service.UserService;
import com.app.login.service.dto.UserDTO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.*;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	public void createUserTest() {
		createUser();
	}

	@Test
	public void updateUserTest()
	{
		//assume
		User user = createUser();
		UserDTO userDto=new UserDTO(user);
		//act
		Optional<UserDTO> updateUser = userService.updateUser(userDto);
		//assert
		assertNotNull(updateUser);
	}

	@Test
	public void deleteUser() throws Exception {
		//assume
		User user = createUser();
		//act
		userService.deleteUser(user.getLogin());
		Optional<User> existUser =userService.getUserWithAuthoritiesByLogin(user.getLogin());
		//assert
		assertFalse(existUser.isPresent());
	}

	@Test
	public void activateRegistrationTest()
	{
		//assume
		User user = createUser();
		//act
		Optional<User> activeUser= userService.activateRegistration(user.getActivationKey());

		//assert
		assertTrue(activeUser.isPresent());
		User existUser=activeUser.get();
		assertEquals(existUser.getActivationKey(),null);
	}


	private User createUser() {
		User user = new User();
		user.setFirstName("Peter");
		user.setLastName("Liu");
		user.setPassword("batman");
		user.setCreatedDate(Instant.now());
		user.setEmail("kmdxdk1@hotmail.com");
		user.setIpAddress("127.0.0.1");

		User newUser = userService.createUser(user.getFirstName(), user.getPassword(), user.getFirstName(), user.getLastName(),
				user.getEmail(), "", "en", user.getCreatedDate(), user.getIpAddress());
		assertNotNull(newUser);
		return newUser;
	}
}
