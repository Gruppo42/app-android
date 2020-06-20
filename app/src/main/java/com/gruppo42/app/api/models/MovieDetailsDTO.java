package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

public class MovieDetailsDTO {

    @SerializedName("poster_path")
    public String poster_path;

    public MovieDetailsDTO(){}

    public MovieDetailsDTO(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
