package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

public class MovieDetailsDTO {

    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("release_date")
    private  String release_date;
    @SerializedName("imdb_id")
    private String imdb_id;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("runtime")
    private String runtime;


    public MovieDetailsDTO(){}

    public MovieDetailsDTO(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
