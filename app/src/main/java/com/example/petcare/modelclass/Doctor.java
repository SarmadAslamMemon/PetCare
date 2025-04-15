package com.example.petcare.modelclass;


import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class Doctor {
    private String name;
    private String education;
    private String hospital;
    private String callNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Doctor(String name, String education, String hospital, String callNumber, int imageResId, String about) {
        this.name = name;
        this.education = education;
        this.hospital = hospital;
        this.callNumber = callNumber;
        this.imageResId = imageResId;
        this.about = about;
    }

    private int imageResId;
    private String about;


    public void callNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("+923042064865"));
        view.getContext().startActivity(intent);
    }

}
