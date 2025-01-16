package com.example.petcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petcare.R;
import com.example.petcare.modelclass.Blog;

import java.util.ArrayList;
import java.util.List;

public class CorouselSliderAdapter extends RecyclerView.Adapter<CorouselSliderAdapter.ViewHolder> {
    Context context;
    List<Blog> arrayList;
    OnItemClickListener onItemClickListener;

    public CorouselSliderAdapter(Context context, ArrayList<Blog> arrayList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_blogs_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog = arrayList.get(position);

        // Creating a list of resource IDs for images
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.raw.raw_res_1);
        images.add(R.raw.raw_res_2);
        images.add(R.raw.raw_res_3);
        images.add(R.raw.raw_res_4);

        // Check if the position is within bounds of the images list
        if (position < images.size()) {
            holder.imageView.setImageResource(images.get(position));
        } else {
            Glide.with(context).load("https://images.pexels.com/photos/45170/kittens-cat-cat-puppy-rush-45170.jpeg").into(holder.imageView);
        }

        holder.imageView.setOnClickListener(v -> onItemClickListener.onClick(holder.imageView, blog));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView blogTitle, blogContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_image);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(ImageView imageView, Blog blog);
    }
}
