package com.app.login.service;

import com.app.TestBase;
import com.app.login.Application;
import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.repository.AuthorityRepository;
import com.app.login.repository.UserRepository;
import com.app.login.service.Impl.MailServiceImpl;
import com.app.login.service.Impl.UserServiceImpl;
import com.app.login.service.dto.UserDTO;
import com.app.login.service.mapper.UserMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

/**
 * Created by dev on 2018/5/16.
 */
@RunWith(SpringRunner.class)
public class UserServiceLogicTest extends TestBase {

    @MockBean
    private UserRepository  userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private  AuthorityRepository authorityRepository;

    @MockBean
    private IMailService mailService;

    private UserServiceImpl userServiceImpl=new UserServiceImpl(userRepository, passwordEncoder,
            authorityRepository , mailService);


    @Before
    public void mockUserService(){
        UserDTO userdto = new UserMapper().userToUserDTO(createUser());
        List<UserDTO> userDTOList=new ArrayList<>();
        userDTOList.add(userdto);
        User user=createUser();
        user.setActivationKey(null);
        given(userRepository.save(user)).willReturn(user);
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
}
