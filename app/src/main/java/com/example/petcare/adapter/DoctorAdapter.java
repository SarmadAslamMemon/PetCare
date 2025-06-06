package com.example.petcare.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.R;
import com.example.petcare.fragments.ConsultationFragment;
import com.example.petcare.fragments.consultion.DoctorDetailsBottomSheet;
import com.example.petcare.modelclass.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private final Context context;
    private final List<Doctor> doctorList;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctors_list_layout_item, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.doctorNameTextView.setText(doctor.getName());
        holder.doctorHospitalTextView.setText(doctor.getHospital());
        holder.doctorImageView.setImageResource(doctor.getImageResId());


        holder.itemView.setOnClickListener(view -> {
           showBottomSheet();
        });
        // Set click listener for the call icon
        holder.callDoctorIc.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + doctor.getCallNumber()));
            context.startActivity(intent);
        });


        holder.itemView.setOnClickListener(view -> {
            DoctorDetailsBottomSheet bottomSheet = new DoctorDetailsBottomSheet(doctor);
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            bottomSheet.show(activity.getSupportFragmentManager(), "doctor_details");
        }

        });

    }

    private void showBottomSheet() {

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView doctorNameTextView, doctorHospitalTextView;
        Button callDoctorIc;
        ImageView doctorImageView;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            doctorHospitalTextView = itemView.findViewById(R.id.docterHospitalTextView);
            callDoctorIc = itemView.findViewById(R.id.callDoctorIc);
            doctorImageView = itemView.findViewById(R.id.doctorImageView);
        }
    }
}
