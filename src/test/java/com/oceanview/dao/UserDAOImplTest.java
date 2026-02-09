
package com.oceanview.dao;

import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.model.User;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserDAOImplTest {

    @Test
    public void findByEmail_existingUser_returnsUser() {
        UserDAO dao = new UserDAOImpl();
        User user = dao.findByEmail("manager@oceanview.com");

        assertNotNull(user);
    }
}