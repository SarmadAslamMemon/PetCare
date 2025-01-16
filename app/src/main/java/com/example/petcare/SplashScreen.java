package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieCompositionFactory;
import com.example.petcare.activity.MainDashBoardActivity;
import com.example.petcare.utility.SharePreference;

public class SplashScreen extends AppCompatActivity {

    SharePreference sharePreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        sharePreference = new SharePreference(this);




        new Handler().postDelayed(() -> {
            if(!sharePreference.getUserRegistered()) {
                startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
            }else{
                startActivity(new Intent(SplashScreen.this, MainDashBoardActivity.class));
            }
            }, 3000);
    }

}