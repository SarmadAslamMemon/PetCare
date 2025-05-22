package com.example.petcare.modelclass;

import com.google.gson.annotations.SerializedName;

public class TextInput {
    @SerializedName("text")
    private String text;

    public TextInput(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
} 