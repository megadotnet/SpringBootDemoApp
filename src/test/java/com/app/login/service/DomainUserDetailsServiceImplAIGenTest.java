package com.app.login.service;

import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.repository.UserRepository;
import com.app.login.security.DomainUserDetailsServiceImpl;
import com.app.login.security.UserNotActivatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DomainUserDetailsServiceImplAIGenTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DomainUserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create a test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setActivated(true);
        testUser.setEmail("testuser@example.com");

        // Add some authorities to the test user
        Authority authority = new Authority();
        authority.setName("ROLE_USER");
        testUser.setAuthorities(Collections.singleton(authority));
    }

    @Test
    public void testLoadUserByUsername_Success() {
        // Mock the repository to return the test user
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.of(testUser));

        // Call the method under test
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Verify the interactions with the mock
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("testuser");

        // Assert the returned user details
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testLoadUserByUsername_UserNotActivated() {
        // Create a test user that is not activated
        User inactiveUser = new User();
        inactiveUser.setId(1L);
        inactiveUser.setLogin("inactiveuser");
        inactiveUser.setPassword("encodedPassword");
        inactiveUser.setActivated(false);
        inactiveUser.setEmail("inactiveuser@example.com");

        // Mock the repository to return the inactive user
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.of(inactiveUser));

        // Call the method under test and expect an exception
        assertThrows(UserNotActivatedException.class, () -> userDetailsService.loadUserByUsername("inactiveuser"));

        // Verify the interactions with the mock
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("inactiveuser");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Mock the repository to return an empty optional
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.empty());

        // Call the method under test and expect an exception
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistentuser"));

        // Verify the interactions with the mock
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("nonexistentuser");
    }
}