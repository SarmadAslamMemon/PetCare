package com.example.petcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.petcare.R;

public class PetInfoDialog extends DialogFragment {
    private static final String ARG_PET_TYPE = "pet_type";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_RES_ID = "image_res_id";

    public static PetInfoDialog newInstance(String petType, String description, int imageResId) {
        PetInfoDialog dialog = new PetInfoDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PET_TYPE, petType);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_pet_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            String petType = getArguments().getString(ARG_PET_TYPE);
            String description = getArguments().getString(ARG_DESCRIPTION);
            int imageResId = getArguments().getInt(ARG_IMAGE_RES_ID);

            TextView titleTextView = view.findViewById(R.id.petTypeTextView);
            TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
            ImageView petImageView = view.findViewById(R.id.petImageView);

            titleTextView.setText(petType);
            descriptionTextView.setText(description);
            Glide.with(this)
                .load(imageResId)
                .into(petImageView);

            view.findViewById(R.id.closeButton).setOnClickListener(v -> dismiss());
        }
    }
} 