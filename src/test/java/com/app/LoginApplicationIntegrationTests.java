package com.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.app.login.service.IUserService;
import com.app.login.service.mapper.UserMapper;
import com.app.login.web.rest.vm.ManagedUserVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.login.Application;
import com.app.login.domain.User;
import com.app.login.service.impl.UserServiceImpl;
import com.app.login.service.dto.UserDTO;
import com.app.login.web.rest.vm.LoginVM;

/**
 * LoginApplicationIntegrationTests
 * ref:http://www.springframework.net/rest/refdoc/resttemplate.html
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginApplicationIntegrationTests extends TestBase {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private IUserService userService;

	private String jwtTokenForTest;

	private User testuser;

	@Before
	public void setUp() {

		//register user from service
		//assume
		testuser=createUser();

		//Long id, String login, String password, String firstName, String lastName, String email, boolean activated, String imageUrl, String langKey,Set<String> authorities
		ManagedUserVM managedUserVM=new ManagedUserVM(1L
				, testuser.getLogin()
				, testuser.getPassword()
				, testuser.getFirstName()
				, testuser.getLastName()
				, testuser.getEmail().toLowerCase()
				, true
				, testuser.getImageUrl()
				, testuser.getLangKey()
				, new HashSet<>());
		userService.createUser(managedUserVM,Instant.now(), "127.0.0.1");

		login();
	}


	@Test
	public void getUserAccount() {

		Map<String, String> params = new HashMap();
		ResponseEntity<UserDTO> responseEntity = restTemplate.getForEntity("/api/account", UserDTO.class, params);
		UserDTO user = responseEntity.getBody();
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
		assertNull(user);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", jwtTokenForTest);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		responseEntity = restTemplate.exchange("/api/account", HttpMethod.GET, entity, UserDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(testuser.getFirstName(), responseEntity.getBody().getFirstName());
	}

	private void login() {
		User user=createUser();
		LoginVM loginVM = new LoginVM();
		loginVM.setUsername(user.getLogin());
		loginVM.setPassword(user.getPassword());
		loginVM.setRememberMe(true);

		Map<String, String> params = new HashMap();
		ResponseEntity<String> response = restTemplate.postForEntity("/api/authenticate", loginVM, String.class,
				params);
		assertNotNull(response);
		HttpHeaders httpHeader= response.getHeaders();
		assertNotNull(httpHeader);
		jwtTokenForTest = httpHeader.get("Authorization").get(0);
	}
	
}
