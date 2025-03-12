package com.example.petcare.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RawRes;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.petcare.GeneralClass;
import com.example.petcare.Notification_Fragment;
import com.example.petcare.R;
import com.example.petcare.adapter.CorouselSliderAdapter;
import com.example.petcare.modelclass.Blog;
import com.example.petcare.modelclass.HealthTip;
import com.example.petcare.utility.SharePreference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashBoardFragment extends Fragment implements CorouselSliderAdapter.OnItemClickListener {


    RecyclerView recyclerView;
    CardView healthTipCardView, petMealCardView, consulationCardView, aiDetectorCardView;
    ImageView notificationIcon,personProfilePic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maindashboard_fragment, container, false);

        findViews(view);
        setupCardListeners(view);
        loadGifImages(view);
        loadCorousel();


        personProfilePic.setOnClickListener(v -> loadFragment(new PersonProfileFragment()));


        return view;
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerCorouselView);
        notificationIcon=view.findViewById(R.id.notificationIcon);
        personProfilePic=view.findViewById(R.id.profileIcDashBoardImageView);
    }

    private void loadCorousel() {
        List<Blog> blogsList = new ArrayList<>();
       blogsList =GeneralClass.getBlogs();

        CorouselSliderAdapter adapter = new CorouselSliderAdapter(requireContext(), (ArrayList<Blog>) blogsList, this);
        recyclerView.setAdapter(adapter);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        // Replace the current fragment with the new one
        fragmentTransaction.replace(R.id.frameLayoutMainDashBoard, fragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    private void setupCardListeners(View view) {
        view.findViewById(R.id.healthTipCardView).setOnClickListener(v -> loadHealthTipsAlertDialogueBox());
        view.findViewById(R.id.petMealCardView).setOnClickListener(v -> loadFragment(new PetMealFragment()));
        view.findViewById(R.id.consultationCardView).setOnClickListener(v -> loadFragment(new CommunityFragment()));
        view.findViewById(R.id.aiDetectorCardView).setOnClickListener(v -> loadFragment(new AIDiseaseDetectorFragment()));
        view.findViewById(R.id.notificationIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Notification_Fragment notificationFragment = new Notification_Fragment();
                loadFragment(notificationFragment);
            }
        });

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

    @Override
    public void onClick(ImageView imageView, Blog blog) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("blog",  blog);
        BlogFragment fragment = new BlogFragment();
        fragment.setArguments(bundle);
        loadFragment(fragment);




    }
}





