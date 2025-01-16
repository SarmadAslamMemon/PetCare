package com.example.petcare.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.petcare.R;
import com.example.petcare.modelclass.Pet;
import com.example.petcare.utility.SharePreference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetProfileFragment extends Fragment {

    CircleImageView petProfileImage;
    SharePreference sharePreference;
    Button buttonAddUpdate;
    EditText petNameEditText, petBreedEditTextView, petAgeEditText, petCategoryTextView,petFavFoodTextView;
    RadioGroup petGenderRadioGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_profile, container, false);


        getViews(view);
//        checkIfAddPet();

        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonAddUpdate.getText().toString().contains("Add Pet")){

                    if(validateFields())
                    {
                        String petName, petBreed, petAge, petCategory,petFavFood,petGender;
                        petName = petNameEditText.getText().toString();
                        petAge = petAgeEditText.getText().toString();
                        petCategory = petCategoryTextView.getText().toString();
                        petFavFood = petFavFoodTextView.getText().toString();

                        int selectedRadioButtonId = petGenderRadioGroup.getCheckedRadioButtonId();

                        if (selectedRadioButtonId == R.id.radioMale) {
                            petGender = "Male";
                        } else {
                            petGender = "Female";
                        }

                        sharePreference.addPetCount();
                        Pet pet = new Pet(sharePreference.getPetCount(),1,petName,petGender,Integer.parseInt(petAge),petCategory,petFavFood,petGender);
                        sharePreference.savePet(pet);

                    }

                }
            }
        });


        return view;

    }

    private boolean validateFields() {

        String petName, petBreed, petAge, petCategory,petFavFood,petGender;
        petName = petNameEditText.getText().toString();
        petAge = petAgeEditText.getText().toString();
        petCategory = petCategoryTextView.getText().toString();
        petFavFood = petFavFoodTextView.getText().toString();
        petBreed = petBreedEditTextView.getText().toString();

        int selectedRadioButtonId = petGenderRadioGroup.getCheckedRadioButtonId();

        if(petName.isEmpty())
        {
            petNameEditText.setError("Please enter pet name");
            return false;
        }else if(petAge.isEmpty())
        {
            petNameEditText.setError(null);
            petAgeEditText.setError("Please enter pet age");
            return false;
        }else if(petFavFood.isEmpty())
        {
            petAgeEditText.setError(null);

            petFavFoodTextView.setError("Please enter pet favourite food");
            return false;
        }else if(petBreed.isEmpty())
        {
            petFavFoodTextView.setError(null);
            petNameEditText.setError(null);
            petAgeEditText.setError(null);
            petBreedEditTextView.setError("Please enter pet breed");
            return false;
        }else{
            petBreedEditTextView.setError(null);
            petNameEditText.setError(null);
            petAgeEditText.setError(null);
            petFavFoodTextView.setError(null);
            return true;
        }


    }

    private void checkIfAddPet() {
        String action = getArguments().getString("action");
        int petId = getArguments().getInt("petId");
        if(action!= null && action.contains("Add Pet") && petId==0)
        {
            Log.d("jarvis", "checkIfAddPet: " + action);
            buttonAddUpdate.setText("Add Pet");
        }else{
            setPetData(petId);
            setPetImage();
            buttonAddUpdate.setText("Update Pet");
        }
    }

    private void setPetData(int petId) {
        Pet pet = sharePreference.getPetById(petId);
        if(pet!=null) {
            Log.d("jarvis", "setPetData: " + pet.getPetName());
        }else{
            Log.d("jarvis", "setPetData: null");
        }
    }

    private void setPetImage() {
        Bitmap bitmap = sharePreference.getPetProfilePic();
        if(bitmap!=null) {
            petProfileImage.setImageBitmap(bitmap);
        }else{
            petProfileImage.setImageResource(R.drawable.pet_care_gif);
        }
    }

    private void getViews(View view) {
        sharePreference = new SharePreference(requireContext());
        petProfileImage = view.findViewById(R.id.profilePicCardView);
        buttonAddUpdate = view.findViewById(R.id.nextBtnOne);
        petNameEditText = view.findViewById(R.id.petNameTextView);
        petAgeEditText = view.findViewById(R.id.petAgeTextView);
        petGenderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        petFavFoodTextView = view.findViewById(R.id.petFavFoodTextView);
        petBreedEditTextView = view.findViewById(R.id.petBreedTextView);


    }
}