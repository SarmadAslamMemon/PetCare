package com.example.petcare;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.petcare.fragments.PetMealFragment;
import com.example.petcare.modelclass.Pet;

public class PetMealActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_meal);

        if (savedInstanceState == null) {
            PetMealFragment fragment = new PetMealFragment();
            Bundle args = new Bundle();
            if (getIntent() != null && getIntent().hasExtra("pet")) {
                args.putSerializable("pet", getIntent().getSerializableExtra("pet"));
            }
            fragment.setArguments(args);
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.pet_meal_fragment_container, fragment)
                .commit();
        }
    }
} 