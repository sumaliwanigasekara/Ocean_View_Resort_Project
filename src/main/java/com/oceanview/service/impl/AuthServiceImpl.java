package com.oceanview.service.impl;

import com.oceanview.dao.UserDAO;
import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;
import com.oceanview.model.User;
import com.oceanview.service.AuthService;

import java.util.Objects;

public class AuthServiceImpl implements AuthService {
    private final UserDAO userDAO;

    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = Objects.requireNonNull(userDAO, "userDAO");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userDAO.findByEmail(request.getEmail());
        if (user == null) {
            return LoginResponse.failure("Invalid credentials.");
        }
        if (!user.getUserStatus()) {
            return LoginResponse.failure("User is inactive.");
        }
        if (!Objects.equals(user.getPassword(), request.getPassword())) {
            return LoginResponse.failure("Invalid credentials.");
        }
        return LoginResponse.success(user.getUserId(), user.getUserName(), user.getUserRole().name());
    }
}