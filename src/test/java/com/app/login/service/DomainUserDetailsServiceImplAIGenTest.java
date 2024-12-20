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

/**
 * DomainUserDetailsServiceImplAIGenTest
 * @author 通义千问
 */
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
        // Arrange
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Assert
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("testuser");
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
        // Arrange
        User inactiveUser = new User();
        inactiveUser.setId(1L);
        inactiveUser.setLogin("inactiveuser");
        inactiveUser.setPassword("encodedPassword");
        inactiveUser.setActivated(false);
        inactiveUser.setEmail("inactiveuser@example.com");

        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.of(inactiveUser));

        // Act & Assert
        assertThrows(UserNotActivatedException.class, () -> userDetailsService.loadUserByUsername("inactiveuser"));
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("inactiveuser");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistentuser"));
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("nonexistentuser");
    }

    @Test
    public void testLoadUserByUsername_EmptyLogin() {
        // Arrange
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(""));
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("");
    }

    @Test
    public void testLoadUserByUsername_NullLogin() {
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(null));
        verify(userRepository, never()).findOneWithAuthoritiesByLogin(anyString());
    }

    @Test
    public void testLoadUserByUsername_NoAuthorities() {
        // Arrange
        User userWithoutAuthorities = new User();
        userWithoutAuthorities.setId(1L);
        userWithoutAuthorities.setLogin("userwithoutauthorities");
        userWithoutAuthorities.setPassword("encodedPassword");
        userWithoutAuthorities.setActivated(true);
        userWithoutAuthorities.setEmail("userwithoutauthorities@example.com");
        userWithoutAuthorities.setAuthorities(Collections.emptySet());

        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.of(userWithoutAuthorities));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("userwithoutauthorities");

        // Assert
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("userwithoutauthorities");
        assertNotNull(userDetails);
        assertEquals("userwithoutauthorities", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    public void testLoadUserByUsername_CaseInsensitiveLogin() {
        // Arrange
        when(userRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("TESTUSER");

        // Assert
        verify(userRepository, times(1)).findOneWithAuthoritiesByLogin("testuser");
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
}