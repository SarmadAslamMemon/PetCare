package com.example.petcare.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.petcare.PetDetailsActivity;
import com.example.petcare.R;
import com.example.petcare.fragments.DiseaseDiagnosisBottomSheet;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainDashBoardActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private NavController navController;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private DiseaseDiagnosisBottomSheet diagnosisBottomSheet;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showDiagnosisBottomSheetWithCamera();
                } else {
                    showPermissionDeniedDialog();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_main_dash_board);
        EdgeToEdge.enable(this);



        // Initialize views
        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set up NavController from NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_main);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        // FAB click listener
        fab.setOnClickListener(v -> checkCameraPermissionAndProceed());

        // Optional: Handle BottomNavigationView item clicks manually if needed
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.mainDashBoardMenuItem) {
                navController.navigate(R.id.dashBoardFragment);
                return true;
            } else if (id == R.id.communityMenu) {
                navController.navigate(R.id.communityFragment);
                return true;
            } else if (id == R.id.petprofileMenu) {
                Intent i = new Intent(this, PetDetailsActivity.class);
                startActivity(i);
                return true;
            } else if (id == R.id.personprofileMenu) {
                navController.navigate(R.id.personProfileFragment);
                return true;
            }
            return false;
        });

        // Set the initial selected item (optional, but good practice)
        // This ensures the correct item is highlighted on startup
        bottomNavigationView.setSelectedItemId(R.id.mainDashBoardMenuItem);
    }

    private void checkCameraPermissionAndProceed() {
        if (isInDiagnosisEnabledFragment()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
                showDiagnosisBottomSheetWithCamera();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show explanation why we need camera permission
                showPermissionExplanationDialog();
            } else {
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        }
    }

    private void showPermissionExplanationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Camera Permission Required")
                .setMessage("We need camera access to capture pet disease images for diagnosis. This helps in providing accurate disease detection.")
                .setPositiveButton("Grant Permission", (dialog, which) -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Camera permission is required for disease diagnosis", Toast.LENGTH_LONG).show();
                })
                .create()
                .show();
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("Without camera access, you cannot use the disease diagnosis feature. Please enable camera permission in app settings to continue.")
                .setPositiveButton("App Settings", (dialog, which) -> {
                    // Open app settings
                    openAppSettings();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Camera permission is required for disease diagnosis", Toast.LENGTH_LONG).show();
                })
                .create()
                .show();
    }

    private void openAppSettings() {
        // Intent to open app settings
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void showDiagnosisBottomSheetWithCamera() {
        diagnosisBottomSheet = DiseaseDiagnosisBottomSheet.newInstance();
        diagnosisBottomSheet.show(getSupportFragmentManager(), "diagnosis_sheet");
        diagnosisBottomSheet.openCamera();
    }

    private boolean isInDiagnosisEnabledFragment() {
        if (navController.getCurrentDestination() != null) {
            int currentDestinationId = navController.getCurrentDestination().getId();
            return currentDestinationId == R.id.dashBoardFragment ||
                   currentDestinationId == R.id.AIDiseaseDetectorFragment;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() != R.id.dashBoardFragment) {
            navController.popBackStack(); // Use popBackStack for Navigation Component
            // The BottomNavigationView will automatically update its selection
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Exit Application")
                    .setMessage("Do you really want to quit?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}