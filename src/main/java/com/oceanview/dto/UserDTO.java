package com.oceanview.dto;

import com.oceanview.model.User;

import java.time.LocalDateTime;

public class UserDTO {
    private Long userId;
    private String userName;
    private String password;
    private String userEmail;
    private User.UserRole userRole;
    private User.UserStatus userStatus;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public UserDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public User.UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(User.UserRole userRole) {
        this.userRole = userRole;
    }

    public User.UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(User.UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
