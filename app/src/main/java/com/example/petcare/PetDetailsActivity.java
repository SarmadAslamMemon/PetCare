package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.PetAdapter;
import com.example.petcare.fragments.AddPetBottomSheet;
import com.example.petcare.fragments.PetDetailsBottomSheet;
import com.example.petcare.fragments.PetMealFragment;
import com.example.petcare.modelclass.Pet;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.SharePreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetDetailsActivity extends AppCompatActivity implements PetAdapter.OnPetClickListener, AddPetBottomSheet.OnPetAddedListener {
    private RecyclerView recyclerView;
    private PetAdapter adapter;
    private FloatingActionButton addPetButton;
    private SharePreference sharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        EdgeToEdge.enable(this);
        sharePreference = new SharePreference(this);


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        );
        initViews();
        setupToolbar();
        setupRecyclerView();
        loadPets();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        addPetButton = findViewById(R.id.addPetButton);

        addPetButton.setOnClickListener(v -> {
            AddPetBottomSheet bottomSheet = new AddPetBottomSheet();
            // Set the listener to refresh pets when a new pet is added
            bottomSheet.listener = this;
            bottomSheet.show(getSupportFragmentManager(), "AddPetBottomSheet");
        });
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new PetAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadPets() {
        // Get user ID from SharedPreferences
        int userId = sharePreference.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make API call to get pets
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Pet>> call = apiService.getPetsByUserId(userId);

        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pet>> call, @NonNull Response<List<Pet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updatePets(response.body());
                } else {
                    Toast.makeText(PetDetailsActivity.this, "Failed to load pets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Pet>> call, @NonNull Throwable t) {
                Toast.makeText(PetDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPetClick(Pet pet) {
        PetDetailsBottomSheet bottomSheet = PetDetailsBottomSheet.newInstance(pet);
        bottomSheet.show(getSupportFragmentManager(), "PetDetailsBottomSheet");
    }

    @Override
    public void onEditMealTimeClick(Pet pet) {
        Intent intent = new Intent(this, PetMealActivity.class);
        intent.putExtra("pet", pet);
        startActivity(intent);
    }

    @Override
    public void onPetAdded() {
        // Refresh the pet list when a new pet is added
        loadPets();
    }
}