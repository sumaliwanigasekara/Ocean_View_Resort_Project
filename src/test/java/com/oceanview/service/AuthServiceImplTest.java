
package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;
import com.oceanview.model.User;
import com.oceanview.service.impl.AuthServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthServiceImplTest {

    @Test
    public void login_userNotFound_returnsFailure() {
        AuthServiceImpl service = new AuthServiceImpl(new FakeUserDAO(null));

        LoginResponse response = service.login(new LoginRequest("user@example.com", "secret"));

        assertFalse(response.isSuccess());
        assertEquals("Invalid credentials.", response.getMessage());
    }

    @Test
    public void login_inactiveUser_returnsFailure() {
        User user = new User(1L, "Asha", "user@example.com", "secret",
                User.UserRole.MANAGER, User.UserStatus.INACTIVE);
        AuthServiceImpl service = new AuthServiceImpl(new FakeUserDAO(user));

        LoginResponse response = service.login(new LoginRequest("user@example.com", "secret"));

        assertFalse(response.isSuccess());
        assertEquals("User is inactive.", response.getMessage());
    }

    @Test
    public void login_wrongPassword_returnsFailure() {
        User user = new User(1L, "Asha", "user@example.com", "secret",
                User.UserRole.MANAGER, User.UserStatus.ACTIVE);
        AuthServiceImpl service = new AuthServiceImpl(new FakeUserDAO(user));

        LoginResponse response = service.login(new LoginRequest("user@example.com", "wrong"));

        assertFalse(response.isSuccess());
        assertEquals("Invalid credentials.", response.getMessage());
    }

    @Test
    public void login_validCredentials_returnsSuccess() {
        User user = new User(1L, "Asha", "user@example.com", "secret",
                User.UserRole.MANAGER, User.UserStatus.ACTIVE);
        AuthServiceImpl service = new AuthServiceImpl(new FakeUserDAO(user));

        LoginResponse response = service.login(new LoginRequest("user@example.com", "secret"));

        assertTrue(response.isSuccess());
        assertEquals("Asha", response.getUserName());
        assertEquals("MANAGER", response.getRole());
    }

    private static class FakeUserDAO implements UserDAO {
        private final User user;

        private FakeUserDAO(User user) {
            this.user = user;
        }

        @Override
        public User findByEmail(String email) {
            return user;
        }
    }
}