package com.gruppo42.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.gruppo42.app.databinding.ActivitySignUpSuccessBinding;

public class SignUpSuccess extends AppCompatActivity {

    private ActivitySignUpSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySignUpSuccessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.secondaryAnimationView.loop(true);
        binding.secondaryAnimationView.playAnimation();
    }
}