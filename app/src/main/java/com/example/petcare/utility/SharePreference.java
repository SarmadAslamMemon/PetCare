package com.example.petcare.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.petcare.GeneralClass;
import com.example.petcare.modelclass.HealthTip;

import java.util.List;
import java.util.Random;

public class SharePreference {

    private static final String PREF_NAME = "PetCarePrefs";
    private static SharePreference instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Private constructor to prevent instantiation
    private SharePreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharePreference getInstance(Context context) {
        if (instance == null) {
            synchronized (SharePreference.class) {
                if (instance == null) {
                    instance = new SharePreference(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // Method to save a String value in SharedPreferences
    public void savePetDetails(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public HealthTip getHealthTip() {
        List<HealthTip> healthTips = GeneralClass.getHealthTips();
        Random random = new Random();
        int randomIndex = random.nextInt(healthTips.size());
        return healthTips.get(randomIndex);
    }


    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    // Method to retrieve an integer value from SharedPreferences
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Method to save a boolean value in SharedPreferences
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Method to retrieve a boolean value from SharedPreferences
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Method to remove a value from SharedPreferences
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Method to clear all values in SharedPreferences
    public void clear() {
        editor.clear();
        editor.apply();
    }
}
