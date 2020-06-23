package com.gruppo42.app.activities;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.gruppo42.app.R;
import com.gruppo42.app.databinding.ActivitySplashScreenBinding;
import com.gruppo42.app.session.SessionManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2000;
    private ActivitySplashScreenBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sessionManager = new SessionManager(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(SplashScreenActivity.this, Login.class);
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(binding.animationView, "logo");
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(SplashScreenActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_SCREEN);
    }
}