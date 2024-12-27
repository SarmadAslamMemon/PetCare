package com.example.petcare.modelclass;

public class HealthTip {

    private String title;
    private String description;
    private String lottieRawRes;

    // Constructor
    public HealthTip(String title, String description, String lottieRawRes) {
        this.title = title;
        this.description = description;
        this.lottieRawRes = lottieRawRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLottieRawRes() {
        return lottieRawRes;
    }

    public void setLottieRawRes(String lottieRawRes) {
        this.lottieRawRes = lottieRawRes;
    }
}
