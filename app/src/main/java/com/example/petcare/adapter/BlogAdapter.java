package com.example.petcare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petcare.R;
import com.example.petcare.model.Blog;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private List<Blog> blogs;
    private OnBlogClickListener listener;

    public interface OnBlogClickListener {
        void onBlogClick(Blog blog);
    }

    public BlogAdapter(List<Blog> blogs, OnBlogClickListener listener) {
        this.blogs = blogs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = blogs.get(position);
        holder.bind(blog);
    }

    @Override
    public int getItemCount() {
        return blogs != null ? blogs.size() : 0;
    }

    public void updateBlogs(List<Blog> newBlogs) {
        this.blogs = newBlogs;
        notifyDataSetChanged();
    }

    class BlogViewHolder extends RecyclerView.ViewHolder {
        private TextView blogTitle;
        private TextView authorName;
        private TextView blogPreview;

        BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blogTitle);
            authorName = itemView.findViewById(R.id.authorName);
            blogPreview = itemView.findViewById(R.id.blogPreview);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBlogClick(blogs.get(position));
                }
            });
        }

        void bind(Blog blog) {
            blogTitle.setText(blog.getBlogTitle());
            authorName.setText(blog.getAuthorName());
            blogPreview.setText(blog.getBlogText());
        }
    }
} 