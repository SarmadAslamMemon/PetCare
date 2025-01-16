package com.example.petcare.modelclass;


public class Pet {

    private int petId;
    private int userId;
    private String petName;
    private String petColor;
    private int petAge;
    private String petCategory;
    private String petImage;
    private String petGender;


    public Pet(int petId, int userId, String petName, String petColor, int petAge, String petCategory, String petImage, String petGender) {
        this.petId = petId;
        this.userId = userId;
        this.petName = petName;
        this.petColor = petColor;
        this.petAge = petAge;
        this.petCategory = petCategory;
        this.petImage = petImage;
        this.petGender = petGender;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }
}
