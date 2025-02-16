package com.app.login.web.rest;


import com.app.login.security.jwt.TokenProvider;
import com.app.login.web.rest.vm.LoginVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserJWTControllerTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserJWTController userJWTController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     测试用户授权接口：当使用有效凭证时，应返回200 OK状态码并携带JWT令牌

     测试场景：
     准备有效的登录凭证（用户名、密码、记住我标记）
     模拟认证成功的流程
     验证返回状态码和响应头中的令牌设置

     @throws Exception 测试过程中可能抛出的异常
     */
    @Test
    @Disabled
    public void authorize_ValidCredentials_ShouldReturnOkWithToken() throws Exception {
        // 准备
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("testUser");
        loginVM.setPassword("testPassword");
        loginVM.setRememberMe(true);

        UserDetails userDetails = new User("testUser", "testPassword", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.createToken(authentication, true)).thenReturn("testToken");

        HttpServletResponse response = mock(HttpServletResponse.class);

        // 执行
        ResponseEntity<?> responseEntity = userJWTController.authorize(loginVM, response);

        // 验证
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(response).addHeader("Authorization", "Bearer testToken");
    }

    @Test
    @Disabled
    public void authorize_InvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // 准备
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("testUser");
        loginVM.setPassword("wrongPassword");

        when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        HttpServletResponse response = mock(HttpServletResponse.class);

        // 执行
        ResponseEntity<?> responseEntity = userJWTController.authorize(loginVM, response);

        // 验证
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid credentials", responseEntity.getBody());
    }
}
