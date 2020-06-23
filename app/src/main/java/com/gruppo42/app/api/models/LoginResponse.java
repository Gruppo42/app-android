package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("tokenType")
    private String tokenType;

    public LoginResponse(){

    }

    public LoginResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
