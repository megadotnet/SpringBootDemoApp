package com.app.login.web.rest;

import com.app.TestBase;
import com.app.login.repository.UserRepository;
import com.app.login.service.Impl.MailServiceImpl;
import com.app.login.service.Impl.UserServiceImpl;
import com.app.login.service.dto.UserDTO;
import com.app.login.service.mapper.UserMapper;
import com.app.login.web.rest.vm.ManagedUserVM;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;




/**
 * Created by
 * @author Administrator
 * @date 2018/4/25 0025.
 */
//@Ignore
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

        Set<String> authory=new HashSet<>();
        authory.add("ROLE");
        ManagedUserVM managedUserVM =
                new ManagedUserVM(1L,"asd","asd"
                        ,"asd","asd","asd",true,"asd","sdd",authory);

        userAccountController.registerAccount(managedUserVM,httpServletRequest);
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