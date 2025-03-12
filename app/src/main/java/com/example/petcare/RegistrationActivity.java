package com.example.petcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.activity.MainDashBoardActivity;
import com.example.petcare.modelclass.User;
import com.example.petcare.modelclass.UserRegisterRequest;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.SharePreference;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText firstNameEditText, lastNameEditText, emailEditText, dobEditText, addressEditText, passwordEditText, confirmPasswordEditText;
    private Button finishButton;
    private TextInputLayout firstNameLayout, lastNameLayout, emailLayout, dobLayout, addressLayout, passwordLayout, confirmPasswordLayout;

    SharePreference sharePreference;
    boolean hasError=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        sharePreference = new SharePreference(this);

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
        finishButton.setOnClickListener(v -> {
            validateForm();
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

    private boolean validateForm() {


        String firstName, lastName, email, dob, address, password, confirmPassword;
        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        email= emailEditText.getText().toString();
        address =addressEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();
        dob = dobEditText.getText().toString();
        UserRegisterRequest userRegisterRequest= new UserRegisterRequest(firstName,lastName,address,"","",email,password);


        if(TextUtils.isEmpty(firstName)){
            firstNameLayout.setError("First Name is required");
            hasError = true;
        } else if(TextUtils.isEmpty(lastName)){
            firstNameLayout.setError(null);
            lastNameLayout.setError("Last Name is required");
            hasError = true;
        } else if (TextUtils.isEmpty(email)) {
            lastNameLayout.setError(null);
            emailLayout.setError("Email is required");
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email address");
            hasError = true;
        }else if (TextUtils.isEmpty(dob)) {
            emailLayout.setError(null);
            dobLayout.setError("Date of Birth is required");
            hasError = true;
        } else if (TextUtils.isEmpty(address)) {
            dobLayout.setError(null);
            addressLayout.setError("Address is required");
            hasError = true;
        } else if (TextUtils.isEmpty(password)) {
            addressLayout.setError(null);
            passwordLayout.setError("Password is required");
            hasError = true;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            passwordLayout.setError(null);
            confirmPasswordLayout.setError("Confirm Password is required");
            hasError = true;
        } else if (password.length() < 6){
            confirmPasswordLayout.setError("Password must be at least 6 characters long");
            hasError = true;
        } else if (!password.equals(confirmPassword)){
            confirmPasswordLayout.setError("Passwords do not match");
            hasError = true;
        } else{
            firstNameLayout.setError(null);
            lastNameLayout.setError(null);
            emailLayout.setError(null);
            dobLayout.setError(null);
            addressLayout.setError(null);
            passwordLayout.setError(null);
            confirmPasswordLayout.setError(null);
            hasError = false;

            callRegistrationApi(userRegisterRequest);

        }

        return hasError;
    }

    private void callRegistrationApi(UserRegisterRequest userRegisterRequest) {
        Log.w("API_RESPONSE", "Registration API Called "+userRegisterRequest);
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.registerUser(userRegisterRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(RegistrationActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    sharePreference.setUserRegistered(true);
                    Snackbar.make(findViewById(android.R.id.content), "Registration Successful", Snackbar.LENGTH_SHORT).show();

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Registration Failed", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), "Registration Failed", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
