package com.app;

import com.app.login.domain.User;
import com.app.login.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class UserRepositoryMockitoTest {

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        ArrayList<User> users=  new ArrayList<User>();
        User mockuser= new User();
        users.add(mockuser);
        Optional<User> optuser=Optional.of(mockuser);

        when(userRepository.findAll()).thenReturn(users);
        when(userRepository.findOneByEmail(any(String.class))).thenReturn(optuser);
    }

    @Test
    public void findAllUsers()  {
        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertTrue(!users.isEmpty());
    }

    @Test
    public void findOneByEmail()  {
        Optional<User> users = userRepository.findOneByEmail("test@fasf.acom");
        assertNotNull(users);
    }
}
