package com.example.main.services;

import com.example.main.exceptions.UnauthorizedException;
import com.example.main.repositories.UserRepository;
import com.example.main.dto.request.LoginRequest; 
import com.example.main.dto.response.LoginResponse;
import com.example.main.entity.UserEntity;
import com.example.main.security.UserRole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // ! Login Success
    @Test
    void should_login_successfully_when_username_and_password_are_valid() {
        // given
        LoginRequest request = new LoginRequest("admin_user", "secure_password");
        
        UserEntity user = new UserEntity();
        user.setUsername("admin_user");
        user.setPassword("secure_password"); 
        user.setRole(UserRole.ADMIN);
        user.setToken(UserRole.ADMIN.getToken());

        when(userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword()))
                .thenReturn(Optional.of(user));

        // when
        LoginResponse response = userService.login(request);

        // then
        assertNotNull(response);
        assertEquals("token-admin", response.getToken()); 
        assertEquals("admin_user", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        
        verify(userRepository, times(1)).findByUsernameAndPassword(request.getUsername(), request.getPassword());
    }

    // ! Invalid Password
    @Test
    void should_throw_unauthorized_when_password_is_invalid() {
        // given
        LoginRequest request = new LoginRequest("admin_user", "wrong_password");

        when(userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword()))
                .thenReturn(Optional.empty());

        // when & then
        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> userService.login(request));

        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository, times(1)).findByUsernameAndPassword(request.getUsername(), request.getPassword());
    }
}