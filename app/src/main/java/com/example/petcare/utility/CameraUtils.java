package com.example.petcare.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraUtils {

    private Context context;
    private ImageView imageView;

    // Constructor
    public CameraUtils(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    // Method to check permissions and ask for them if necessary
    public void checkPermissionsAndOpen(ActivityResultLauncher<Intent> cameraActivityResultLauncher, ActivityResultLauncher<Intent> galleryActivityResultLauncher) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Request permissions if not granted
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1001);
        } else {
            // Permissions granted, show the image selection dialog
            showImageSelectionDialog(cameraActivityResultLauncher, galleryActivityResultLauncher);
        }
    }

    // Show a dialog to let user choose between Camera and Gallery
    private void showImageSelectionDialog(ActivityResultLauncher<Intent> cameraActivityResultLauncher, ActivityResultLauncher<Intent> galleryActivityResultLauncher) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Select Image")
                .setMessage("Choose how you want to upload an image")
                .setPositiveButton("Gallery", (dialog, which) -> openGallery(galleryActivityResultLauncher))
                .setNegativeButton("Camera", (dialog, which) -> openCamera(cameraActivityResultLauncher))
                .show();
    }

    // Open the Gallery to pick an image
    private void openGallery(ActivityResultLauncher<Intent> galleryActivityResultLauncher) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryActivityResultLauncher.launch(galleryIntent);
    }

    // Open the Camera to capture an image
    private void openCamera(ActivityResultLauncher<Intent> cameraActivityResultLauncher) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    // Handle the permission request result (if using permissions for camera/storage)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, show the dialog
                Toast.makeText(context, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permissions denied, show a message
                Toast.makeText(context, "Camera and Storage permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
