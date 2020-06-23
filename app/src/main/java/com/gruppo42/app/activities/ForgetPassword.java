package com.gruppo42.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.gruppo42.app.R;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserApiResponse;
import com.gruppo42.app.databinding.ActivityForgetPasswordBinding;
import com.gruppo42.app.databinding.ActivitySplashScreenBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPassword extends AppCompatActivity {

    private ActivityForgetPasswordBinding binding;
    private UserApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = UserApi.Instance.get();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    binding.email.setHelperTextEnabled(false);
                    binding.email.setErrorEnabled(false);
                }
            }
        });
        binding.resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.usernameText.getText() != null && binding.usernameText.getText().length() == 0 ||
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.usernameText.getText()).matches()) {
                    binding.email.setHelperTextEnabled(false);
                    binding.email.setErrorEnabled(true);
                    binding.email.setError("Please enter a valid email!");
                }
                else {
                    Log.d("DEBUG", "CALLING API");
                    binding.animationView.playAnimation();
                    binding.animationView.loop(true);
                    api.resetPassword(binding.usernameText.getText().toString())
                            .enqueue(new Callback<UserApiResponse>() {
                                @Override
                                public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                                    if (response.isSuccessful() && response.body().isSuccess()) {
                                        Log.d("DEBUG", "HELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                                        binding.animationView.pauseAnimation();
                                        binding.animationView.loop(false);
                                        binding.email.setErrorEnabled(false);
                                        binding.email.setHelperTextEnabled(true);
                                        binding.email.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                                        binding.email.setHelperText("An email has been sent to " + binding.usernameText.getText().toString());
                                    } else
                                        onFailure(call, new Throwable());
                                }

                                @Override
                                public void onFailure(Call<UserApiResponse> call, Throwable t) {
                                    binding.animationView.pauseAnimation();
                                    binding.animationView.loop(false);
                                    binding.email.setHelperTextEnabled(false);
                                    binding.email.setErrorEnabled(true);
                                    binding.email.setError("Please enter a valid email!");
                                }
                            });
                }
            }
        });
    }
}