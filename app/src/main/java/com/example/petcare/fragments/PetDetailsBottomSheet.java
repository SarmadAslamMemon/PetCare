package com.example.petcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.petcare.R;
import com.example.petcare.modelclass.Pet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.Serializable;

public class PetDetailsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_PET = "pet";
    private Pet pet;

    public static PetDetailsBottomSheet newInstance(Pet pet) {
        PetDetailsBottomSheet fragment = new PetDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PET,  pet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pet = (Pet) getArguments().getSerializable(ARG_PET);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_details_sheet, container, false);
        
        if (pet != null) {
            ShapeableImageView petImage = view.findViewById(R.id.petImage);
            TextView petName = view.findViewById(R.id.petName);
            TextView petCategory = view.findViewById(R.id.petCategory);
            TextView petBreed = view.findViewById(R.id.petBreed);
            TextView petAge = view.findViewById(R.id.petAge);
            TextView petWeight = view.findViewById(R.id.petWeight);
            TextView petGender = view.findViewById(R.id.petGender);

            // Set pet details
            petName.setText(pet.getPetName());
            petCategory.setText(pet.getPetCategory());
            petBreed.setText(pet.getPetBreed());
            petAge.setText(String.format("%d years", Integer.parseInt(pet.getPetAge())));
            petWeight.setText(String.format("%d kg", pet.getPetWeight()));
            petGender.setText(pet.getPetGender());

            // Load pet image
            if (pet.getPetImageUrl() != null && !pet.getPetImageUrl().isEmpty()) {
                Glide.with(requireContext())
                        .load(pet.getPetImageUrl())
                        .placeholder(R.drawable.pets_foot_ic)
                        .error(R.drawable.pets_foot_ic)
                        .into(petImage);
            } else {
                petImage.setImageResource(R.drawable.pets_foot_ic);
            }
        }

        return view;
    }
} 