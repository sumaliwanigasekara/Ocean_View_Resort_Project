package com.oceanview.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String userName;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String message, Long userId, String userName, String role) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.userName = userName;
        this.role = role;
    }

    public static LoginResponse success(Long userId, String userName, String role) {
        return new LoginResponse(true, "Login successful.", userId, userName, role);
    }

    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
