package com.example.petcare.api;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/pets")
    Call<JSONObject> addPet(@Body RequestBody requestBody);
} 