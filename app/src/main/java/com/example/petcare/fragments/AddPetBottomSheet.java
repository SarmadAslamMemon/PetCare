package com.example.petcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petcare.R;
import com.example.petcare.modelclass.Pet;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.SharePreference;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPetBottomSheet extends BottomSheetDialogFragment {
    
    public interface OnPetAddedListener {
        void onPetAdded();
    }
    
    private EditText petNameEditText, petAgeEditText, petBreedEditText;
    private Button addPetButton;
    AutoCompleteTextView petCategoryEditText;
    private SharePreference sharePreference;
    private List<String> petCategory=new ArrayList<>();
    public OnPetAddedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pet_sheet, container, false);
        
        // Adjust for keyboard
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        sharePreference = new SharePreference(requireContext());

        // Initialize views
        petNameEditText = view.findViewById(R.id.petNameEditText);
        petCategoryEditText = view.findViewById(R.id.petCategoryDropdown);
        petAgeEditText = view.findViewById(R.id.petAgeEditText);
        petBreedEditText = view.findViewById(R.id.petBreedEditText);
        addPetButton = view.findViewById(R.id.addPetButton);
        petCategory.add("Dog");
        petCategory.add("Cat");
        petCategory.add("Parrot");
        petCategory.add("Fish");
        petCategory.add("Rabbit");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, petCategory);
        petCategoryEditText.setAdapter(adapter);

        addPetButton.setOnClickListener(v -> addPet());

        return view;
    }

    private void addPet() {
        String petName = petNameEditText.getText().toString();
        String petCategory = petCategoryEditText.getText().toString();
        String petAge = petAgeEditText.getText().toString();
        String petBreed = petBreedEditText.getText().toString();

        if (validateFields(petName, petCategory, petAge, petBreed)) {
            Pet pet = new Pet();
            pet.setUserId(sharePreference.getUserId());
            pet.setPetName(petName);
            pet.setPetCategory(petCategory);
            pet.setPetAge(petAge);
            pet.setPetBreed(petBreed);

            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            Call<ResponseBody> call = apiService.addPet(pet);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseString = response.body().string(); // Convert to string
                            JSONObject jsonResponse = new JSONObject(responseString); // Parse JSON
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                dismiss();
                                // Notify parent to refresh pet list
                                if (listener != null) {
                                    listener.onPetAdded();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private boolean validateFields(String petName, String petCategory, String petAge, String petBreed) {
        if (petName.isEmpty()) {
            petNameEditText.setError("Please enter pet name");
            return false;
        }
        if (petCategory.isEmpty()) {
            petCategoryEditText.setError("Please enter pet category");
            return false;
        }
        if (petAge.isEmpty()) {
            petAgeEditText.setError("Please enter pet age");
            return false;
        }
        if (petBreed.isEmpty()) {
            petBreedEditText.setError("Please enter pet breed");
            return false;
        }
        return true;
    }
} 