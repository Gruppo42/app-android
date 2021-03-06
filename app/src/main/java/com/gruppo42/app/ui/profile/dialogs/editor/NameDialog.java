package com.gruppo42.app.ui.profile.dialogs.editor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.gruppo42.app.api.models.ProfileChangeRequest;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserApiResponse;
import com.gruppo42.app.databinding.NameDialogBinding;
import com.gruppo42.app.session.SessionManager;
import com.gruppo42.app.ui.dialogs.ChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NameDialog extends DialogFragment {

    private NameDialogBinding binding;
    private UserApi api;
    private ChangeListener onSuccessListener = null;
    private SessionManager sessionManager;

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
        sessionManager = new SessionManager(getContext());
        api = UserApi.Instance.get();
        binding = NameDialogBinding.inflate(inflater, container, false);
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        return binding.getRoot();
    }

    private void sendRequest() {
        if (binding.editText.getText().length() >= 3) {
            api.changeProfileDetails(sessionManager.getUserAuthorization(),
                    new ProfileChangeRequest(binding.editText.getText().toString(), null, null))
                    .enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                            if (response.isSuccessful()) {
                                binding.name.setErrorEnabled(false);
                                binding.name.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                                binding.name.setHelperTextEnabled(true);
                                binding.name.setHelperText("Name changed successfully!");
                                if (onSuccessListener != null)
                                    onSuccessListener.onChange(binding.editText.getText().toString());
                            } else {
                                binding.name.setErrorEnabled(true);
                                binding.name.setError("Could not change name!");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserApiResponse> call, Throwable t) {
                            binding.name.setErrorEnabled(true);
                            binding.name.setError("Could not change name!");
                        }
                    });
        } else {
            binding.name.setErrorEnabled(true);
            binding.name.setError("Invalid name!");
        }
    }

    public void setOnSuccessListener(ChangeListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
