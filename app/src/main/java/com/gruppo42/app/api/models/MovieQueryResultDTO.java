package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieQueryResultDTO {
    @SerializedName("page")
    int page;
    @SerializedName("total_results")
    int total_results;
    @SerializedName("total_pages")
    int total_pages;
    @SerializedName("results")
    List<ActorDTO> results;

    public MovieQueryResultDTO() {
    }

    public MovieQueryResultDTO(int page,
                          int total_results,
                          int total_pages,
                          List<ActorDTO> results) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<ActorDTO> getResults() {
        return results;
    }

    public void setResults(List<ActorDTO> results) {
        this.results = results;
    }

    public class ActorDTO
    {
        @SerializedName("profile_path")
        private String profile_path;

        public String getProfile_path() {
            return profile_path;
        }

        public void setProfile_path(String profile_path) {
            this.profile_path = profile_path;
        }
    }
}
