package com.example.petcare.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pet implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("petName")
    private String petName;

    @SerializedName("petCategory")
    private String petCategory;

    @SerializedName("petImageUrl")
    private String petImageUrl;

    @SerializedName("petAge")
    private String petAge;

    @SerializedName("petBreed")
    private String petBreed;

    @SerializedName("petWeight")
    private int petWeight;

    @SerializedName("petGender")
    private String petGender;

    @SerializedName("fcmToken")
    private String fcmToken;

    @SerializedName("deviceId")
    private String deviceId;

    public Pet(int petCount, int i, String petName, String petGender, int i1, String petCategory, String petFavFood, String petGender1) {
    }

    public Pet() {
    }

    // Getters
    public int getId() { return id; }
    public String getPetName() { return petName; }
    public String getPetCategory() { return petCategory; }
    public String getPetImageUrl() { return petImageUrl; }
    public String getPetAge() { return petAge; }
    public String getPetBreed() { return petBreed; }
    public int getPetWeight() { return petWeight; }
    public String getPetGender() { return petGender; }
    public String getFcmToken() { return fcmToken; }
    public String getDeviceId() { return deviceId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setPetName(String petName) { this.petName = petName; }
    public void setPetCategory(String petCategory) { this.petCategory = petCategory; }
    public void setPetImageUrl(String petImageUrl) { this.petImageUrl = petImageUrl; }
    public void setPetAge(String petAge) { this.petAge = petAge; }
    public void setPetBreed(String petBreed) { this.petBreed = petBreed; }
    public void setPetWeight(int petWeight) { this.petWeight = petWeight; }
    public void setPetGender(String petGender) { this.petGender = petGender; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
