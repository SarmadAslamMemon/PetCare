package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    private Button nextBtnTwo, nextBtnOne, finishBtnThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        showScreenTwo(); // Start with screen two (shuffling)
    }

    private void showScreenTwo() {
        setContentView(R.layout.welcome_screen_two);

        nextBtnTwo = findViewById(R.id.nextBtnTwo);
        nextBtnTwo.setOnClickListener(v -> showScreenOne()); // From screen two to screen one
    }

    private void showScreenOne() {
        setContentView(R.layout.welcome_screen_one);

        nextBtnOne = findViewById(R.id.nextBtnOne);
        nextBtnOne.setOnClickListener(v -> showScreenThree()); // From screen one to screen three
    }

    private void showScreenThree() {
        setContentView(R.layout.welcome_screen_three);
        finishBtnThree = findViewById(R.id.finishBtnThree);
        finishBtnThree.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeScreen.this, RegistrationActivity.class);
            startActivity(intent);
            finish(); // End this activity so the user can't go back to the welcome screens
        });
    }

    @Override
    public void onBackPressed() {
        // Handle back navigation between screens
        if (getCurrentFocus() != null) {
            if (getCurrentFocus().getId() == R.id.finishBtnThree) {
             Intent i = new Intent(WelcomeScreen.this, RegistrationActivity.class);
             startActivity(i);
            } else if (getCurrentFocus().getId() == R.id.nextBtnOne) {
                showScreenTwo(); // Navigate back to screen two
            } else if (getCurrentFocus().getId() == R.id.nextBtnTwo) {
                showScreenOne(); // Navigate back to screen one
            }
        }
    }
}
