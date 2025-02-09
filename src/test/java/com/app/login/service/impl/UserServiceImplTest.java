package com.app.login.service.impl;

import com.app.login.common.utils.ValidationFacade;
import com.app.login.config.Constants;
import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.repository.AuthorityRepository;
import com.app.login.repository.UserRepository;
import com.app.login.security.AuthoritiesConstants;
import com.app.login.security.SecurityUtils;
import com.app.login.service.IMailService;
import com.app.login.service.IUserService;
import com.app.login.service.dto.UserDTO;
import com.app.login.service.events.EmailNoticeEvent;
import com.app.login.service.mapper.UserMapper;
import com.app.login.service.util.RandomUtil;
import com.app.login.web.rest.util.HeaderUtil;
import com.app.login.web.rest.vm.KeyAndPasswordVM;
import com.app.login.web.rest.vm.ManagedUserVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private ValidationFacade validationFacade;

    @Mock
    private IMailService mailService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUserAccount_LoginAlreadyInUse_ShouldReturnBadRequest() {
        // 设置
        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin("existingUser");


        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);

        when(userRepository.findOneByLogin("existinguser")).thenReturn(Optional.of(new User()));

        // 执行
        ResponseEntity responseEntity = userService.registerUserAccount(managedUserVM, textPlainHeaders, "127.0.0.1");

        // 断言
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("login already in use", responseEntity.getBody());
    }




}
