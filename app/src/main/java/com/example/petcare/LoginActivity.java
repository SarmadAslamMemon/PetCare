package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.activity.MainDashBoardActivity;
import com.example.petcare.databinding.ActivityLoginBinding;
import com.example.petcare.modelclass.LoginRequest;
import com.example.petcare.modelclass.LoginResponse;
import com.example.petcare.modelclass.User;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.SharePreference;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SharePreference sharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharePreference = new SharePreference(this);

        // Set up login button click listener
        binding.loginButton.setOnClickListener(v -> validateAndLogin());

        // Set up register link click listener
        binding.registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void validateAndLogin() {
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        // Reset errors
        binding.emailLayout.setError(null);
        binding.passwordLayout.setError(null);

        boolean hasError = false;

        // Email validation
        if (TextUtils.isEmpty(email)) {
            binding.emailLayout.setError("Email is required");
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError("Invalid email address");
            hasError = true;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            binding.passwordLayout.setError("Password is required");
            hasError = true;
        } else if (password.length() < 6) {
            binding.passwordLayout.setError("Password must be at least 6 characters");
            hasError = true;
        }

        if (!hasError) {
            // Create login request
            LoginRequest loginRequest = new LoginRequest(email, password);
            callLoginApi(loginRequest);
        }
    }

    private void callLoginApi(LoginRequest loginRequest) {
        binding.loginButton.setEnabled(false);
        binding.loginButton.setText("Logging in...");

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                binding.loginButton.setEnabled(true);
                binding.loginButton.setText("Login");

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    User user = response.body().getData();
                    if (user != null) {
                        // Save user data to shared preferences
                        sharePreference.saveUserRegisteration(user);
                        sharePreference.setUserRegistered(true);
                        // Navigate to main dashboard
                        Snackbar.make(binding.getRoot(), "Login successful", Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainDashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(binding.getRoot(), "Login failed", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(binding.getRoot(), "Invalid credentials", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.loginButton.setEnabled(true);
                binding.loginButton.setText("Login");
                Snackbar.make(binding.getRoot(), "Login failed: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
} 