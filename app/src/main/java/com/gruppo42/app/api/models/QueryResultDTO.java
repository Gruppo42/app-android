package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class QueryResultDTO {
    @SerializedName("page")
    int page;
    @SerializedName("total_results")
    int total_results;
    @SerializedName("total_pages")
    int total_pages;
    @SerializedName("results")
    List<ResultDTO> results;

    public QueryResultDTO() {
    }

    public QueryResultDTO(int page,
                          int total_results,
                          int total_pages,
                          List<ResultDTO> results) {
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

    public List<ResultDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultDTO> results) {
        this.results = results;
    }

    public class ResultDTO {
        @SerializedName("popularity")
        float popularity;
        @SerializedName("vote_count")
        int vote_count;
        @SerializedName("video")
        boolean video;
        @SerializedName("poster_path")
        String poster_path;
        @SerializedName("id")
        int id;
        @SerializedName("adult")
        boolean adult;
        @SerializedName("backdrop_path")
        String backdrop_path;
        @SerializedName("original_language")
        String original_language;
        @SerializedName("original_title")
        String original_title;
        @SerializedName("genre_ids")
        int[] genre_ids;
        @SerializedName("title")
        String title;
        @SerializedName("vote_average")
        float vote_avergae;
        @SerializedName("overview")
        String overview;
        @SerializedName("release_date")
        String release_date;

        public ResultDTO(float popularity,
                         int vote_count,
                         boolean video,
                         String poster_path,
                         int id,
                         boolean adult,
                         String backdrop_path,
                         String original_language,
                         String original_title,
                         int[] genre_ids,
                         String title,
                         float vote_avergae,
                         String overview,
                         String release_date) {
            this.popularity = popularity;
            this.vote_count = vote_count;
            this.video = video;
            this.poster_path = poster_path;
            this.id = id;
            this.adult = adult;
            this.backdrop_path = backdrop_path;
            this.original_language = original_language;
            this.original_title = original_title;
            this.genre_ids = genre_ids;
            this.title = title;
            this.vote_avergae = vote_avergae;
            this.overview = overview;
            this.release_date = release_date;
        }

        public ResultDTO() {
        }

        public float getPopularity() {
            return popularity;
        }

        public void setPopularity(float popularity) {
            this.popularity = popularity;
        }

        public int getVote_count() {
            return vote_count;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isAdult() {
            return adult;
        }

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public int[] getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(int[] genre_ids) {
            this.genre_ids = genre_ids;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public float getVote_avergae() {
            return vote_avergae;
        }

        public void setVote_avergae(float vote_avergae) {
            this.vote_avergae = vote_avergae;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }
    }
}
