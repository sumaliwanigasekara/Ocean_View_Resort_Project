package com.oceanview.service;

import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
