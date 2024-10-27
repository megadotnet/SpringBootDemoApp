package com.app.login.security;

import com.app.login.domain.User;
import com.app.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 * @author megadotnet
 * @date 2017-12-13
 */
@Component("userDetailsService")
@Slf4j
public class DomainUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public DomainUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user details by the given username.
     * This method is used by Spring Security to authenticate a user.
     *
     * @param login The username of the user to be authenticated.
     * @return The UserDetails object representing the authenticated user.
     * @throws UsernameNotFoundException If the user with the given username is not found in the database.
     * @throws UserNotActivatedException If the user is found but is not activated.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(final String login) {
        if (login==null){
            throw  new UsernameNotFoundException("User login is null");
        }
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
                    if (!user.isActivated()) {
                        throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
                    }
                    List<GrantedAuthority> grantedAuthorities = user.getAuthorities()
                            .stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                            .collect(Collectors.toList());
                    return new org.springframework.security.core.userdetails.User(lowercaseLogin, user.getPassword(), grantedAuthorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " + "database"));
    }

}
