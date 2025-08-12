package com.example.notes_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.window.SplashScreen;

import com.example.notes_app.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_screen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_animation);
        binding.splashscreenTxtview.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentuser==null){
                    Intent i = new Intent(splash_screen.this, login_screen.class);
                    startActivity(i);
                }else {
                    startActivity(new Intent(splash_screen.this, MainActivity.class));
                }

                finish();
            }
        },9000);
    }
}