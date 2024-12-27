package com.example.petcare.modelclass;



public class Doctor {
    private String name;
    private String education;
    private String hospital;
    private String callNumber;

    public Doctor(String name, String education, String hospital, String callNumber) {
        this.name = name;
        this.education = education;
        this.hospital = hospital;
        this.callNumber = callNumber;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEducation() {
        return education;
    }

    public String getHospital() {
        return hospital;
    }

    public String getCallNumber() {
        return callNumber;
    }
}
