package com.example.petcare.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
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
        
        // Set dialog properties
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        lottieAnimation = dialog.findViewById(R.id.lottieAnimation);
        messageText = dialog.findViewById(R.id.messageText);
        
        // Set default message
        setMessage("Analyzing disease...");
    }

    public void show() {
        try {
            if (!dialog.isShowing()) {
                dialog.show();
                // Start Lottie animation
                if (lottieAnimation != null) {
                    lottieAnimation.playAnimation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (dialog.isShowing()) {
                // Stop Lottie animation
                if (lottieAnimation != null) {
                    lottieAnimation.pauseAnimation();
                }
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
        if (messageText != null && message != null) {
            messageText.setText(message);
        }
    }
    
    public boolean isShowing() {
        return dialog.isShowing();
    }
}