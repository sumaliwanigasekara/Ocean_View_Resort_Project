package com.oceanview.controller;

import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;
import com.oceanview.service.AuthService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class AuthControllerTest {
    @Test
    public void login_missingEmailOrPassword_returnsFailure() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);

        LoginResponse response = controller.login(new LoginRequest("", "secret"));

        assertFalse(response.isSuccess());
        assertEquals("Email and password are required.", response.getMessage());
        verifyNoInteractions(authService);
    }

    @Test
    public void login_validRequest_delegatesToService() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);
        LoginRequest request = new LoginRequest("user@example.com", "secret");
        LoginResponse expected = LoginResponse.success(1L, "Asha", "Manager");

        when(authService.login(request)).thenReturn(expected);

        LoginResponse response = controller.login(request);

        assertSame(expected, response);
        verify(authService).login(request);
    }
}
