package com.gruppo42.app.ui.profile.dialogs.editor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.gruppo42.app.api.models.AvailableResponse;
import com.gruppo42.app.api.models.LoginRequest;
import com.gruppo42.app.api.models.ProfileChangeRequest;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserApiResponse;
import com.gruppo42.app.databinding.EmailDialogBinding;
import com.gruppo42.app.ui.dialogs.ChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailDialog extends DialogFragment {

    private EmailDialogBinding binding;
    private String userToken;
    private ChangeListener onSuccessListener = null;
    private boolean emailAvailable;
    private UserApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        emailAvailable = false;
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        userToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkyNzk1ODg3LCJleHAiOjE1OTM0MDA2ODd9.HadRp2srca8WlO3VcVr1x5CLOT6i3USoYLO8HTZyjiHtenupH7BBkO7KV_7hznacTIDCQhWL6oHovvee5Nzkeg";//sharedPref.getString("user", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiaWF0IjoxNTkyNzkzMzg4LCJleHAiOjE1OTMzOTgxODh9.rBQkpdkGybnM2qK5G4c3ueGjkGW0zTfyM9Up42R8HY56_to1KJuOfh9gPRg2_IAO7Ymgul_5WCv9Za-QFazFJQ");
        api = UserApi.Instance.get();
        binding = EmailDialogBinding.inflate(inflater, container, false);
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        binding.emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Log.d("DEbug", "IN HERE");
                    if(binding.emailText.getText()!=null &&
                            binding.emailText.getText().length()>0 &&
                            android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailText.getText()).matches())
                        api.checkEmailAvailable(binding.emailText.getText().toString())
                                .enqueue(new Callback<AvailableResponse>() {
                                    @Override
                                    public void onResponse(Call<AvailableResponse> call, Response<AvailableResponse> response) {
                                        Log.d("DEbug", response.toString());
                                        if(response.isSuccessful())
                                        {
                                            if(response.body().isAvailable())
                                            {
                                                binding.email.setErrorEnabled(false);
                                                binding.email.setHelperTextEnabled(true);
                                                binding.email.setHelperText("Email not in use");
                                                emailAvailable = true;
                                            }
                                            else
                                            {
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
                    if(binding.emailText.getText()!=null && binding.emailText.getText().length()==0 ||
                            !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailText.getText()).matches())
                    {
                        binding.email.setHelperTextEnabled(false);
                        binding.email.setErrorEnabled(true);
                        binding.email.setError("Please enter a valid email!");
                    }
                    binding.email.setCounterEnabled(false);
                }
            }
        });

        return binding.getRoot();
    }

    private void sendRequest()
    {
        if((!TextUtils.isEmpty(binding.emailText.getText()) &&
                Patterns.EMAIL_ADDRESS.matcher(binding.emailText.getText().toString()).matches()) &&
                binding.emailText.getText().length()>7 &&
                emailAvailable)
        {
            api.changeProfileDetails(userToken,
                                     new ProfileChangeRequest(null, binding.emailText.getText().toString(), null))
                    .enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                            Log.d("Debug api", response.toString());
                            if(response.isSuccessful())
                            {
                                binding.email.setErrorEnabled(false);
                                binding.email.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                                binding.email.setHelperTextEnabled(true);
                                binding.email.setHelperText("Email changed successfully!");
                                if(onSuccessListener!=null)
                                    onSuccessListener.onChange(binding.emailText.getText().toString());
                            }
                            else
                            {
                                binding.email.setErrorEnabled(true);
                                binding.email.setError("Could not change email!");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserApiResponse> call, Throwable t) {
                            binding.email.setErrorEnabled(true);
                            binding.email.setError("Could not change email!");
                        }
                    });
        }
        else
        {
            binding.email.setErrorEnabled(true);
            binding.email.setError("Insert valid email");
        }
    }

    public void setOnSuccessListener(ChangeListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
