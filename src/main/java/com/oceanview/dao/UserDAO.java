package com.oceanview.dao;

import com.oceanview.model.User;

public interface UserDAO {
    User findByEmail(String email);
}
