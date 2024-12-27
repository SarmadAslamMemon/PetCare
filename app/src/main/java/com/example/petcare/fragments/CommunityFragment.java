package com.example.petcare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petcare.GeneralClass;
import com.example.petcare.R;
import com.example.petcare.adapter.DoctorAdapter;
import com.example.petcare.modelclass.Doctor;

import java.util.List;


public class CommunityFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        getViews(view);
        setRecyclerView();
        return view;

    }

    private void setRecyclerView() {
        List<Doctor> doctorList = GeneralClass.getDoctors();
        DoctorAdapter adapter = new DoctorAdapter(requireContext(), doctorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

    }

    private void getViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerDoctorList);

    }
}