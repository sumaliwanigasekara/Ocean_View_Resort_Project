package com.oceanview.controller;

import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;
import com.oceanview.service.AuthService;

import java.util.Objects;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = Objects.requireNonNull(authService, "authService");
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null || isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            return LoginResponse.failure("Email and password are required.");
        }
        return authService.login(request);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
