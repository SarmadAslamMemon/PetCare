package com.example.petcare.modelclass;

import com.google.gson.annotations.SerializedName;

public class PredictionResponse {
    @SerializedName("disease")
    private String disease;

    @SerializedName("accuracy")
    private double accuracy;

    // Getters and setters
    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    // For backward compatibility, keep the old method names
    public double getDiseaseConfidence() {
        return accuracy;
    }

    public void setDiseaseConfidence(double diseaseConfidence) {
        this.accuracy = diseaseConfidence;
    }
} 