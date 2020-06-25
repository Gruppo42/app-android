package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReccQueryResultDTO {

    @SerializedName("page")
    private String page;
    @SerializedName("results")
    private List<ReccomendedMovieDTO> results;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<ReccomendedMovieDTO> getResults() {
        return results;
    }

    public void setResults(List<ReccomendedMovieDTO> results) {
        this.results = results;
    }

    public class ReccomendedMovieDTO {
        @SerializedName("id")
        private String id;
        @SerializedName("original_title")
        private String original_title;
        @SerializedName("poster_path")
        private String poster_path;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }
    }
}
