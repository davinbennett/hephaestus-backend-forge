package com.example.main.services;

import com.example.main.dto.request.LoginRequest;
import com.example.main.dto.response.LoginResponse;
import com.example.main.dto.response.UserMeResponse;
import com.example.main.entity.UserEntity;
import com.example.main.exceptions.UnauthorizedException;
import com.example.main.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String correlationId = MDC.get("correlation_id");

        try {
            UserEntity foundUser = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                    .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

            log.info("{{\"level\":\"info\",\"event\":\"auth_login_success\",\"username\":\"{}\",\"role\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    foundUser.getUsername(), foundUser.getRole().name(), correlationId);

            return new LoginResponse(
                    foundUser.getToken(),
                    foundUser.getUsername(),
                    foundUser.getRole().name() 
            );
        } catch (UnauthorizedException e) {
            log.warn("{{\"level\":\"warn\",\"event\":\"auth_login_failed\",\"username\":\"{}\",\"reason\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    request.getUsername(), e.getMessage(), correlationId);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public UserMeResponse getCurrentUser(String token) {
        String correlationId = MDC.get("correlation_id");

        try {
            UserEntity user = userRepository.findByToken(token)
                    .orElseThrow(() -> new UnauthorizedException("Authentication is required"));

            String roleName = (user.getRole() != null) ? user.getRole().name() : "GUEST";

            return new UserMeResponse(user.getUsername(), roleName);
        } catch (UnauthorizedException e) {
            log.warn("{{\"level\":\"warn\",\"event\":\"auth_login_failed\",\"reason\":\"Invalid or missing token\",\"correlation_id\":\"{}\"}}", 
                    correlationId);
            throw e;
        }
    }
}