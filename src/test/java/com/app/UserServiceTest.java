package com.app;

import com.app.login.Application;
import com.app.login.domain.User;
import com.app.login.repository.UserRepository;
import com.app.login.service.Impl.UserServiceImpl;
import com.app.login.service.dto.UserDTO;
import com.app.login.service.mapper.UserMapper;
import javafx.scene.control.Pagination;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * UserServiceTest
 * @author megadotnet
 * @date 2018/4/22
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest extends TestBase {

	@MockBean
	private UserServiceImpl userServiceImpl;

	@Before
	public void mockUserService(){
		UserDTO userdto = new UserMapper().userToUserDTO(createUser());
		List<UserDTO> userDTOList=new ArrayList<>();
		userDTOList.add(userdto);
		User user=createUser();
		given(userServiceImpl.createUser(userdto)).willReturn(user);
		user.setActivationKey(null);
		given(userServiceImpl.activateRegistration("Peter")).willReturn(Optional.of(user));
		Pageable pageable = PageRequest.of(1, 20);
		Page<UserDTO> userPage= new PageImpl<>(userDTOList);
		given(userServiceImpl.getAllManagedUsers(pageable)).willReturn(userPage);

	}

	private UserDTO createUserDto()
	{
		return  new UserMapper().userToUserDTO(createUser());
	}


	@Test
	public void createUserTest() {
		User user=createUser();
		assertNotNull(user);
	}


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

	@Test
	public void getAllManagedUsersTest()
	{
		//assume
		User user = createUser();
		Pageable pageable = PageRequest.of(1, 20);
		//act
		Page<UserDTO> userPage= userServiceImpl.getAllManagedUsers(pageable);

		//assert
		assertNotNull(userPage);
		assertEquals(userPage.getTotalElements(),1L);
		List<UserDTO> userDTOList=userPage.getContent();
		assertNotNull(userDTOList);
		assertFalse(userDTOList.isEmpty());
	}

	@Test
	public void getAuthorities()
	{
		List<String> stringList= userServiceImpl.getAuthorities();
		assertNotNull(stringList);
	}

}
