package com.example.petcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.R;
import com.example.petcare.adapter.BlogAdapter;
import com.example.petcare.network.model.Blog;
import com.example.petcare.modelclass.BlogListResponse;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.SharePreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardFragment extends Fragment implements BlogAdapter.OnBlogClickListener {

    RecyclerView recyclerView;
    ImageView notificationIcon, personProfilePic;
    NavController navController;
    View view;
    BlogAdapter blogAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EdgeToEdge.enable(requireActivity());
        view = inflater.inflate(R.layout.maindashboard_fragment, container, false);

        navController = NavHostFragment.findNavController(this);

        findViews(view);
        setupCardListeners();
        setupBlogCarousel();
        setupViewAllButton();

        return view;
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerCorouselView);
        TextView userNameText=view.findViewById(R.id.userNameText);
        userNameText.setText(SharePreference.getInstance(requireActivity()).getUserPetName());

//        notificationIcon = view.findViewById(R.id.notificationIcon);
//        personProfilePic = view.findViewById(R.id.profileIcDashBoardImageView);
    }

    private void setupBlogCarousel() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        blogAdapter = new BlogAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(blogAdapter);
        loadBlogs();
    }

    private void loadBlogs() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getAllBlogs().enqueue(new Callback<BlogListResponse>() {
            @Override
            public void onResponse(Call<BlogListResponse> call, Response<BlogListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BlogListResponse blogResponse = response.body();
                    if (blogResponse.isSuccess()) {
                        blogAdapter.updateBlogs(blogResponse.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<BlogListResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void setupViewAllButton() {
        TextView viewAllButton = view.findViewById(R.id.viewAllBlogs);
        viewAllButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashBoardFragment_to_communityFragment);
        });
    }

    private void setupCardListeners() {
        view.findViewById(R.id.dogCardView).setOnClickListener(v -> {
            showPetInfoDialog("Dog", "Dogs are known for their loyalty, intelligence, and unconditional love. They come in various breeds, each with unique characteristics and personalities. Dogs are excellent companions and can be trained for various tasks including assistance, therapy, and protection.", R.drawable.dog_svgrepo_com);
        });
        view.findViewById(R.id.catCardView).setOnClickListener(v -> {
            showPetInfoDialog("Cat", "Cats are graceful, independent creatures known for their agility and hunting skills. They are excellent companions who can be both playful and affectionate. Cats are known for their cleanliness and ability to adapt to various living environments.", R.drawable.cat_svgrepo_com);
        });
        view.findViewById(R.id.fishCardView).setOnClickListener(v -> {
            showPetInfoDialog("Fish", "Fish are fascinating aquatic creatures that come in countless species, colors, and sizes. They create a peaceful and mesmerizing environment in aquariums. Fish keeping is a popular hobby that teaches responsibility and provides a calming presence in homes.", R.drawable.fish_svgrepo);
        });
        view.findViewById(R.id.parrotCardView).setOnClickListener(v -> {
            showPetInfoDialog("Parrot", "Parrots are intelligent birds known for their ability to mimic human speech and their vibrant plumage. They are social creatures that can form strong bonds with their owners. Parrots require mental stimulation and social interaction to thrive.", R.drawable.parrot_svgrepo_com);
        });
        view.findViewById(R.id.rabbitCardView).setOnClickListener(v -> {
            showPetInfoDialog("Rabbit", "Rabbits are gentle, social animals that make wonderful pets. They are known for their soft fur, long ears, and hopping movements. Rabbits are intelligent creatures that can be litter-trained and enjoy playing with toys.", R.drawable.rabbit_easter_svgrepo_com);
        });

        view.findViewById(R.id.healthTipCard).setOnClickListener(v -> {
            navController.navigate(R.id.action_dashBoardFragment_to_healthTipFragment);
        });

        view.findViewById(R.id.petFoodTimeCardView).setOnClickListener(v -> {
            navController.navigate(R.id.action_dashBoardFragment_to_petMealFragment);
        });

        view.findViewById(R.id.consultationCardView).setOnClickListener(v -> {
            navController.navigate(R.id.action_dashBoardFragment_to_consultationFragment);
        });



    }

    private void showPetInfoDialog(String petType, String description, int imageResId) {
        PetInfoDialog dialog = PetInfoDialog.newInstance(petType, description, imageResId);
        dialog.show(getChildFragmentManager(), "pet_info_dialog");
    }

    @Override
    public void onBlogClick(Blog blog) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("blog", blog);
        navController.navigate(R.id.action_dashBoardFragment_to_blogDetailFragment, bundle);
    }
}
