package com.example.petcare.fragments;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;


import java.util.Calendar;

public class PetMealFragment extends Fragment {

    private TextInputEditText eatingTimeEditText1, eatingTimeEditText2, eatingTimeEditText3;

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
//        ImageView backButton = view.findViewById(R.id.backButtonIcPetMeals);
//        ImageView petMealTitleImage = view.findViewById(R.id.petMealTitleImage);
//        TextView catNameText = view.findViewById(R.id.catNameText);
//        TextInputEditText favoriteFoodEditText = view.findViewById(R.id.favoriteFoodEditText);
        eatingTimeEditText1 = view.findViewById(R.id.eatingTimeEditText);
        eatingTimeEditText2 = view.findViewById(R.id.eatingTimeEditText2);
        eatingTimeEditText3 = view.findViewById(R.id.eatingTimeEditText3);

//        backButton.setOnClickListener(v -> {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragmentContainerPetMeal, new DashBoardFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
//        });


        eatingTimeEditText1.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText1));
        eatingTimeEditText2.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText2));
        eatingTimeEditText3.setOnClickListener(v -> showTimePickerDialog(eatingTimeEditText3));

        return view;
    }

    private void showTimePickerDialog(TextInputEditText timeField) {
        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    // Format and set selected time in the field
                    String formattedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    timeField.setText(formattedTime);
                },
                hour,
                minute,
                true // Use 24-hour format
        );
        timePickerDialog.show();
    }
}
