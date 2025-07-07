package com.example.petcare.fragments.consultion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.petcare.R;
import com.example.petcare.adapter.BlogAdapter;
import com.example.petcare.model.Blog;
import com.example.petcare.modelclass.BlogListResponse;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.ProgressDialogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityFragment extends Fragment implements BlogAdapter.OnBlogClickListener {

    private RecyclerView recyclerView;
    private BlogAdapter blogAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavController navController;
    private LinearLayout noDataLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main);
        initViews(view);
        setupRecyclerView();
        loadBlogs();
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.blogsRecyclerView);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        ImageButton fabCreateBlog = view.findViewById(R.id.btnCreateBlog);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        fabCreateBlog.setOnClickListener(v -> showCreateBlogBottomSheet());
        swipeRefreshLayout.setOnRefreshListener(this::loadBlogs);
    }

    private void setupRecyclerView() {
        blogAdapter = new BlogAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(blogAdapter);
    }

    private void loadBlogs() {
        ProgressDialogUtil.showProgressBar(requireContext(), true);
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        
        apiService.getAllBlogs().enqueue(new Callback<BlogListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BlogListResponse> call, @NonNull Response<BlogListResponse> response) {
                ProgressDialogUtil.showProgressBar(requireContext(), false);
                swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    BlogListResponse blogResponse = response.body();
                    if (blogResponse.isSuccess()) {
                        List<Blog> blogs = blogResponse.getData();
                        blogAdapter.updateBlogs(blogs);
                        updateNoDataVisibility(blogs.isEmpty());
                    } else {
                        showError(blogResponse.getMessage());
                        updateNoDataVisibility(true);
                    }
                } else {
                    showError("Failed to load blogs");
                    updateNoDataVisibility(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BlogListResponse> call, @NonNull Throwable t) {
                ProgressDialogUtil.showProgressBar(requireContext(), false);
                swipeRefreshLayout.setRefreshing(false);
                showError("Network error occurred");
                updateNoDataVisibility(true);
            }
        });
    }

    private void updateNoDataVisibility(boolean showNoData) {
        if (noDataLayout != null) {
            noDataLayout.setVisibility(showNoData ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(showNoData ? View.GONE : View.VISIBLE);
        }
    }

    private void showCreateBlogBottomSheet() {
        CreateBlogBottomSheet bottomSheet = CreateBlogBottomSheet.newInstance();
        bottomSheet.show(getChildFragmentManager(), "create_blog");
    }

    @Override
    public void onBlogClick(Blog blog) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("blog", blog);
        navController.navigate(R.id.action_communityFragment_to_blogDetailFragment, bundle);
    }

    private void showError(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}