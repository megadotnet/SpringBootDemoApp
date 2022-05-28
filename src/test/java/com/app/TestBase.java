package com.app;

import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.security.AuthoritiesConstants;
import com.app.login.web.rest.vm.ManagedUserVM;

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
        user.setEmail("xxxxxx@google.com");
        user.setIpAddress("127.0.0.1");
        user.setLogin("Peter");
        user.setActivated(true);
        user.setImageUrl("asfa.png");
        user.setLangKey("en-us");
        user.setCreatedDate(Instant.now());
        user.setAuthorities(createAuthorities());
        user.setActivationKey("Peter");
        return user;
    }

    /**
     *    createAuthorities
     *
     */
    protected final Set<Authority> createAuthorities() {
        Set<Authority> mockauthorityset= new HashSet<>();
        Authority authority=new Authority();
        authority.setName(AuthoritiesConstants.USER);
        boolean isSucess=mockauthorityset.add(authority);

        return  mockauthorityset;
    }

    protected final ManagedUserVM creatManagedUserVM() {
        Set<String> authory=new HashSet<>();
        authory.add("ROLE");
        return new ManagedUserVM(1L,"asd","asd"
                        ,"asd","asd","asd",true,"asd","sdd",authory);
    }
}
