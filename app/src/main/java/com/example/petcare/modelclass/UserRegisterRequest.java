package com.example.petcare.modelclass;

public class UserRegisterRequest {
    String firstName;
    String lastName;
    String userAddress;
    String userAge;
    String userPhone;
    String userEmail;
    String userPassword;

    public UserRegisterRequest(String firstName, String lastName, String userAddress, String userAge, String userPhone, String userEmail, String userPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAddress = userAddress;
        this.userAge = userAge;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}