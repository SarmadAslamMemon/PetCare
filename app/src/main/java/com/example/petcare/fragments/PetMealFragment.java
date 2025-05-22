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

public class PetMealFragment  extends Fragment {

    private TextInputEditText eatingTimeEditText1, eatingTimeEditText2, eatingTimeEditText3;
    private TextInputEditText favoriteFoodEditText;
    private SharePreference sharePreference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_meal, container, false);

        MaterialToolbar topAppBar = view.findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        topAppBar.setTitle("Pet Meal");

        // Initialize views
        favoriteFoodEditText = view.findViewById(R.id.favoriteFoodEditText);
        eatingTimeEditText1 = view.findViewById(R.id.eatingTimeEditText);
        eatingTimeEditText2 = view.findViewById(R.id.eatingTimeEditText2);
        eatingTimeEditText3 = view.findViewById(R.id.eatingTimeEditText3);

        // Initialize SharePreference
        sharePreference = new SharePreference(requireContext());

        // Set click listeners for time pickers
        eatingTimeEditText1.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText1));
        eatingTimeEditText2.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText2));
        eatingTimeEditText3.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText3));

        // Set click listener for save button
        view.findViewById(R.id.saveFoodTimeButton).setOnClickListener(v -> savePetFoodTime());

        return view;
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

    private void savePetFoodTime() {
        // Validate at least one meal time is set
        if (eatingTimeEditText1.getText().toString().isEmpty() &&
            eatingTimeEditText2.getText().toString().isEmpty() &&
            eatingTimeEditText3.getText().toString().isEmpty()) {
            Snackbar.make(requireView(), "Please set at least one meal time", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Get FCM token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String fcmToken = task.getResult();
                String deviceId = android.provider.Settings.Secure.getString(
                    requireContext().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID
                );

                // Create PetFoodTime object
                PetFoodTime petFoodTime = new PetFoodTime(
                1,
                    Objects.requireNonNull(favoriteFoodEditText.getText()).toString(),
                    eatingTimeEditText1.getText().toString(),
                    Objects.requireNonNull(eatingTimeEditText2.getText()).toString(),
                    Objects.requireNonNull(eatingTimeEditText3.getText()).toString(),
                    fcmToken,
                    deviceId
                );

                // Show progress dialog
                ProgressDialogUtil.showProgressBar(requireContext(), true);

                // Make API call
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                apiService.setPetFoodTime(petFoodTime).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                        ProgressDialogUtil.showProgressBar(requireContext(), false);
                        if (response.isSuccessful()) {
                            Snackbar.make(requireView(), "Pet meal times saved successfully", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(requireView(), "Failed to save pet meal times", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Void> call, @NonNull Throwable t) {
                        ProgressDialogUtil.showProgressBar(requireContext(), false);
                        Snackbar.make(requireView(), "Network error occurred", Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Snackbar.make(requireView(), "Failed to get FCM token", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
