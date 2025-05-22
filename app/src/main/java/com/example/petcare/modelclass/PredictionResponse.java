package com.example.petcare.modelclass;

import com.google.gson.annotations.SerializedName;

public class PredictionResponse {
    @SerializedName("species")
    private String species;

    @SerializedName("species_confidence")
    private double speciesConfidence;

    @SerializedName("disease")
    private String disease;

    @SerializedName("disease_confidence")
    private double diseaseConfidence;

    // Getters and setters
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getSpeciesConfidence() {
        return speciesConfidence;
    }

    public void setSpeciesConfidence(double speciesConfidence) {
        this.speciesConfidence = speciesConfidence;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public double getDiseaseConfidence() {
        return diseaseConfidence;
    }

    public void setDiseaseConfidence(double diseaseConfidence) {
        this.diseaseConfidence = diseaseConfidence;
    }
} 