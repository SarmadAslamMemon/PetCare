package com.example.petcare.modelclass;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PetFoodTimeResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<PetFoodTime> data;

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

    public List<PetFoodTime> getData() {
        return data;
    }

    public void setData(List<PetFoodTime> data) {
        this.data = data;
    }
} 