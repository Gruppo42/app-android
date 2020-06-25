package com.gruppo42.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import com.gruppo42.app.api.models.LoginRequest;
import com.gruppo42.app.api.models.LoginResponse;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.databinding.ActivityLoginBinding;
import com.gruppo42.app.session.SessionManager;
import com.gruppo42.app.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserApi api;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = UserApi.Instance.get();
        sessionManager = new SessionManager(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    binding.username.setHelperTextEnabled(false);
                    binding.password.setErrorEnabled(false);
                }
            }
        });
        binding.usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    binding.username.setHelperTextEnabled(false);
                    binding.password.setErrorEnabled(false);
                }
            }
        });
        binding.enterguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.usernameText.getText().length()==0)
                {
                    binding.username.setHelperTextEnabled(true);
                    binding.username.setHelperText("Insert a username or password");
                    return;
                }
                if(binding.passwordText.getText().length()==0)
                {
                    binding.password.setHelperTextEnabled(true);
                    binding.password.setHelperText("Insert a password");
                    return;
                }
                binding.animationView.playAnimation();
                binding.animationView.loop(true);
                api.signinUser(new LoginRequest(binding.usernameText.getText().toString(),
                                                binding.passwordText.getText().toString()))
                        .enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                binding.animationView.pauseAnimation();
                                binding.animationView.loop(false);
                                if(response.isSuccessful())
                                {
                                    sessionManager.createLoginSession(response.body().getAccessToken());
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    binding.usernameText.getText().clear();
                                    binding.passwordText.getText().clear();
                                    startActivity(intent);
                                }
                                else {
                                    binding.password.setErrorEnabled(true);
                                    binding.password.setError("Wrong credentials!");
                                    binding.authContainer.startAnimation(Utils.shakeError());
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                binding.animationView.pauseAnimation();
                                binding.animationView.loop(false);
                            }
                        });
            }
        });
        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String>(binding.animationView, "logo");
                pairs[1] = new Pair<View, String>(binding.forgotPassword, "title");
                pairs[2] = new Pair<View, String>(binding.username, "editMail");
                pairs[3] = new Pair<View, String>(binding.login, "loginBTN");
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
        setContentView(view);
    }

    public void openSignup(View view) {
        binding.password.setErrorEnabled(false);
        binding.username.setErrorEnabled(false);
        binding.password.setHelperTextEnabled(false);
        binding.username.setHelperTextEnabled(false);
        binding.usernameText.getText().clear();
        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
    }

}