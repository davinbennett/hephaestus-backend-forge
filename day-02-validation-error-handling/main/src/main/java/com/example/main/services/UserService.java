package com.example.main.services;

import com.example.main.dto.request.LoginRequest;
import com.example.main.dto.response.LoginResponse;
import com.example.main.dto.response.UserMeResponse;
import com.example.main.entity.UserEntity;
import com.example.main.exceptions.UnauthorizedException;
import com.example.main.repositories.UserRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        UserEntity foundUser = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        return new LoginResponse(
                foundUser.getToken(),
                foundUser.getUsername(),
                foundUser.getRole().name() 
        );
    }

    @Transactional(readOnly = true)
    public UserMeResponse getCurrentUser(String token) {
            UserEntity user = userRepository.findByToken(token)
                    .orElseThrow(() -> new UnauthorizedException("Authentication is required"));

            String roleName = (user.getRole() != null) ? user.getRole().name() : "GUEST";

            return new UserMeResponse(user.getUsername(), roleName);
        }
}