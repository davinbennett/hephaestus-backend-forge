package com.example.main.controllers;

import com.example.main.dto.request.LoginRequest;
import com.example.main.dto.response.LoginResponse;
import com.example.main.dto.response.UserMeResponse;
import com.example.main.security.UserRole;
import com.example.main.services.UserService;
import com.example.main.template.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.main.security.RequiresRoles;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse data = userService.login(request);
        return ResponseEntity.ok(Response.ok(data, "Login successful"));
    }

    @GetMapping("/me")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER})
    public ResponseEntity<Response<UserMeResponse>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new com.example.main.exceptions.UnauthorizedException("Invalid Authorization header format");
        }
        
        String token = authHeader.substring(7);
        UserMeResponse data = userService.getCurrentUser(token);
        return ResponseEntity.ok(Response.ok(data, "Current user profile retrieved successfully"));
    }
}