package com.example.petcare.modelclass;

public class PetFoodTime {
    private int petId;
    private String favoriteFood;
    private String eatingTime1;
    private String eatingTime2;
    private String eatingTime3;
    private String fcmToken;
    private String deviceId;

    public PetFoodTime(int petId, String favoriteFood, String eatingTime1, String eatingTime2, String eatingTime3, String fcmToken, String deviceId) {
        this.petId = petId;
        this.favoriteFood = favoriteFood;
        this.eatingTime1 = eatingTime1;
        this.eatingTime2 = eatingTime2;
        this.eatingTime3 = eatingTime3;
        this.fcmToken = fcmToken;
        this.deviceId = deviceId;
    }

    // Getters and Setters
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public String getEatingTime1() {
        return eatingTime1;
    }

    public void setEatingTime1(String eatingTime1) {
        this.eatingTime1 = eatingTime1;
    }

    public String getEatingTime2() {
        return eatingTime2;
    }

    public void setEatingTime2(String eatingTime2) {
        this.eatingTime2 = eatingTime2;
    }

    public String getEatingTime3() {
        return eatingTime3;
    }

    public void setEatingTime3(String eatingTime3) {
        this.eatingTime3 = eatingTime3;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
} 