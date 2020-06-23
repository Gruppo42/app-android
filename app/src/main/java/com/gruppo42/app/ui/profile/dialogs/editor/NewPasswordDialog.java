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

import com.gruppo42.app.api.models.PasswordChangeRequest;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserApiResponse;
import com.gruppo42.app.databinding.PasswordDialogBinding;
import com.gruppo42.app.session.SessionManager;
import com.gruppo42.app.ui.dialogs.ChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordDialog extends DialogFragment {

    private UserApi api;
    private PasswordDialogBinding binding;
    private ChangeListener onSuccessListener = null;
    private SessionManager sessionManager;
    private boolean callApi = false;
    private String title;

    public NewPasswordDialog()
    {
        super();
        this.title = "Change password";
    }

    public NewPasswordDialog(String title)
    {
        super();
        this.title = title;
    }

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
        binding = PasswordDialogBinding.inflate(inflater, container, false);
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callApi)
                    sendRequest();
                else
                {
                    if(onSuccessListener!=null)
                        onSuccessListener.onChange(binding.passwordText.getText().toString());
                }
            }
        });
        binding.textView7.setText(title);
        return binding.getRoot();
    }

    public void sendRequest()
    {
        if(binding.passwordText.toString().length()>=6) {
            api.changePassword(sessionManager.getUserAuthorization(),
                    new PasswordChangeRequest(binding.passwordText.getText().toString(),
                            binding.passwordText.getText().toString()))
                    .enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                            Log.d("Debug api", response.toString());

                            if (response.isSuccessful()) {
                                binding.password.setErrorEnabled(false);
                                binding.password.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                                binding.password.setHelperTextEnabled(true);
                                binding.password.setHelperText("Password changed!");
                                if (onSuccessListener != null)
                                    onSuccessListener.onChange(null);
                            } else {
                                binding.password.setErrorEnabled(true);
                                binding.password.setError("Password needs to be at least 6 characters!");
                            }
                        }
                        @Override
                        public void onFailure(Call<UserApiResponse> call, Throwable t) {
                        }
                    });
        }
        else
        {
            binding.password.setHelperTextEnabled(false);
            binding.password.setErrorEnabled(true);
            binding.password.setError("Password needs to be at least 6 characters!");
        }
    }

    public void setOnSuccessListener(ChangeListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public void setCallApi(boolean callApi) {
        this.callApi = callApi;
    }

    public PasswordDialogBinding getBinding() {
        return binding;
    }
}
