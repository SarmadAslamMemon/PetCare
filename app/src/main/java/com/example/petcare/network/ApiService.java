package com.example.petcare.network;

import com.example.petcare.modelclass.UserLoginRequest;
import com.example.petcare.modelclass.UserRegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({"Accept: */*", "Content-Type: application/json"})
    @POST("user/register")
    Call<Void> registerUser(@Body UserRegisterRequest request);

    @Headers({"Accept: */*", "Content-Type: application/json"})
    @POST("user/login")
    Call<Void> loginUser(@Body UserLoginRequest request);
}