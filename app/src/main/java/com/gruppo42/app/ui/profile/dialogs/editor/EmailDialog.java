package com.gruppo42.app.ui.profile.dialogs.editor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

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

        return binding.getRoot();
    }

    private void sendRequest()
    {
        if((!TextUtils.isEmpty(binding.editText.getText()) && Patterns.EMAIL_ADDRESS.matcher(binding.editText.getText().toString()).matches()) && binding.editText.getText().length()>7)
        {
            api.changeProfileDetails(userToken,
                                     new ProfileChangeRequest(null, binding.editText.getText().toString(), null))
                    .enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                            Log.d("Debug api", response.toString());
                            if(response.isSuccessful())
                            {
                                binding.status.setTextColor(Color.GREEN);
                                binding.status.setText("Email changed successfully!");
                                if(onSuccessListener!=null)
                                    onSuccessListener.onChange(binding.editText.getText().toString());
                            }
                            else
                            {
                                binding.status.setTextColor(Color.RED);
                                binding.status.setText("Could not change email!");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserApiResponse> call, Throwable t) {
                            binding.status.setTextColor(Color.RED);
                            binding.status.setText("Could not change email!");
                        }
                    });
        }
        else
        {
            binding.status.setTextColor(Color.RED);
            binding.status.setText("Insert valid email!");
        }
    }

    public void setOnSuccessListener(ChangeListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
