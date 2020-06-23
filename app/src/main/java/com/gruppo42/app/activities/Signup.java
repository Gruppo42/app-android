package com.gruppo42.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.AvailableResponse;
import com.gruppo42.app.api.models.SignUpRequest;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserApiResponse;
import com.gruppo42.app.databinding.ActivitySignupBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Max Username 20, Name 42
public class Signup extends AppCompatActivity {

    private UserApi api;
    private ActivitySignupBinding binding;
    private boolean usernameAvailable;
    private boolean emailAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usernameAvailable = false;
        api = UserApi.Instance.get();
        Log.d("Signup", "********************************created");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Glide.with(this)
                .asBitmap()
                .load(R.drawable.place_holder_profile)
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.GRAY))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(binding.profilePic);

        binding.emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d("DEbug", "IN HERE");
                    if (binding.emailText.getText() != null &&
                            binding.emailText.getText().length() > 0 &&
                            android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailText.getText()).matches())
                        api.checkEmailAvailable(binding.emailText.getText().toString())
                                .enqueue(new Callback<AvailableResponse>() {
                                    @Override
                                    public void onResponse(Call<AvailableResponse> call, Response<AvailableResponse> response) {
                                        Log.d("DEbug", response.toString());
                                        if (response.isSuccessful()) {
                                            if (response.body().isAvailable()) {
                                                binding.email.setErrorEnabled(false);
                                                binding.email.setHelperTextEnabled(true);
                                                binding.email.setHelperText("Email not in use");
                                                emailAvailable = true;
                                            } else {
                                                binding.email.setHelperTextEnabled(false);
                                                binding.email.setErrorEnabled(true);
                                                binding.email.setError("Email already in use!");
                                                emailAvailable = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AvailableResponse> call, Throwable t) {
                                        emailAvailable = false;
                                    }
                                });
                    if (binding.emailText.getText() != null && binding.emailText.getText().length() == 0 ||
                            !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailText.getText()).matches()) {
                        binding.email.setHelperTextEnabled(false);
                        binding.email.setErrorEnabled(true);
                        binding.email.setError("Please enter a valid email!");
                    }
                    binding.email.setCounterEnabled(false);
                }
            }
        });

        binding.nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.name.setCounterEnabled(true);
                else {
                    binding.name.setCounterEnabled(false);
                    binding.name.setHelperTextEnabled(false);
                    binding.name.setErrorEnabled(false);
                }
            }
        });
        binding.usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.username.setCounterEnabled(true);
                else {
                    //Controllo integrita'
                    if (binding.usernameText.getText() != null &&
                            binding.usernameText.getText().length() > 0 &&
                            binding.usernameText.getText().length() <= 20)
                        api.checkUsernameAvailable(binding.usernameText.getText().toString())
                                .enqueue(new Callback<AvailableResponse>() {
                                    @Override
                                    public void onResponse(Call<AvailableResponse> call, Response<AvailableResponse> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().isAvailable()) {
                                                binding.username.setErrorEnabled(false);
                                                binding.username.setHelperTextEnabled(true);
                                                binding.username.setHelperText("Username available");
                                                usernameAvailable = true;
                                            } else {
                                                binding.username.setHelperTextEnabled(false);
                                                binding.username.setErrorEnabled(true);
                                                binding.username.setError("Username not available!");
                                                usernameAvailable = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AvailableResponse> call, Throwable t) {
                                        usernameAvailable = false;
                                    }
                                });
                    if (binding.usernameText.getText() != null && binding.usernameText.getText().length() == 0) {
                        binding.username.setHelperTextEnabled(false);
                        binding.username.setErrorEnabled(true);
                        binding.username.setError("Please enter a valid username!");
                    }
                    binding.username.setCounterEnabled(false);
                }
            }
        });

        binding.BTNSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        setContentView(view);
    }

    public void registerUser() {
        if (binding.nameText.getText().length() < 3) {
            binding.name.setError("Name must have at least 3 characters!");
            binding.name.setHelperTextEnabled(false);
            binding.name.setErrorEnabled(true);
            return;
        }
        if (!emailAvailable) {
            binding.email.setError("Email taken!");
            binding.email.setHelperTextEnabled(false);
            binding.email.setErrorEnabled(true);
            return;
        }
        if (!usernameAvailable) {
            binding.username.setError("Username taken!");
            binding.username.setHelperTextEnabled(false);
            binding.username.setErrorEnabled(true);
            return;
        }
        if (!(binding.usernameText.getText() != null &&
                binding.usernameText.getText().length() > 0 &&
                binding.usernameText.getText().length() <= 20)) {
            binding.username.setError("Username must be between 1 and 20 characters!");
            binding.username.setHelperTextEnabled(false);
            binding.username.setErrorEnabled(true);
            return;
        }
        if (binding.emailText.getText() == null ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailText.getText()).matches()) {
            binding.email.setError("Insert a valid email!");
            binding.email.setHelperTextEnabled(false);
            binding.email.setErrorEnabled(true);
            return;
        }
        binding.password.setHelperTextEnabled(false);
        binding.password.setErrorEnabled(false);
        if (binding.passwordText.getText().length() < 6) {
            binding.password.setError("Password must have at least 6 characters!");
            binding.password.setHelperTextEnabled(false);
            binding.password.setErrorEnabled(true);
            return;
        }
        Log.d("Signup", "**********************Sending signup request!******************");
        api.signupUser(new SignUpRequest(binding.nameText.getText().toString(),
                binding.usernameText.getText().toString(),
                binding.emailText.getText().toString(),
                "",
                binding.passwordText.getText().toString()))
                .enqueue(new Callback<UserApiResponse>() {
                    @Override
                    public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                        Log.d("Signup", response.toString());
                        if (response.isSuccessful()) {
                            if (response.body().isSuccess())
                                Log.d("Signup", "User created!");
                            else
                                Log.d("Signup", "User creation failed!");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserApiResponse> call, Throwable t) {
                        Log.d("Signup", "User creation failed!");
                    }
                });
        usernameAvailable = false;
    }

    public void openSignIn(View view) {
        finish();
    }
}