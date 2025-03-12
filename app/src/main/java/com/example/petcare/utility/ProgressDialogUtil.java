package com.example.petcare.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.petcare.R;

public class ProgressDialogUtil {

    private static AlertDialog alertDialog;

    public static void showProgressBar(Context context, boolean toShow) {
        if (!(context instanceof Activity) || ((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
            return; // Exit if context is not valid
        }

        if (toShow) {
            if (alertDialog == null) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER);
                layout.setBackgroundColor(Color.TRANSPARENT);

                LottieAnimationView lottieAnimationView = new LottieAnimationView(context);
                lottieAnimationView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                lottieAnimationView.setAnimation(R.raw.dog_walking_anim); // Make sure this file exists in res/raw/
                lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                lottieAnimationView.playAnimation();

                layout.addView(lottieAnimationView);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setView(layout);

                alertDialog = builder.create();

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }

            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        } else {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }
}
