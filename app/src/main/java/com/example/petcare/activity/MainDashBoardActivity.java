package com.example.petcare.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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


    @Override
    public void onBackPressed() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameLayoutMainDashBoard); // Replace with your fragment container ID
        if (!(currentFragment instanceof DashBoardFragment)) {
            // Navigate to UserDashboardFragment
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutMainDashBoard, new DashBoardFragment()) // Replace with your UserDashboardFragment instance
                    .addToBackStack(null)
                    .commit();
        } else {
            // Show a dialog to confirm quitting
            new AlertDialog.Builder(this)
                    .setTitle("Exit Application")
                    .setMessage("Do you really want to quit?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finish(); // Close the activity
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss(); // Dismiss the dialog
                    })
                    .show();
        }
    }

}
