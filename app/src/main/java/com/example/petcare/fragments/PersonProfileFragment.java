package com.example.petcare.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petcare.PetDetailsActivity;
import com.example.petcare.R;


public class PersonProfileFragment extends Fragment {


    CardView cardView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_profile, container, false);

        cardView = view.findViewById(R.id.addpetprofile);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PetDetailsActivity.class);
                startActivity(i);
            }
        });
        
        return view;

    }
}