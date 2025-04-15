package com.example.petcare.utility;

import android.widget.ImageView;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("imageResId")
    public static void setImageResource(ImageView view, int resId) {
        view.setImageResource(resId);
    }
}
