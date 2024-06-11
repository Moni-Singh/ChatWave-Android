package com.example.chatwave.models.request;

public class LoginRequest {
    private String usernameOrEmail;
    private String password;
    private String fcmToken;

    public LoginRequest(String usernameOrEmail, String password, String fcmToken) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}

