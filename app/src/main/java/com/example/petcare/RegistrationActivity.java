package com.example.petcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText firstNameEditText, lastNameEditText, emailEditText, dobEditText, addressEditText, passwordEditText, confirmPasswordEditText;
    private Button finishButton;
    private TextInputLayout firstNameLayout, lastNameLayout, emailLayout, dobLayout, addressLayout, passwordLayout, confirmPasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize Views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        dobEditText = findViewById(R.id.dobEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        finishButton = findViewById(R.id.finishBtn);

        firstNameLayout = findViewById(R.id.firstNameLayout);
        lastNameLayout = findViewById(R.id.lastNameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        dobLayout = findViewById(R.id.dobLayout);
        addressLayout = findViewById(R.id.addressLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        // Date of Birth DatePicker
        dobEditText.setOnClickListener(view -> openDatePicker());

        // Finish button click listener
//        finishButton.setOnClickListener(view -> validateForm());

        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, PetDetailsActivity.class);
            startActivity(intent);
        });
    }

    // Open Date Picker
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format and set date
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dobEditText.setText(sdf.format(selectedDate.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void validateForm() {
        boolean hasError = false;



        // Clear previous error messages
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        emailLayout.setError(null);
        dobLayout.setError(null);
        addressLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        // First Name Validation
        String firstName = firstNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            firstNameLayout.setError("First Name is required");
            hasError = true;
        } else if (!firstName.matches("[a-zA-Z]+")) {
            firstNameLayout.setError("Invalid First Name");
            hasError = true;
        } else if (firstName.length() < 3) {
            firstNameLayout.setError("First Name must be at least 3 characters");
            hasError = true;
        } else if (firstName.length() > 20) {
            firstNameLayout.setError("First Name cannot exceed 20 characters");
            hasError = true;
        }

        // Last Name Validation
        String lastName = lastNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(lastName)) {
            lastNameLayout.setError("Last Name is required");
            hasError = true;
        } else if (!lastName.matches("[a-zA-Z]+")) {
            lastNameLayout.setError("Invalid Last Name");
            hasError = true;
        } else if (lastName.length() < 3) {
            lastNameLayout.setError("Last Name must be at least 3 characters");
            hasError = true;
        } else if (lastName.length() > 20) {
            lastNameLayout.setError("Last Name cannot exceed 20 characters");
            hasError = true;
        }

        // Email Validation
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email Address is required");
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email address");
            hasError = true;
        }

        // Date of Birth Validation
        String dob = dobEditText.getText().toString().trim();
        if (TextUtils.isEmpty(dob)) {
            dobLayout.setError("Date of Birth is required");
            hasError = true;
        }

        // Address Validation
        String address = addressEditText.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            addressLayout.setError("Address is required");
            hasError = true;
        }

        // Password Validation
        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            hasError = true;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            hasError = true;
        }

        // Confirm Password Validation
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordLayout.setError("Confirm Password is required");
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            hasError = true;
        }

        // If there's any error, show a Snackbar message
        if (hasError) {
            Snackbar.make(finishButton, "Please fix the errors above", Snackbar.LENGTH_LONG).show();
        } else {
            // If no error, show a success Snackbar
            Snackbar.make(finishButton, "Registration Successful!", Snackbar.LENGTH_SHORT).show();
        }
    }

}
