package com.example.petcare.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petcare.GeneralClass;
import com.example.petcare.notification.Notification_Fragment;
import com.example.petcare.R;
import com.example.petcare.adapter.CorouselSliderAdapter;
import com.example.petcare.modelclass.Blog;
import com.example.petcare.modelclass.HealthTip;
import com.example.petcare.utility.SharePreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashBoardFragment extends Fragment implements CorouselSliderAdapter.OnItemClickListener {


    RecyclerView recyclerView;
    ImageView notificationIcon,personProfilePic;

    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maindashboard_fragment, container, false);

        findViews(view);
        setupCardListeners(view);
        loadGifImages(view);
        loadCorousel();

         navController = findNavController(this);

        personProfilePic.setOnClickListener(v -> navController.navigate(R.id.action_dashBoardFragment_to_personProfileFragment));


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
        view.findViewById(R.id.healthTipCardView).setOnClickListener(v -> navController.navigate(R.id.action_dashBoardFragment_to_healthTipFragment));
        view.findViewById(R.id.petMealCardView).setOnClickListener(v -> navController.navigate(R.id.action_dashBoardFragment_to_petMealFragment));
        view.findViewById(R.id.consultationCardView).setOnClickListener(v -> navController.navigate(R.id.action_dashBoardFragment_to_consultationFragment));
        view.findViewById(R.id.aiDetectorCardView).setOnClickListener(v -> navController.navigate(R.id.action_dashBoardFragment_to_AIDiseaseDetectorFragment));
        view.findViewById(R.id.notificationIcon).setOnClickListener(view1 -> navController.navigate(R.id.action_dashBoardFragment_to_notification_Fragment));

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





