package com.example.petcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.petcare.R;
import com.example.petcare.utility.CameraUtils;

public class AIDiseaseDetectorFragment extends Fragment {

    private ImageView imageview;
    private CardView cardView;
    private EditText editText;
    private AutoCompleteTextView autoCompleteTextView;

    // List of pet types to be displayed in AutoCompleteTextView
    String[] petType = {"Dog", "Cat", "Bird", "Fish", "Rabbit"};
    ArrayAdapter<String> arrayAdapter;

    // ActivityResultLaunchers for camera and gallery
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    public AIDiseaseDetectorFragment() {
        super(R.layout.fragment_a_i_disease_detector);  // The fragment layout
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        imageview = view.findViewById(R.id.diseaseSymptomImage);
        cardView = view.findViewById(R.id.cardView);
        editText = view.findViewById(R.id.petAge);
        autoCompleteTextView = view.findViewById(R.id.petTypeDropdown);

        // Set up AutoCompleteTextView for pet types
        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.activity_pet_types, petType);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String pettypes = adapterView.getItemAtPosition(i).toString();
                // Use the selected pet type if needed (e.g., log or store the value)
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle case when no pet type is selected
            }
        });

        // Add text watcher to EditText for validating pet age input
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("jarvis", "Age input length: " + charSequence.toString().length());
                if (charSequence.toString().length() > 2) {
                    editText.setError("Invalid Age: Age must be a 2-digit number");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // Initialize CameraUtils class for handling image capture
        CameraUtils cameraUtils = new CameraUtils(requireActivity(), imageview);

        // Set up ActivityResultLauncher for Camera to capture image
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");  // Get captured photo as Bitmap
                            imageview.setImageBitmap(photo);  // Set photo to ImageView
                        }
                    } else {
                        Toast.makeText(requireContext(), "Camera capture failed", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set up ActivityResultLauncher for Gallery to select image
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();  // Get image URI from gallery
                            imageview.setImageURI(imageUri);  // Set the selected image to ImageView
                        }
                    } else {
                        Toast.makeText(requireContext(), "Gallery selection failed", Toast.LENGTH_SHORT).show();
                    }
                });

        // OnClickListener to handle image selection (either Camera or Gallery)
        cardView.setOnClickListener(view1 -> {
            // Check permissions and open the image selection dialog (camera/gallery)
            cameraUtils.checkPermissionsAndOpen(cameraActivityResultLauncher, galleryActivityResultLauncher);
        });
    }

    // Handle permission result (for camera or storage permissions)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CameraUtils cameraUtils = new CameraUtils(requireActivity(), imageview);
        cameraUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
