package com.example.petcare.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.petcare.R;

public class ProgressDialog {
    private final Dialog dialog;
    private final LottieAnimationView lottieAnimation;
    private final TextView messageText;

    public ProgressDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        lottieAnimation = dialog.findViewById(R.id.lottieAnimation);
        messageText = dialog.findViewById(R.id.messageText);
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setMessage(String message) {
        messageText.setText(message);
    }
}