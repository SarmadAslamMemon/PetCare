package com.example.petcare.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.petcare.GeneralClass;
import com.example.petcare.modelclass.HealthTip;
import com.example.petcare.modelclass.Pet;
import com.example.petcare.modelclass.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SharePreference {

    private static final String PREF_NAME = "PetCarePrefs";
    private static SharePreference instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PET_NAME = "pet_name";
    private static final String PET_IMAGE = "pet_image";
    private static final String USER_PETS_LIST = "user_pets_list";
    private static final String PET_COUNT = "pet_count";

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
        User user = getUserRegisteration();
        if (user != null) {
            return user.getFirstName();
        }
        return "User";
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    // Save image path to SharedPreferences
    public void saveImagePathToSharedPreferences(String absolutePath) {
        editor.putString(PET_IMAGE, absolutePath);
        editor.apply();
    }

    // Retrieve pet profile picture as Bitmap
    public Bitmap getPetProfilePic() {
        String imagePath = sharedPreferences.getString(PET_IMAGE, null);
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            Log.d("SharePreference", "getPetProfilePic: " + imagePath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                if (bitmap != null) {
                    return bitmap; // Return the decoded Bitmap
                }
            }
        }
        return null;
    }

    // Save a single pet
    public void savePet(Pet pet) {
        List<Pet> pets = getUserStoredPets();
        if (pets == null) {
            pets = new ArrayList<>();
            pets.add(pet);
            Log.d("jarvis","Check Pets size before"+pets.size());
            savePets(pets);
        }else{
            pets.add(pet);
            Log.d("jarvis","Check Pets size after"+pets.size());
        }

    }

    // Save list of pets to SharedPreferences
    private void savePets(List<Pet> pets) {
        Gson gson = new Gson();
        String json = gson.toJson(pets);
        editor.putString(USER_PETS_LIST, json); // Store JSON string in SharedPreferences
        editor.apply();
    }

    // Get a pet by its ID
    public Pet getPetById(int petId) {
        List<Pet> pets = getUserStoredPets();
        if (pets != null) {
            for (Pet pet : pets) {
                if (pet.getId()== petId) {
                    return pet;
                }
            }
        }
        return null;
    }

    // Get the list of all stored pets
    private List<Pet> getUserStoredPets() {
        String petsJson = sharedPreferences.getString(USER_PETS_LIST, null);
        if (petsJson != null) {
            Gson gson = new Gson();
            TypeToken<List<Pet>> token = new TypeToken<List<Pet>>() {};
            return gson.fromJson(petsJson, token.getType());
        }
        return new ArrayList<>();
    }

    public int getPetCount(){
        return getInt(PET_COUNT,0);
    }

    public void addPetCount() {
        int count = getInt(PET_COUNT, 0);
        count++;
        editor.putInt(PET_COUNT, count);
        editor.apply();


    }

    public void setUserRegistered(boolean b) {
        editor.putBoolean("userRegistered", b);
        editor.apply();
    }

    public boolean getUserRegistered() {
        return getBoolean("userRegistered", false);
    }

    public void saveUserRegisteration(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.apply();
    }
    public User getUserRegisteration() {
        String json = sharedPreferences.getString("user", null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, User.class);
        }
        return null;


    }

    public int getUserId() {
        User user = getUserRegisteration();
        if (user != null) {
            return user.getId();
        }
        return -1; // or 0 or throw exception depending on your logic
    }

}
