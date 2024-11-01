package com.app.login.web.rest;

import com.app.TestBase;
import com.app.login.repository.UserRepository;
import com.app.login.service.impl.MailServiceImpl;
import com.app.login.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


/**
 * Created by
 * @author Administrator
 * @date 2018/4/25 0025.
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class UserAccountControllerTest extends TestBase {

    @Mock
    private  UserRepository userRepository;

    @Mock
    private  UserServiceImpl userServiceImpl;

    @Mock
    private  MailServiceImpl mailService;

    @Mock
    private  HttpServletRequest httpServletRequest;

    private MockMvc mockMvc;

    @InjectMocks
    private UserAccountController  userAccountController;

    @Before
    public void setUp()  {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userAccountController).build();
        //userAccountController= new UserAccountController(userRepository,userServiceImpl,mailService);
    }

    @After
    public void tearDown()  {
    }

    @Test
    public void registerAccount() throws Exception {
        when(httpServletRequest.getParameter("param1")).thenReturn("true");
        when(httpServletRequest.getParameter("param2")).thenReturn("false");

        userAccountController.registerAccount(creatManagedUserVM(),httpServletRequest);
    }

    @Test
    public void getAccount() throws Exception {
        //mock 行为
        when(userServiceImpl.createUser(Mockito.any())).thenReturn(createUser());

        //构建请求
        RequestBuilder request = MockMvcRequestBuilders.get("/api/account");

        //断言结果
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is(404));
    }



}