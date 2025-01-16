package com.example.petcare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.petcare.R;
import com.example.petcare.modelclass.Blog;


public class BlogFragment extends Fragment {

    TextView headingText, authorText, dateText, blogHeading, blogContent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blog, container, false);

        headingText = v.findViewById(R.id.headingText);
        blogContent = v.findViewById(R.id.blogContent);




        Bundle bundle = getArguments();
        if (bundle != null) {
            Blog blog = (Blog) bundle.getSerializable("blog");
            if (blog != null) {
                headingText.setText(blog.getBlogTitle());
                blogContent.setText(Html.fromHtml(blog.getBlogText(), Html.FROM_HTML_MODE_LEGACY));
            }
        }

        return  v;
    }
}