package com.app;

import com.app.login.domain.Authority;
import com.app.login.domain.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by TestBase
 * @author Administrator
 * @date 2018/4/22 0022.
 */
public class TestBase {

    /**
     * createUser
     * @return User
     */
    protected final User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Peter");
        user.setLastName("Liu");
        user.setPassword("batman");
        user.setCreatedDate(Instant.now());
        user.setEmail("ljylun@163.com");
        user.setIpAddress("127.0.0.1");
        user.setLogin("Peter");
        user.setActivated(true);
        user.setImageUrl("asfa.png");
        user.setLangKey("en-us");
        user.setCreatedDate(Instant.now());
        user.setAuthorities(createAuthorities());
        return user;
    }

    /**
     *    createAuthorities
     *
     */
    protected final Set<Authority> createAuthorities() {
        Set<Authority> mockauthorityset= new HashSet<>();
        Authority authority=new Authority();
        authority.setName("ROLE_USER");
        boolean isSucess=mockauthorityset.add(authority);

        return  mockauthorityset;
    }
}
