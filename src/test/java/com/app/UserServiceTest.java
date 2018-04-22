package com.app;

import com.app.login.Application;
import com.app.login.domain.User;
import com.app.login.service.Impl.UserServiceImpl;
import com.app.login.service.dto.UserDTO;
import com.app.login.service.mapper.UserMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

	@MockBean
	private UserServiceImpl userServiceImpl;

	@Before
	public void mockUserService(){
		UserDTO userdto = new UserMapper().userToUserDTO(createUser());
		given(userServiceImpl.createUser(userdto)).willReturn(createUser());


	}

	@Ignore("TODO Refactor")
	@Test
	public void createUserTest() {
		User user=createUser();
		assertNotNull(user);
	}

	@Ignore("TODO Refactor")
	@Test
	public void updateUserTest()
	{
		//assume
		User user = createUser();
		UserDTO userDto=new UserDTO(user);
		//act
		Optional<UserDTO> updateUser = userServiceImpl.updateUser(userDto);
		//assert
		assertNotNull(updateUser);
	}

	@Ignore("TODO Refactor")
	@Test
	public void deleteUser() throws Exception {
		//assume
		User user = createUser();
		//act
		userServiceImpl.deleteUser(user.getLogin());
		Optional<User> existUser = userServiceImpl.getUserWithAuthoritiesByLogin(user.getLogin());
		//assert
		assertFalse(existUser.isPresent());
	}

	@Ignore("TODO Refactor")
	@Test
	public void activateRegistrationTest()
	{
		//assume
		User user = createUser();
		//act
		Optional<User> activeUser= userServiceImpl.activateRegistration(user.getActivationKey());

		//assert
		assertTrue(activeUser.isPresent());
		User existUser=activeUser.get();
		assertEquals(existUser.getActivationKey(),null);
	}

	@Ignore("TODO Refactor")
	@Test
	public void getAllManagedUsersTest()
	{
		//assume
		User user = createUser();
		Sort sort = new Sort(Sort.Direction.ASC, "login");
		Pageable pageable = new PageRequest(0, 20);
		//act
		Page<UserDTO> userPage= userServiceImpl.getAllManagedUsers(pageable);

		//assert
		assertNotNull(userPage);
		assertEquals(userPage.getTotalElements(),1L);
		List<UserDTO> userDTOList=userPage.getContent();
		assertNotNull(userDTOList);
		assertFalse(userDTOList.isEmpty());
	}


	private User createUser() {
		User user = new User();
		user.setFirstName("Peter");
		user.setLastName("Liu");
		user.setPassword("batman");
		user.setCreatedDate(Instant.now());
		user.setEmail("kmdxdk1@hotmail.com");
		user.setIpAddress("127.0.0.1");

		//User newUser = userService.createUser(user.getFirstName(), user.getPassword(), user.getFirstName(), user.getLastName(),
		//		user.getEmail(), "", "en", user.getCreatedDate(), user.getIpAddress());
		//assertNotNull(newUser);
		return user;
	}
}
