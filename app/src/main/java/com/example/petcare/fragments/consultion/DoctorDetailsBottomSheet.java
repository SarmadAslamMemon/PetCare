package com.example.petcare.fragments.consultion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.example.petcare.R;
import com.example.petcare.databinding.FragmentDoctorDetailsBottomSheetBinding;
import com.example.petcare.modelclass.Doctor;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DoctorDetailsBottomSheet extends BottomSheetDialogFragment {

    Doctor doctor;

    public DoctorDetailsBottomSheet(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentDoctorDetailsBottomSheetBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_doctor_details_bottom_sheet,
                container,
                false
        );

        binding.setDoctor(doctor);

        // simple click
        binding.callButton.setOnClickListener(v -> {
            String phone = doctor.getCallNumber(); // replace with your getter
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            requireContext().startActivity(intent);
        });

        return binding.getRoot();
    }

}
