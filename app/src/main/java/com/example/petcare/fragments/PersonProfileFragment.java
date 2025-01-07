package com.example.petcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petcare.PetDetailsActivity;
import com.example.petcare.R;
import com.example.petcare.utility.CameraUtils;
import com.example.petcare.utility.SharePreference;

import de.hdodenhof.circleimageview.CircleImageView;


public class PersonProfileFragment extends Fragment {


    CardView cardView;
    ImageView petImageViewUserProfile;
    CircleImageView personProfilePic;
    TextView petName;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    SharePreference sharePreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_profile, container, false);
        sharePreference = new SharePreference(requireContext());


        getViews(view);
        setPetData();


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PetDetailsActivity.class);
                startActivity(i);
            }
        });



        //setting image
        CameraUtils cameraUtils = new CameraUtils(requireActivity(), personProfilePic);

        // Set up ActivityResultLauncher for Camera to capture image
        // Get captured photo as Bitmap
        // Set photo to ImageView
        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            personProfilePic.setImageBitmap(photo);  // Set photo to ImageView
                        }
                    } else {
                        Toast.makeText(requireContext(), "Camera capture failed", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set up ActivityResultLauncher for Gallery to select image
        // Get image URI from gallery
        // Set the selected image to ImageView
        ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();  // Get image URI from gallery
                            personProfilePic.setImageURI(imageUri);  // Set the selected image to ImageView
                        }
                    } else {
                        Toast.makeText(requireContext(), "Gallery selection failed", Toast.LENGTH_SHORT).show();
                    }
                });



        personProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtils.checkPermissionsAndOpen(cameraActivityResultLauncher, galleryActivityResultLauncher);
            }
        });



        


        return view;


    }

    private void setPetData() {
        Bitmap bitmap = sharePreference.getPetProfilePic();
        petImageViewUserProfile.setImageBitmap(bitmap);
        petName.setText(sharePreference.getUserPetName());

    }

    private void getViews(View view) {
        personProfilePic = view.findViewById(R.id.profilePicCardView);
        cardView = view.findViewById(R.id.addpetprofile);
        petImageViewUserProfile = view.findViewById(R.id.petImageViewUserProfile);
        petName = view.findViewById(R.id.petNameUserProfile);

    }
}