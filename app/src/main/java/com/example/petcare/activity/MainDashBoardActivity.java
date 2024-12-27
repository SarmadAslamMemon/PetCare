package com.example.petcare.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.petcare.R;
import com.example.petcare.fragments.CommunityFragment;
import com.example.petcare.fragments.ConsultationFragment;
import com.example.petcare.fragments.DashBoardFragment;
import com.example.petcare.fragments.PersonProfileFragment;
import com.example.petcare.fragments.PetProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainDashBoardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dash_board);

        getViews();

        fragmentManager = getSupportFragmentManager();
        loadFragment(new DashBoardFragment()); // Set the default fragment

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.homeMenu) {
                selectedFragment = new DashBoardFragment();
            } else if (item.getItemId() == R.id.personprofileMenu) {
                selectedFragment = new PersonProfileFragment();
            } else if (item.getItemId() == R.id.communityMenu) { // Changed to correct menu ID
                selectedFragment = new ConsultationFragment();
            } else if (item.getItemId() == R.id.petprofileMenu) {
                selectedFragment = new PetProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true;
        });

    }

    private void getViews() {
        bottomNavigationView = findViewById(R.id.bottomnavigation);
    }

    private void loadFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayoutMainDashBoard, fragment)
                .commit();
    }
}
