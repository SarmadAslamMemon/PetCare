package com.example.petcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBinderMapperImpl;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petcare.PetDetailsActivity;
import com.example.petcare.R;
import com.example.petcare.databinding.FragmentPersonProfileBinding;
import com.example.petcare.modelclass.User;
import com.example.petcare.utility.CameraUtils;
import com.example.petcare.utility.SharePreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class PersonProfileFragment extends Fragment {


    CardView cardView;
    ImageView petImageViewUserProfile;
    CircleImageView personProfilePic;
    TextView petName;
    CardView addPetLinearLayout;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    SharePreference sharePreference;


    private FragmentPersonProfileBinding dataBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_person_profile, container, false);
        sharePreference = new SharePreference(requireContext());
        View view = dataBinding.getRoot();


        getViews(view);
//        setPetData();
        setUserData();
//
//        addPetLinearLayout.setOnClickListener(v -> {
//            PetProfileFragment petProfileFragment = new PetProfileFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("action", "AddPet");
//            bundle.putInt("petId", 0);
//            petProfileFragment.setArguments(bundle);
//
//            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            transaction.replace(R.id.main, petProfileFragment);
//            transaction.commit();
//        });



        cardView.setOnClickListener(view2 -> {
            Intent i = new Intent(getActivity(), PetDetailsActivity.class);
            startActivity(i);
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
                            Uri imageUri = data.getData();
                            saveImageToInternalStorage(imageUri);
                        }
                    } else {
                        Toast.makeText(requireContext(), "Gallery selection failed", Toast.LENGTH_SHORT).show();
                    }
                });



        personProfilePic.setOnClickListener(view1 -> cameraUtils.checkPermissionsAndOpen(cameraActivityResultLauncher, galleryActivityResultLauncher));



        


        return view;


    }

    private void setUserData() {
        User user = sharePreference.getUserRegisteration();
        if(user != null){
            dataBinding.userNameEditText.setText(user.getFirstName());
            dataBinding.addressTextView.setText(user.getAddress());
            dataBinding.emailTextView.setText(user.getEmail());
            dataBinding.dobTextView.setText(user.getDateOfBirth());

        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            // Get the bitmap from the URI
            Bitmap bitmap = getBitmapFromUri(imageUri);

            if (bitmap != null) {
                // Save the bitmap as a file
                File file = new File(requireContext().getFilesDir(), "profile_image.png");  // You can change the file name and extension as needed

                // Create an output stream to write the bitmap to the file
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();

                sharePreference.saveImagePathToSharedPreferences(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }



    // Convert the URI to Bitmap
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void setPetData() {
        Bitmap bitmap = sharePreference.getPetProfilePic();
        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pets_foot_ic);
        }

        petImageViewUserProfile.setImageBitmap(bitmap);
        petName.setText(sharePreference.getUserPetName());
    }

    private void getViews(View view) {
        personProfilePic = view.findViewById(R.id.profilePicCardView);
        cardView = view.findViewById(R.id.addPetProfile);
        petImageViewUserProfile = view.findViewById(R.id.petImageViewUserProfile);
        petName = view.findViewById(R.id.petNameUserProfile);
        addPetLinearLayout = view.findViewById(R.id.addPetCard);

    }
}