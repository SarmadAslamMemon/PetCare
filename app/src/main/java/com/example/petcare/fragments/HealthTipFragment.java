package com.example.petcare.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.petcare.R;
import com.example.petcare.modelclass.HealthTip;
import com.example.petcare.utility.SharePreference;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HealthTipFragment extends Fragment {

    private LinearLayout cardContainer;
    private View lastExpandedCard = null;
    private View topAppBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_tip, container, false);
        cardContainer = view.findViewById(R.id.cardContainer);

        MaterialToolbar topAppBar = view.findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        topAppBar.setTitle("Health Tips");




        // Load all cards
        loadStaticCards(inflater);

        return view;
    }

    private void loadStaticCards(LayoutInflater inflater) {
        List<HealthTip> healthTips = getHealthTips();

        for (HealthTip tip : healthTips) {
            View card = inflater.inflate(R.layout.item_health_tip_card, cardContainer, false);

            TextView title = card.findViewById(R.id.tipTitle);
            TextView description = card.findViewById(R.id.tipDescription);
            LottieAnimationView image = card.findViewById(R.id.tipImage);
            ImageView dropIcon = card.findViewById(R.id.dropDownIcon);

            title.setText(tip.getTitle());
            description.setText(tip.getDescription());

            int imageResId = getResources().getIdentifier(tip.getLottieRawRes(), "raw", requireContext().getPackageName());
            if (imageResId != 0) {
                Log.d("ImageResId", "Image Resource ID: " + imageResId);
                image.setAnimation(imageResId);
                image.playAnimation();
            }

            dropIcon.setOnClickListener(v -> {
                if (description.getVisibility() == View.GONE) {
                    if (lastExpandedCard != null && lastExpandedCard != description) {
                        lastExpandedCard.setVisibility(View.GONE);
                    }
                    description.setVisibility(View.VISIBLE);
                    lastExpandedCard = description;
                } else {
                    description.setVisibility(View.GONE);
                    lastExpandedCard = null;
                }
            });

            cardContainer.addView(card);
        }
    }

    private List<HealthTip> getHealthTips() {
        List<HealthTip> healthTips = new ArrayList<>();

        healthTips.add(new HealthTip("Healthy Diet",
                "1. Balanced and nutritious diet.\n2. Essential for energy.\n3. Includes proteins, vitamins, minerals.\n4. Avoid overfeeding.\n5. Consult vet.",
                "pet_care_food_lottie"));

        healthTips.add(new HealthTip("Regular Exercise",
                "1. Daily activity is vital.\n2. Controls weight.\n3. Encourages bonding.\n4. Use toys.\n5. Tailor to pet's needs.",
                "dog_walking_anim"));

        healthTips.add(new HealthTip("Grooming",
                "1. Keeps pet clean.\n2. Prevents tangles.\n3. Trim nails.\n4. Bath regularly.\n5. Clean ears, teeth, paws.",
                "dogs_smiling_anim"));

        healthTips.add(new HealthTip("Vet Visits",
                "1. Regular check-ups.\n2. Early detection of issues.\n3. Vaccinations and parasite control.\n4. Discuss concerns.\n5. Promotes longer life.",
                "lottie_vet"));

        healthTips.add(new HealthTip("Mental Stimulation",
                "1. Prevent boredom.\n2. Use puzzle toys.\n3. Train new tricks.\n4. Social interaction.\n5. Enrich environment.",
                "mental_activity_pet"));

        return healthTips;
    }
}
