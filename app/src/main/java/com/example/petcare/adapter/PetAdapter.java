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
import com.example.petcare.modelclass.Pet;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private List<Pet> pets;
    private OnPetClickListener listener;

    public interface OnPetClickListener {
        void onPetClick(Pet pet);
    }

    public PetAdapter(List<Pet> pets, OnPetClickListener listener) {
        this.pets = pets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = pets.get(position);
        holder.bind(pet);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public void updatePets(List<Pet> newPets) {
        this.pets = newPets;
        notifyDataSetChanged();
    }

    class PetViewHolder extends RecyclerView.ViewHolder {
        private ImageView petImage;
        private TextView petName;
        private TextView petCategory;
        private TextView petBreed;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.petImage);
            petName = itemView.findViewById(R.id.petName);
            petCategory = itemView.findViewById(R.id.petCategory);
            petBreed = itemView.findViewById(R.id.petBreed);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onPetClick(pets.get(position));
                }
            });
        }

        public void bind(Pet pet) {
            petName.setText(pet.getPetName());
            petCategory.setText(pet.getPetCategory());
            petBreed.setText(pet.getPetBreed());

            int categoryDrawableRes = R.drawable.pets_foot_ic;

            switch (pet.getPetCategory().toLowerCase()) {
                case "dog":
                    categoryDrawableRes = R.drawable.dog_svgrepo_com;
                    break;
                case "cat":
                    categoryDrawableRes = R.drawable.cat;
                    break;
                case "fish":
                    categoryDrawableRes = R.drawable.fish_svgrepo;
                    break;
                case "rabbit":
                    categoryDrawableRes = R.drawable.rabbit_easter_svgrepo_com;
                    break;
                case "parrot":
                    categoryDrawableRes = R.drawable.parrot_svgrepo_com;
                    break;
            }

            // Load pet image using Glide
            if (pet.getPetImageUrl() != null && !pet.getPetImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(pet.getPetImageUrl())
                        .placeholder(categoryDrawableRes)
                        .error(categoryDrawableRes)
                        .into(petImage);
            } else {
                petImage.setImageResource(categoryDrawableRes);
            }
        }

    }
} 