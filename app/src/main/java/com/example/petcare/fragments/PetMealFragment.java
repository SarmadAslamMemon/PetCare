package com.example.petcare.fragments;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.petcare.R;
import com.example.petcare.modelclass.PetFoodTime;
import com.example.petcare.modelclass.PetFoodTimeResponse;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.ProgressDialogUtil;
import com.example.petcare.utility.SharePreference;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Objects;
import com.example.petcare.modelclass.Pet;
import android.widget.LinearLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.button.MaterialButton;
import android.text.InputType;

public class PetMealFragment  extends Fragment {

    private TextInputEditText eatingTimeEditText1, eatingTimeEditText2, eatingTimeEditText3;
    private TextInputEditText favoriteFoodEditText;
    private SharePreference sharePreference;
    private Pet selectedPet;
    private MaterialAutoCompleteTextView petDropdown;
    private List<Pet> petList;
    private ProgressDialogUtil progressDialogUtil;
    private int currentFoodId = 0; // Track current food ID for update/create logic

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_meal, container, false);

        MaterialToolbar topAppBar = view.findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        topAppBar.setTitle("Pet Meal");

        // Initialize SharePreference
        sharePreference = new SharePreference(requireContext());
        petList = new ArrayList<>();
        
        // Load pets from API instead of SharedPreferences
        loadPetsFromAPI();

        // Setup pet dropdown
        petDropdown = view.findViewById(R.id.petDropdown);



        // Initialize views
        favoriteFoodEditText = view.findViewById(R.id.favoriteFoodEditText);
        eatingTimeEditText1 = view.findViewById(R.id.eatingTimeEditText);
        eatingTimeEditText2 = view.findViewById(R.id.eatingTimeEditText2);
        eatingTimeEditText3 = view.findViewById(R.id.eatingTimeEditText3);

        // Set click listeners for time pickers
        eatingTimeEditText1.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText1));
        eatingTimeEditText2.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText2));
        eatingTimeEditText3.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText3));

        // Set click listener for save button
        view.findViewById(R.id.saveFoodTimeButton).setOnClickListener(v -> savePetFoodTime());

        // Set initial button text
        MaterialButton saveButton = view.findViewById(R.id.saveFoodTimeButton);
        saveButton.setText("Create Food Time");

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Reset progress dialog to prevent memory leaks
        ProgressDialogUtil.resetDialog();
    }

    private void loadPetsFromAPI() {
        int userId = sharePreference.getUserId();
        if (userId == -1) {
            // Handle case where user is not logged in
            setupPetDropdown();
            return;
        }

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Pet>> call = apiService.getPetsByUserId(userId);

        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pet>> call, @NonNull Response<List<Pet>> response) {
                // Check if fragment is still attached to activity before updating UI
                if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                    return;
                }
                
                if (response.isSuccessful() && response.body() != null) {
                    petList = response.body();
                } else {
                    petList = new ArrayList<>();
                }
                setupPetDropdown();
            }

            @Override
            public void onFailure(@NonNull Call<List<Pet>> call, @NonNull Throwable t) {
                // Check if fragment is still attached to activity before updating UI
                if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                    return;
                }
                
                petList = new ArrayList<>();
                setupPetDropdown();
            }
        });
    }

    private void setupPetDropdown() {
        // Check if fragment is still attached to activity before updating UI
        if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        
        List<String> petNames = new ArrayList<>();
        for (Pet pet : petList) {
            petNames.add(pet.getPetName() + " (" + pet.getPetCategory() + ")");
        }
        
        Log.d("PetMealFragment", "Pet list size: " + petList.get(0).getPetName());
        if (!petList.isEmpty()) {
            petDropdown.setEnabled(true);
            petDropdown.setText(""); // Clear any previous text like 'No pets available'
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, petNames);
            petDropdown.setAdapter(adapter);
            
            petDropdown.setOnClickListener(v -> petDropdown.showDropDown());
            petDropdown.setInputType(InputType.TYPE_NULL);
            petDropdown.setKeyListener(null);

            petDropdown.setOnItemClickListener((parent, v, position, id) -> {
                if (position >= 0 && position < petList.size()) {
                    selectedPet = petList.get(position);
                    fetchAndPrefillMealTimes(selectedPet.getId());
                }
            });
            
            // If coming from PetDetailsActivity with a selected pet, preselect it
            if (getArguments() != null && getArguments().containsKey("pet")) {
                selectedPet = (Pet) getArguments().getSerializable("pet");
                if (selectedPet != null) {
                    int idx = 0;
                    for (int i = 0; i < petList.size(); i++) {
                        if (petList.get(i).getId() == selectedPet.getId()) {
                            idx = i;
                            break;
                        }
                    }
                    // Check if idx is within bounds before accessing petNames
                    if (idx >= 0 && idx < petNames.size()) {
                        petDropdown.setText(petNames.get(idx), false);
                        fetchAndPrefillMealTimes(selectedPet.getId());
                    }
                }
            }
        } else {
            // Show message if no pets are available
            petDropdown.setEnabled(false);
            petDropdown.setText("No pets available", false);
        }
    }

    private void showTimePickerDialog(TextInputEditText timeField) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    String formattedTime = String.format("%02d:%02d:00", hourOfDay, minuteOfHour);
                    timeField.setText(formattedTime);
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void fetchAndPrefillMealTimes(int petId) {
        Log.d("PetMealFragment", "fetchAndPrefillMealTimes called with petId: " + petId);
        
        // Check if fragment is still attached to activity
        if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            Log.w("PetMealFragment", "Fragment not attached, skipping API call");
            return;
        }
        
        ProgressDialogUtil.showProgressBar(requireContext(), true);
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Log.d("PetMealFragment", "Making API call to getPetFoodTimes for petId: " + petId);
        apiService.getPetFoodTimes(petId).enqueue(new retrofit2.Callback<PetFoodTimeResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<PetFoodTimeResponse> call, @NonNull retrofit2.Response<PetFoodTimeResponse> response) {
                Log.d("PetMealFragment", "API Response received - Code: " + response.code());
                Log.d("PetMealFragment", "Response body: " + response.body());
                
                // Check if fragment is still attached to activity before updating UI
                if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                    Log.w("PetMealFragment", "Fragment not attached, skipping UI update");
                    return;
                }
                
                ProgressDialogUtil.showProgressBar(requireContext(), false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    PetFoodTimeResponse foodTimeResponse = response.body();
                    List<PetFoodTime> foodTimes = foodTimeResponse.getData();
                    
                    Log.d("PetMealFragment", "API Success: " + foodTimeResponse.isSuccess());
                    Log.d("PetMealFragment", "API Message: " + foodTimeResponse.getMessage());
                    Log.d("PetMealFragment", "Number of food times received: " + (foodTimes != null ? foodTimes.size() : 0));
                    
                    if (foodTimes != null && !foodTimes.isEmpty()) {
                        // Find the PetFoodTime with the highest id (latest)
                        PetFoodTime latestFoodTime = foodTimes.get(0);
                        for (PetFoodTime ft : foodTimes) {
                            if (ft.getFoodId() > latestFoodTime.getFoodId()) {
                                latestFoodTime = ft;
                            }
                        }
                        currentFoodId = latestFoodTime.getFoodId(); // Store the food ID for update
                        
                        // Save to SharedPreferences for offline access
                        sharePreference.savePetFoodTime(petId, latestFoodTime);
                        
                        favoriteFoodEditText.setText(latestFoodTime.getFavoriteFood());
                        eatingTimeEditText1.setText(latestFoodTime.getEatingTime1());
                        eatingTimeEditText2.setText(latestFoodTime.getEatingTime2());
                        eatingTimeEditText3.setText(latestFoodTime.getEatingTime3());
                        
                        // Update button text to show "Update"
                        updateSaveButtonText();
                        
                        Log.d("PetMealFragment", "Successfully populated form fields with food ID: " + currentFoodId);
                    } else {
                        Log.w("PetMealFragment", "No food times in response data - creating new food time");
                        currentFoodId = 0; // Set to 0 for new food time
                        clearFormFields();
                        
                        // Check if we have cached data in SharedPreferences
                        PetFoodTime cachedFoodTime = sharePreference.getPetFoodTime(petId);
                        if (cachedFoodTime != null) {
                            Log.d("PetMealFragment", "Loading cached food time data");
                            currentFoodId = cachedFoodTime.getFoodId();
                            favoriteFoodEditText.setText(cachedFoodTime.getFavoriteFood());
                            eatingTimeEditText1.setText(cachedFoodTime.getEatingTime1());
                            eatingTimeEditText2.setText(cachedFoodTime.getEatingTime2());
                            eatingTimeEditText3.setText(cachedFoodTime.getEatingTime3());
                        }
                        
                        // Update button text to show "Create"
                        updateSaveButtonText();
                    }
                } else {
                    Log.w("PetMealFragment", "API call not successful or response invalid");
                    Log.w("PetMealFragment", "Response successful: " + response.isSuccessful());
                    Log.w("PetMealFragment", "Response body null: " + (response.body() == null));
                    if (response.body() != null) {
                        Log.w("PetMealFragment", "API Success: " + response.body().isSuccess());
                        Log.w("PetMealFragment", "API Message: " + response.body().getMessage());
                    }
                    
                    currentFoodId = 0; // Set to 0 for new food time
                    clearFormFields();
                    
                    // Try to load from SharedPreferences as fallback
                    PetFoodTime cachedFoodTime = sharePreference.getPetFoodTime(petId);
                    if (cachedFoodTime != null) {
                        Log.d("PetMealFragment", "Loading cached food time data after API failure");
                        currentFoodId = cachedFoodTime.getFoodId();
                        favoriteFoodEditText.setText(cachedFoodTime.getFavoriteFood());
                        eatingTimeEditText1.setText(cachedFoodTime.getEatingTime1());
                        eatingTimeEditText2.setText(cachedFoodTime.getEatingTime2());
                        eatingTimeEditText3.setText(cachedFoodTime.getEatingTime3());
                    }
                    
                    // Update button text
                    updateSaveButtonText();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<PetFoodTimeResponse> call, @NonNull Throwable t) {
                Log.e("PetMealFragment", "API call failed: " + t.getMessage());
                Log.e("PetMealFragment", "Error details: ", t);
                
                // Check if fragment is still attached to activity before updating UI
                if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                    Log.w("PetMealFragment", "Fragment not attached, skipping error handling");
                    return;
                }
                
                ProgressDialogUtil.showProgressBar(requireContext(), false);
                
                // Try to load from SharedPreferences as fallback
                PetFoodTime cachedFoodTime = sharePreference.getPetFoodTime(petId);
                if (cachedFoodTime != null) {
                    Log.d("PetMealFragment", "Loading cached food time data after API failure");
                    currentFoodId = cachedFoodTime.getFoodId();
                    favoriteFoodEditText.setText(cachedFoodTime.getFavoriteFood());
                    eatingTimeEditText1.setText(cachedFoodTime.getEatingTime1());
                    eatingTimeEditText2.setText(cachedFoodTime.getEatingTime2());
                    eatingTimeEditText3.setText(cachedFoodTime.getEatingTime3());
                } else {
                    currentFoodId = 0; // Set to 0 for new food time
                    clearFormFields();
                }
                
                // Update button text
                updateSaveButtonText();
            }
        });
    }

    private void clearFormFields() {
        favoriteFoodEditText.setText("");
        eatingTimeEditText1.setText("");
        eatingTimeEditText2.setText("");
        eatingTimeEditText3.setText("");
    }

    private void savePetFoodTime() {
        Log.d("PetMealFragment", "savePetFoodTime called with foodId: " + currentFoodId);
        
        if (selectedPet == null) {
            Log.w("PetMealFragment", "No pet selected");
            Snackbar.make(requireView(), "Please select a pet", Snackbar.LENGTH_SHORT).show();
            return;
        }
        
        Log.d("PetMealFragment", "Selected pet - ID: " + selectedPet.getId() + ", Name: " + selectedPet.getPetName());
        
        // Additional check to ensure selectedPet has a valid ID
        if (selectedPet.getId() <= 0) {
            Log.w("PetMealFragment", "Invalid pet ID: " + selectedPet.getId());
            Snackbar.make(requireView(), "Invalid pet selected", Snackbar.LENGTH_SHORT).show();
            return;
        }
        // Validate at least one meal time is set
        String time1 = eatingTimeEditText1.getText().toString();
        String time2 = eatingTimeEditText2.getText().toString();
        String time3 = eatingTimeEditText3.getText().toString();
        String favoriteFood = Objects.requireNonNull(favoriteFoodEditText.getText()).toString();
        
        Log.d("PetMealFragment", "Form data - Time1: " + time1 + ", Time2: " + time2 + ", Time3: " + time3 + ", Food: " + favoriteFood);
        
        if (time1.isEmpty() && time2.isEmpty() && time3.isEmpty()) {
            Log.w("PetMealFragment", "No meal times provided");
            Snackbar.make(requireView(), "Please set at least one meal time", Snackbar.LENGTH_SHORT).show();
            return;
        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String fcmToken = task.getResult();
                String deviceId = android.provider.Settings.Secure.getString(
                    requireContext().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID
                );

                PetFoodTime petFoodTime = new PetFoodTime(
                    currentFoodId, // Use the current food ID (0 for new, > 0 for update)
                    selectedPet.getId(),
                    Objects.requireNonNull(favoriteFoodEditText.getText()).toString(),
                    eatingTimeEditText1.getText().toString(),
                    Objects.requireNonNull(eatingTimeEditText2.getText()).toString(),
                    Objects.requireNonNull(eatingTimeEditText3.getText()).toString(),
                    fcmToken,
                    deviceId
                );

                Log.d("PetMealFragment", "Saving pet food time:");
                Log.d("PetMealFragment", "Food ID: " + petFoodTime.getFoodId() + " (0=new, >0=update)");
                Log.d("PetMealFragment", "Pet ID: " + petFoodTime.getPetId());
                Log.d("PetMealFragment", "Favorite Food: " + petFoodTime.getFavoriteFood());
                Log.d("PetMealFragment", "Eating Time 1: " + petFoodTime.getEatingTime1());
                Log.d("PetMealFragment", "Eating Time 2: " + petFoodTime.getEatingTime2());
                Log.d("PetMealFragment", "Eating Time 3: " + petFoodTime.getEatingTime3());
                Log.d("PetMealFragment", "FCM Token: " + petFoodTime.getFcmToken());
                Log.d("PetMealFragment", "Device ID: " + petFoodTime.getDeviceId());

                ProgressDialogUtil.showProgressBar(requireContext(), true);
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                apiService.setPetFoodTime(petFoodTime).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                        Log.d("PetMealFragment", "Save API Response - Code: " + response.code());
                        Log.d("PetMealFragment", "Save API Response - Success: " + response.isSuccessful());
                        
                        // Check if fragment is still attached to activity before updating UI
                        if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                            Log.w("PetMealFragment", "Fragment not attached, skipping save response");
                            return;
                        }
                        
                        ProgressDialogUtil.showProgressBar(requireContext(), false);
                        if (response.isSuccessful()) {
                            // Save to SharedPreferences for offline access
                            sharePreference.savePetFoodTime(selectedPet.getId(), petFoodTime);
                            
                            String message = currentFoodId == 0 ? "Pet meal times created successfully" : "Pet meal times updated successfully";
                            Log.d("PetMealFragment", message);
                            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
                            
                            // Update current food ID if this was a new creation
                            if (currentFoodId == 0) {
                                // For new food time, we might need to fetch the updated data to get the new food ID
                                // For now, we'll keep it as 0 and let the next fetch update it
                                Log.d("PetMealFragment", "New food time created, will update food ID on next fetch");
                            }
                        } else {
                            Log.w("PetMealFragment", "Failed to save pet meal times - Response code: " + response.code());
                            String errorMessage = currentFoodId == 0 ? "Failed to create pet meal times" : "Failed to update pet meal times";
                            Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Void> call, @NonNull Throwable t) {
                        Log.e("PetMealFragment", "Save API call failed: " + t.getMessage());
                        Log.e("PetMealFragment", "Error details: ", t);
                        
                        // Check if fragment is still attached to activity before updating UI
                        if (!isAdded() || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                            Log.w("PetMealFragment", "Fragment not attached, skipping error handling");
                            return;
                        }
                        
                        ProgressDialogUtil.showProgressBar(requireContext(), false);
                        String errorMessage = currentFoodId == 0 ? "Failed to create pet meal times" : "Failed to update pet meal times";
                        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("PetMealFragment", "Failed to get FCM token: " + task.getException());
                Snackbar.make(requireView(), "Failed to get device token", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSaveButtonText() {
        if (getView() != null) {
            MaterialButton saveButton = getView().findViewById(R.id.saveFoodTimeButton);
            if (saveButton != null) {
                String buttonText = currentFoodId == 0 ? "Create Food Time" : "Update Food Time";
                saveButton.setText(buttonText);
            }
        }
    }
}
