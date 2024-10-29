package com.app.login.security;

import com.app.login.domain.Authority;
import com.app.login.domain.User;
import com.app.login.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * DomainUserDetailsServiceImplClaudeTest for the DomainUserDetailsServiceImpl class.
 */
@ExtendWith(MockitoExtension.class)
public class DomainUserDetailsServiceImplClaudeTest {

    @Mock
    private UserRepository userRepository;

    private DomainUserDetailsServiceImpl userDetailsService;

    private User testUser;
    private static final String TEST_LOGIN = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_AUTHORITY = "ROLE_USER";

    @BeforeEach
    void setUp() {
        userDetailsService = new DomainUserDetailsServiceImpl(userRepository);
        testUser = createTestUser();
    }

    @Test
    void loadUserByUsername_WhenUserExistsAndActivated_ReturnsUserDetails() {
        // Arrange
        when(userRepository.findOneWithAuthoritiesByLogin(TEST_LOGIN.toLowerCase()))
            .thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(TEST_LOGIN);

        // Assert
        assertNotNull(userDetails);
        assertEquals(TEST_LOGIN, userDetails.getUsername());
        assertEquals(TEST_PASSWORD, userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals(TEST_AUTHORITY)));
        verify(userRepository, times(1))
            .findOneWithAuthoritiesByLogin(TEST_LOGIN.toLowerCase());
    }

    @Test
    void loadUserByUsername_WhenUserNotFound_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.findOneWithAuthoritiesByLogin(anyString()))
            .thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername(TEST_LOGIN));
        
        assertTrue(exception.getMessage().contains("was not found in the database"));
        verify(userRepository, times(1))
            .findOneWithAuthoritiesByLogin(TEST_LOGIN.toLowerCase());
    }

    @Test
    void loadUserByUsername_WhenUserNotActivated_ThrowsUserNotActivatedException() {
        // Arrange
        testUser.setActivated(false);
        when(userRepository.findOneWithAuthoritiesByLogin(TEST_LOGIN.toLowerCase()))
            .thenReturn(Optional.of(testUser));

        // Act & Assert
        Exception exception = assertThrows(UserNotActivatedException.class, () -> 
            userDetailsService.loadUserByUsername(TEST_LOGIN));
        
        assertTrue(exception.getMessage().contains("was not activated"));
        verify(userRepository, times(1))
            .findOneWithAuthoritiesByLogin(TEST_LOGIN.toLowerCase());
    }

    @Test
    void loadUserByUsername_WhenLoginIsNull_ThrowsUsernameNotFoundException() {
        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername(null));
        
        assertEquals("User login is null", exception.getMessage());
        verify(userRepository, never()).findOneWithAuthoritiesByLogin(anyString());
    }

    @Test
    void loadUserByUsername_WithDifferentCase_ConvertedToLowerCase() {
        // Arrange
        String mixedCaseLogin = "TeStUsEr";
        when(userRepository.findOneWithAuthoritiesByLogin(mixedCaseLogin.toLowerCase()))
            .thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(mixedCaseLogin);

        // Assert
        assertNotNull(userDetails);
        assertEquals(mixedCaseLogin.toLowerCase(), userDetails.getUsername());
        verify(userRepository, times(1))
            .findOneWithAuthoritiesByLogin(mixedCaseLogin.toLowerCase());
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin(TEST_LOGIN);
        user.setPassword(TEST_PASSWORD);
        user.setActivated(true);

        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(TEST_AUTHORITY);
        authorities.add(authority);
        user.setAuthorities(authorities);

        return user;
    }
}