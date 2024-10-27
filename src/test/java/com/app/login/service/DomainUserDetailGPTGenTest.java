package com.app.login.service;

import com.app.login.domain.User;
import com.app.login.repository.UserRepository;
import com.app.login.security.DomainUserDetailsServiceImpl;
import com.app.login.security.UserNotActivatedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DomainUserDetailGPTGenTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private DomainUserDetailsServiceImpl userDetailsService;

    @Test
    public void testLoadUserByUsername_UserFoundAndActivated() {
        // Arrange
        User user = new User();
        user.setLogin("testUser");
        user.setPassword("password");
        user.setActivated(true);
        user.setAuthorities(Collections.emptySet());

        when(mockUserRepository.findOneWithAuthoritiesByLogin("testuser")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(mockUserRepository.findOneWithAuthoritiesByLogin(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknownUser");
        });
        assertEquals("User unknownuser was not found in the database", exception.getMessage());
    }

    @Test
    public void testLoadUserByUsername_UserNotActivated() {
        // Arrange
        User user = new User();
        user.setLogin("inactiveUser");
        user.setPassword("password");
        user.setActivated(false);
        user.setAuthorities(Collections.emptySet());

        when(mockUserRepository.findOneWithAuthoritiesByLogin("inactiveuser")).thenReturn(Optional.of(user));

        // Act & Assert
        UserNotActivatedException exception = assertThrows(UserNotActivatedException.class, () -> {
            userDetailsService.loadUserByUsername("inactiveUser");
        });
        assertEquals("User inactiveuser was not activated", exception.getMessage());
    }
}
