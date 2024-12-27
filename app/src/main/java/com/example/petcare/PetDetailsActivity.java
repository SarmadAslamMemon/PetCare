package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.activity.MainDashBoardActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PetDetailsActivity extends AppCompatActivity {

    private TextInputEditText petNameEditText, petColorEditText, petAgeEditText;
    private AutoCompleteTextView petTypeAutoCompleteTextView;
    private TextInputLayout petNameLayout, petColorLayout, petAgeLayout, petTypeLayout;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details); // Replace with your actual layout name

        // Initialize views
        petNameEditText = findViewById(R.id.petNameEditText);
        petColorEditText = findViewById(R.id.petColorEditText);
        petAgeEditText = findViewById(R.id.petAgeEditText);
        petTypeAutoCompleteTextView = findViewById(R.id.petTypeAutoCompleteTextView);
        petNameLayout = findViewById(R.id.petNameLayout);
        petColorLayout = findViewById(R.id.petColorLayout);
        petAgeLayout = findViewById(R.id.petAgeLayout);
        petTypeLayout = findViewById(R.id.petTypeLayout);
        nextButton = findViewById(R.id.nextButton);

        // Initialize dropdown data
        setupDropdownMenu();

        // Set button click listener
        nextButton.setOnClickListener(v -> validateInputFields());
    }

    private void setupDropdownMenu() {
        // Static data from GeneralClass
        String[] petTypes = GeneralClass.getPetTypes();

        // Set adapter for AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, petTypes);
        petTypeAutoCompleteTextView.setAdapter(adapter);

        // Set dropdown behavior
        petTypeAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedType = (String) parent.getItemAtPosition(position);
            Toast.makeText(this, "Selected: " + selectedType, Toast.LENGTH_SHORT).show();
        });
    }

    private void validateInputFields() {
        // Example input fields
        TextInputLayout petNameLayout = findViewById(R.id.petNameLayout);
        TextInputEditText petNameEditText = findViewById(R.id.petNameEditText);

        TextInputLayout petColorLayout = findViewById(R.id.petColorLayout);
        TextInputEditText petColorEditText = findViewById(R.id.petColorEditText);

        boolean isValid = true;

        // Validate Pet Name
        if (petNameEditText.getText().toString().trim().isEmpty()) {
            petNameLayout.setError("Pet name cannot be empty");
            isValid = false;
        } else {
            petNameLayout.setError(null); // Clear error
            petNameLayout.setErrorEnabled(false); // Remove space
        }

        // Validate Pet Color
        if (petColorEditText.getText().toString().trim().isEmpty()) {
            petColorLayout.setError("Pet color cannot be empty");
            isValid = false;
        } else {
            petColorLayout.setError(null); // Clear error
            petColorLayout.setErrorEnabled(false); // Remove space
        }


        if (isValid){
            petNameLayout.setError(null);
            petColorLayout.setError(null);
            petAgeLayout.setError(null);
            petTypeLayout.setError(null);

            Intent intent = new Intent(PetDetailsActivity.this, MainDashBoardActivity.class);
            startActivity(intent);



        }
    }

}