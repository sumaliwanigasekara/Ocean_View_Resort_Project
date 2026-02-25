package com.oceanview.model;

import java.time.LocalDateTime;

public class User {
    private Long userId;
    private String userName;
    private String password;
    private String userEmail;
    private UserRole userRole;
    private UserStatus userStatus;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public enum UserRole {
        MANAGER, RECEPTIONIST
    }

    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    public User() {}

    public User(Long userId, String userName, String userEmail, String password, UserRole userRole,
               UserStatus userStatus) {
        this.userId =userId;
        this.userEmail = userEmail;
        this.password = password;
        this.userName = userName;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public long getUserId() {return userId; }
    public void setUserId(Long userId) {this.userId = userId;}

    public String getUserEmail() {return userEmail; }
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}

    public String getPassword() {return password; }
    public void setPassword(String password) {this.password = password;}

    public String getUserName() {return userName; }
    public void setUserName(String userName) {this.userName = userName;}

    public UserRole getUserRole() {return userRole; }
    public void setUserRole(UserRole userRole) {this.userRole = userRole;}

    public UserStatus getUserStatus() {return userStatus; }
    public void setUserStatus(UserStatus userStatus) {this.userStatus = userStatus;}

    public LocalDateTime getCreated_at() {return created_at; }
    public void setCreated_at(LocalDateTime created_at) {this.created_at = created_at;}

    public LocalDateTime getUpdated_at() {return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) {this.updated_at = updated_at;}

}
