package com.example.petcare.modelclass;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BlogListResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<com.example.petcare.model.Blog> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<com.example.petcare.model.Blog> getData() {
        return data;
    }
} 