package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

public class UserApiResponse {
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public UserApiResponse()
    {
    }

    public UserApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
