package com.example.petcare.modelclass;

public class UserLoginRequest {
    String userEmail;
    String userPassword;

    public UserLoginRequest(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}