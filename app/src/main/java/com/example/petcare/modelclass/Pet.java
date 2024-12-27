package com.example.petcare.modelclass;


public class Pet {

    private String petId;
    private String petName;
    private String petColor;
    private int petAge;
    private String petCategory;

    // Constructor
    public Pet(String petId, String petName, String petColor, int petAge, String petCategory) {
        this.petId = petId;
        this.petName = petName;
        this.petColor = petColor;
        this.petAge = petAge;
        this.petCategory = petCategory;
    }

    // Getters and Setters
    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetColor() {
        return petColor;
    }

    public void setPetColor(String petColor) {
        this.petColor = petColor;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }
}
