package com.example.petcare.fragments;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.example.petcare.R;
import com.example.petcare.modelclass.PredictionResponse;
import com.example.petcare.utility.PredictionHelper;
import com.example.petcare.utility.ProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class DiseaseDiagnosisBottomSheet extends BottomSheetDialogFragment {

    private ShapeableImageView capturedImageView;
    private TextInputEditText symptomsInput;

    private AutoCompleteTextView petBreedInput;
    private MaterialButton diagnoseButton;

    private Uri imageUri;
    private boolean shouldOpenCamera = false;
    private PredictionHelper predictionHelper;
    private ProgressDialog progressDialog;
    private PredictionResponse imagePredictionResponse;
    private PredictionResponse textPredictionResponse;
    private MaterialButton dogButton, catButton, fishButton;

    private final String[] petTypes = {"Dog", "Cat", "Fish"};
    private final String[] dogBreeds = {"Labrador", "German Shepherd", "Golden Retriever", "Bulldog", "Beagle", "Other"};
    private final String[] catBreeds = {"Persian", "Siamese", "Maine Coon", "Ragdoll", "British Shorthair", "Other"};
    private final String[] fishBreeds = {"Goldfish", "Betta", "Guppy", "Angelfish"};


    private String selectedPetType = "";
    private String selectedPetBreed = "";


    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        if (photo != null) {
                            capturedImageView.setImageBitmap(photo);
                            imageUri = getImageUri(photo);
                            if (imageUri != null) {
                                predictFromImage(imageUri);
                            }
                        }
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_diagnosis_sheet, container, false);
        predictionHelper = new PredictionHelper(requireContext());
        progressDialog = new ProgressDialog(requireContext());
        initViews(view);

        dogButton.setOnClickListener(v -> {
            selectPetType("Dog");
            updatePetBreedDropdown(dogBreeds);

        });
        catButton.setOnClickListener(v -> {
            selectPetType("Cat");
            updatePetBreedDropdown(catBreeds);

        });
        fishButton.setOnClickListener(v -> {
            selectPetType("Fish");
            updatePetBreedDropdown(fishBreeds);

        });


        return view;
    }
    private void selectPetType(String petType) {
        selectedPetType = petType;
        // Update breed dropdown
        switch (petType) {
            case "Dog":
                updatePetBreedDropdown(dogBreeds);
                break;
            case "Cat":
                updatePetBreedDropdown(catBreeds);
                break;
            case "Fish":
                updatePetBreedDropdown(fishBreeds);
                break;
            default:
                updatePetBreedDropdown(new String[]{"Other"});
                break;
        }

        // Highlight selected button
        resetButtonStyles();
        switch (petType) {
            case "Dog":
                dogButton.setBackgroundColor(getResources().getColor(R.color.primary_color));
                dogButton.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Cat":
                catButton.setBackgroundColor(getResources().getColor(R.color.primary_color));
                catButton.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Fish":
                fishButton.setBackgroundColor(getResources().getColor(R.color.primary_color));
                fishButton.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                dogButton.setBackgroundColor(getResources().getColor(R.color.primary_color));
                dogButton.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }



    private void initViews(View view) {
        capturedImageView = view.findViewById(R.id.capturedImageView);
        symptomsInput = view.findViewById(R.id.symptomsInput);
        petBreedInput = view.findViewById(R.id.petBreedInput);
        diagnoseButton = view.findViewById(R.id.diagnoseButton);
        dogButton = view.findViewById(R.id.dogButton);
        catButton = view.findViewById(R.id.catButton);
        fishButton = view.findViewById(R.id.fishButton);
//        resultTextView = view.findViewById(R.id.resultTextView);
        diagnoseButton.setOnClickListener(v -> validateAndSubmit());
    }


    private void resetButtonStyles() {
        dogButton.setBackgroundColor(getResources().getColor(R.color.white));
        dogButton.setTextColor(getResources().getColor(R.color.primary_color));
        catButton.setBackgroundColor(getResources().getColor(R.color.white));
        catButton.setTextColor(getResources().getColor(R.color.primary_color));
        fishButton.setBackgroundColor(getResources().getColor(R.color.white));
        fishButton.setTextColor(getResources().getColor(R.color.primary_color));
    }

    private void updatePetBreedDropdown(String[] breeds) {
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<>(requireContext(),
            android.R.layout.simple_dropdown_item_1line, breeds);
        petBreedInput.setAdapter(breedAdapter);
        petBreedInput.setText("", false);
    }

    private void validateAndSubmit() {
        String symptoms = Objects.requireNonNull(symptomsInput.getText()).toString().trim();
        String petBreed = petBreedInput.getText().toString().trim();
        String petType = selectedPetType;

        if (TextUtils.isEmpty(symptoms)) {
            symptomsInput.setError("Symptoms are required");
            return;
        }

        if (TextUtils.isEmpty(petBreed)) {
            petBreedInput.setError("Pet breed is required");
            return;
        }

        if (TextUtils.isEmpty(petType)) {
            Toast.makeText(requireContext(), "Please select a pet type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        progressDialog.show();

        // Concatenate the input for text prediction
        String combinedText = String.format("%s %s %s", symptoms, petBreed, petType);
        predictFromText(combinedText);
    }

    private void predictFromText(String text) {
        predictionHelper.predictFromText(text, new PredictionHelper.PredictionCallback() {
            @Override
            public void onSuccess(PredictionResponse response) {
                textPredictionResponse = response;
                progressDialog.dismiss();
                showPredictionDialog();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void predictFromImage(Uri imageUri) {
        progressDialog.show();
        predictionHelper.predictFromImage(imageUri, new PredictionHelper.PredictionCallback() {
            @Override
            public void onSuccess(PredictionResponse response) {
                imagePredictionResponse = response;
                progressDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showPredictionDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_prediction_result);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // ⚠️ Set full width
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }

        LottieAnimationView lottieAnimation = dialog.findViewById(R.id.lottieAnimation);
        TextView speciesText = dialog.findViewById(R.id.speciesText);
        TextView diseaseText = dialog.findViewById(R.id.diseaseText);
        Button okButton = dialog.findViewById(R.id.okButton);
        Button consultButton = dialog.findViewById(R.id.consultButton);

        // Set prediction results
        StringBuilder speciesBuilder = new StringBuilder();
        StringBuilder diseaseBuilder = new StringBuilder();

        if (imagePredictionResponse != null) {
            speciesBuilder.append("Image Analysis:\n");
            speciesBuilder.append(String.format("Species: %s (%.2f%% confidence)\n\n",
                    imagePredictionResponse.getSpecies(), imagePredictionResponse.getSpeciesConfidence() * 100));

            diseaseBuilder.append("Image Analysis:\n");
            diseaseBuilder.append(String.format("Disease: %s (%.2f%% confidence)\n\n",
                    imagePredictionResponse.getDisease(), imagePredictionResponse.getDiseaseConfidence() * 100));
        }

        if (textPredictionResponse != null) {
            speciesBuilder.append("Text Analysis:\n");
            speciesBuilder.append(String.format("Species: %s (%.2f%% confidence)",
                    textPredictionResponse.getSpecies(), textPredictionResponse.getSpeciesConfidence() * 100));

            diseaseBuilder.append("Text Analysis:\n");
            diseaseBuilder.append(String.format("Disease: %s (%.2f%% confidence)",
                    textPredictionResponse.getDisease(), textPredictionResponse.getDiseaseConfidence() * 100));
        }

        speciesText.setText(speciesBuilder.toString());
        diseaseText.setText(diseaseBuilder.toString());

        // Set button click listeners
        okButton.setOnClickListener(v -> dialog.dismiss());
        consultButton.setOnClickListener(v -> {
            dialog.dismiss();
            navigateToConsultation();
        });

        dialog.show();
    }


    private void navigateToConsultation() {

    }

    private Uri getImageUri(Bitmap bitmap) {
        try {
            File cachePath = new File(requireContext().getCacheDir(), "images");
            cachePath.mkdirs();
            File imageFile = new File(cachePath, "captured_image.jpg");

            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DiseaseDiagnosisBottomSheet newInstance() {
        return new DiseaseDiagnosisBottomSheet();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldOpenCamera) {
            shouldOpenCamera = false;
            launchCamera();
        }
    }

    public void openCamera() {
        if (isAdded()) {
            launchCamera();
        } else {
            shouldOpenCamera = true;
        }
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }
}