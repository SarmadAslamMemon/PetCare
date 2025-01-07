package com.example.petcare.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.petcare.GeneralClass;
import com.example.petcare.modelclass.HealthTip;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Random;

public class SharePreference {

    private static final String PREF_NAME = "PetCarePrefs";
    private static SharePreference instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PET_NAME = "pet_name";
    private static final String PET_IMAGE = "pet_image";

    // Private constructor to prevent instantiation
    public SharePreference(Context context) {
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

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    public void saveUserPetName(String petName) {
        editor.putString(PET_NAME, petName);
        editor.apply();

    }
    public String getUserPetName() {
        return sharedPreferences.getString(PET_NAME, null);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    // Save a Bitmap image as a Base64 encoded string
    public void savePetProfilePic(Bitmap bitmap) {
        if (bitmap != null) {
            String bitmapString = bitmapToString(bitmap);
            editor.putString(PET_IMAGE, bitmapString);
            editor.apply();
        } else {
            editor.remove(PET_IMAGE);
            editor.apply();
        }
    }

    // Retrieve a Bitmap image from the Base64 encoded string
    public Bitmap getPetProfilePic() {
        String bitmapString = sharedPreferences.getString(PET_IMAGE, null);
        if (bitmapString != null) {
            return stringToBitmap(bitmapString);
        }
        return null;
    }

    // Utility method to convert Bitmap to Base64 encoded string
    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Utility method to convert Base64 encoded string to Bitmap
    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
