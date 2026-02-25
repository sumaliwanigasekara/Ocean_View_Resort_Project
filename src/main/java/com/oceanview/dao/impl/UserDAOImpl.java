package com.oceanview.dao.impl;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    private static final String FIND_BY_EMAIL_SQL =
            "SELECT userId, userName, userEmail, password, userRole, userStatus " +
                    "FROM users WHERE userEmail = ?";

    @Override
    public User findByEmail(String email) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_EMAIL_SQL)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong("userId"));
                user.setUserName(rs.getString("userName"));
                user.setUserEmail(rs.getString("userEmail"));
                user.setPassword(rs.getString("password"));
                user.setUserRole(User.UserRole.valueOf(rs.getString("userRole")));
                user.setUserStatus(User.UserStatus.valueOf(rs.getString("userStatus")));
                return user;
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}