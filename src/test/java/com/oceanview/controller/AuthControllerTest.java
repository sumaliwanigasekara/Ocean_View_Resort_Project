
package com.oceanview.controller;

import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;
import com.oceanview.service.AuthService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthControllerTest {

    @Test
    public void login_missingEmailOrPassword_returnsFailure() {
        AuthController controller = new AuthController(new FakeAuthService());

        LoginResponse response = controller.login(new LoginRequest("", "secret"));

        assertFalse(response.isSuccess());
        assertEquals("Email and password are required.", response.getMessage());
    }

    @Test
    public void login_validRequest_returnsSuccess() {
        AuthController controller = new AuthController(new FakeAuthService());

        LoginResponse response = controller.login(new LoginRequest("user@example.com", "secret"));

        assertTrue(response.isSuccess());
        assertEquals("Asha", response.getUserName());
    }

    @Test
    public void login_invalidEmail_returnsFailure() {
        AuthController controller = new AuthController(new FakeAuthService());

        LoginResponse response = controller.login(new LoginRequest("invalid-email", "secret"));

        assertFalse(response.isSuccess());
        assertEquals("Invalid email format.", response.getMessage());
    }

    private static class FakeAuthService implements AuthService {
        @Override
        public LoginResponse login(LoginRequest request) {
            return LoginResponse.success(1L, "Asha", "Manager");
        }
    }
}