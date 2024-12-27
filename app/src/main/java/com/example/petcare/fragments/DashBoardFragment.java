package com.example.petcare.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.petcare.R;
import com.example.petcare.adapter.CorouselSliderAdapter;
import com.example.petcare.modelclass.HealthTip;
import com.example.petcare.utility.SharePreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashBoardFragment extends Fragment {


    RecyclerView recyclerView;
    CardView healthTipCardView, petMealCardView, consulationCardView, aiDetectorCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maindashboard_fragment, container, false);



        findViews(view);
        setupCardListeners(view);
        loadGifImages(view);
        loadCorousel();

        return view;
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerCorouselView);
    }

    private void loadCorousel() {
        ArrayList<String> imagesList = new ArrayList<>();
        imagesList.add("https://learnwebcode.github.io/json-example/images/cat-2.jpg");
        imagesList.add("https://learnwebcode.github.io/json-example/images/dog-1.jpg");
        imagesList.add("https://learnwebcode.github.io/json-example/images/cat-1.jpg");
        imagesList.add("https://res.cloudinary.com/hzrulbrds/image/upload/v1582764036/benji_rkg884.jpg");


        CorouselSliderAdapter adapter = new CorouselSliderAdapter(requireContext(), imagesList);
        recyclerView.setAdapter(adapter);


    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMainDashBoard, fragment);
        fragmentTransaction.commit();
    }

    private void setupCardListeners(View view) {
        view.findViewById(R.id.healthTipCardView).setOnClickListener(v -> loadHealthTipsAlertDialogueBox());
        view.findViewById(R.id.petMealCardView).setOnClickListener(v -> loadFragment(new PetMealFragment()));
        view.findViewById(R.id.consulationCardView).setOnClickListener(v -> loadFragment(new CommunityFragment()));
        view.findViewById(R.id.aiDetectorCardView).setOnClickListener(v -> loadFragment(new AIDiseaseDetectorFragment()));
    }

    private void loadHealthTipsAlertDialogueBox() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_health_tips);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Initialize views inside the dialog
        ImageView closeIcon = dialog.findViewById(R.id.closeIcon);
        TextView titleTextView = dialog.findViewById(R.id.titleTextView);
        TextView descriptionTextView = dialog.findViewById(R.id.descriptionTextView);

        // Get a random health tip from SharedPreferences (assuming you have set it up)
        HealthTip healthTip = SharePreference.getInstance(requireContext()).getHealthTip();

        // Set the title and description text from the HealthTip model
        titleTextView.setText(healthTip.getTitle());
        descriptionTextView.setText(healthTip.getDescription());

        // Close the dialog when the close icon is clicked
        closeIcon.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    private void loadGifImages(View view) {
        ImageView healthTipsGif = view.findViewById(R.id.imageViewHealthTipsGif);
        Glide.with(this).asGif().load(R.drawable.pet_care_gif).into(healthTipsGif);

        ImageView petMealGif = view.findViewById(R.id.imageViewPetMealGif);
        Glide.with(this).asGif().load(R.drawable.pet_food_gif).into(petMealGif);

        ImageView consultationGif = view.findViewById(R.id.imageViewConsultationGif);
        Glide.with(this).asGif().load(R.drawable.doctor_pet_care_gif).into(consultationGif);

        ImageView aiDetectorGif = view.findViewById(R.id.imageViewAIDetectorGif);
        Glide.with(this).asGif().load(R.drawable.ai_driven_gif).into(aiDetectorGif);
    }
}
