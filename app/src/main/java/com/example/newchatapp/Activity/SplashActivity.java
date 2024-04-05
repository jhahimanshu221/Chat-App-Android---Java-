package com.example.newchatapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newchatapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        new Handler().postDelayed(() -> {
            // Check if there is an authenticated user
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                // User is already logged in, launch MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish(); // Finish SplashActivity
            } else {
                // No authenticated user, proceed with checking signup status
                SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
                boolean signupCompleted = preferences.getBoolean("SIGNUP_COMPLETED", false);

                if (signupCompleted) {
                    // Signup is completed, launch MainActivity
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    // Signup is not completed, launch LoginActivity or any other initial activity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish(); // Finish SplashActivity
            }
        }, 5000);

    }
}