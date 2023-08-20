package com.app.login.security;

import com.app.TestBase;
import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * DomainUserDetailsServiceImplTest
 *
 * @author peter
 * @Description DomainUserDetailsServiceImplTest
 * @date 2023/8/20 0020 14:34
 */
@ExtendWith(MockitoExtension.class)
public class DomainUserDetailsServiceImplTest extends TestBase {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    DomainUserDetailsServiceImpl userDetailsService;

    private final String validLogin = "peter";
    private final String invalidLogin = "nonexistentuser";

    @BeforeEach
    public void setUp() {
       // MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername_ValidUser() {
        // Define mock behavior for UserRepository
        User activatedUser = createUser();
        activatedUser.setLogin(validLogin);
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.of(activatedUser));

        //act
        UserDetails userDetails = userDetailsService.loadUserByUsername(validLogin);

        // Perform assertions on userDetails, e.g., assertEquals
        // Verify that the userRepository method was called with the correct argument
        verify(userRepository).findOneWithAuthoritiesByLogin(validLogin);
    }

    @Test
    public void testLoadUserByUsername_InvalidUser() {
        when(userRepository.findOneWithAuthoritiesByLogin(invalidLogin)).thenReturn(Optional.empty());
        // Using assertThrows to verify the exception
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(invalidLogin);
        });

        // Verify that the userRepository method was called with the correct argument
        verify(userRepository).findOneWithAuthoritiesByLogin(invalidLogin);
    }

    @Test
    public void testLoadUserByUsername_InactiveUser() {
        User inactiveUser = new User();
        inactiveUser.setLogin("inactiveuser");
        inactiveUser.setPassword("password");
        inactiveUser.setActivated(false);

        when(userRepository.findOneWithAuthoritiesByLogin("inactiveuser")).thenReturn(Optional.of(inactiveUser));

        // Using assertThrows to verify the exception
        assertThrows(UserNotActivatedException.class, () -> {
            userDetailsService.loadUserByUsername("inactiveuser");
        });
    }
}

