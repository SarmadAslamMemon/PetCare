package com.example.petcare.fragments.consultion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.petcare.R;
import com.example.petcare.model.Blog;
import com.example.petcare.utility.ProgressDialogUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.Serializable;

public class BlogDetailFragment extends Fragment {
    private static final String ARG_BLOG = "blog";
    private Blog blog;
    private LinearLayout noDataLayout;
    private View contentLayout;

    public static BlogDetailFragment newInstance(Blog blog) {
        BlogDetailFragment fragment = new BlogDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BLOG, (Serializable) blog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            blog = (Blog) getArguments().getSerializable(ARG_BLOG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_detail, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        TextView blogTitle = view.findViewById(R.id.blogDetailTitle);
        TextView blogContent = view.findViewById(R.id.blogDetailContent);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        contentLayout = view.findViewById(R.id.contentLayout);

        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        if (blog != null) {
            ProgressDialogUtil.showProgressBar(requireContext(), true);
            blogTitle.setText("Title: "+ blog.getBlogTitle());
            blogContent.setText("Description: \n " +blog.getBlogText());
            updateNoDataVisibility(false);
            ProgressDialogUtil.showProgressBar(requireContext(), false);
        } else {
            updateNoDataVisibility(true);
        }
    }

    private void updateNoDataVisibility(boolean showNoData) {
        if (noDataLayout != null && contentLayout != null) {
            noDataLayout.setVisibility(showNoData ? View.VISIBLE : View.GONE);
            contentLayout.setVisibility(showNoData ? View.GONE : View.VISIBLE);
        }
    }
} 