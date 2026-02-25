package com.oceanview.model;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test
    public void gettersSetters_work() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("Asha");
        user.setUserEmail("a@x.com");
        user.setPassword("pw");
        user.setUserRole(User.UserRole.MANAGER);
        user.setUserStatus(User.UserStatus.ACTIVE);

        assertEquals(1L, user.getUserId());
        assertEquals("Asha", user.getUserName());
        assertEquals("a@x.com", user.getUserEmail());
        assertEquals("pw", user.getPassword());
        assertEquals(User.UserRole.MANAGER, user.getUserRole());
        assertEquals(User.UserStatus.ACTIVE, user.getUserStatus());
    }
}
