package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

public class AvailableResponse {

    @SerializedName("available")
    boolean available;

    public AvailableResponse(){
    }

    public AvailableResponse(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
