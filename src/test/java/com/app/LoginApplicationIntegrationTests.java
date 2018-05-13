package com.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
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
import com.app.login.service.Impl.UserServiceImpl;
import com.app.login.service.dto.UserDTO;
import com.app.login.web.rest.vm.LoginVM;

/**
 * LoginApplicationIntegrationTests
 * ref:http://www.springframework.net/rest/refdoc/resttemplate.html
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginApplicationIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserServiceImpl userServiceImpl;

	private String jwtTokenForTest;

	@Before
	public void setUp() {

		//register user from service
		User user = userServiceImpl.createUser("roger", "perfect", "roger", "federer", "roger@hotmail.com", "", "en",
				Instant.now(), "127.0.0.1");

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
		assertEquals("roger", responseEntity.getBody().getFirstName());
	}

	private void login() {
		LoginVM loginVM = new LoginVM();
		loginVM.setUsername("roger");
		loginVM.setPassword("perfect");
		loginVM.setRememberMe(true);

		Map<String, String> params = new HashMap();
		ResponseEntity<String> response = restTemplate.postForEntity("/api/authenticate", loginVM, String.class,
				params);
		jwtTokenForTest = response.getHeaders().get("Authorization").get(0);
	}
	
}
