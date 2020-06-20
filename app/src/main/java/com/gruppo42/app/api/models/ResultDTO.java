package com.gruppo42.app.api.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

public class ResultDTO implements Parcelable {
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
    float vote_average;
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
                     float vote_average,
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
        this.vote_average = vote_average;
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

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected ResultDTO(Parcel in) {
        poster_path = in.readString();
        adult = in.readBoolean();
        overview = in.readString();
        release_date = in.readString();
        in.readIntArray(genre_ids);
        id = in.readInt();
        original_title = in.readString();
        original_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readFloat();
        vote_count = in.readInt();
        video = in.readBoolean();
        vote_average = in.readFloat();
    }

    @Override
    public int describeContents() { return 0; }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeBoolean(adult);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeIntArray(genre_ids);
        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeDouble(popularity);
        dest.writeInt(vote_count);
        dest.writeBoolean(video);
        dest.writeDouble(vote_average);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ResultDTO> CREATOR = new Parcelable.Creator<ResultDTO>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public ResultDTO createFromParcel(Parcel in) {
            return new ResultDTO(in);
        }

        @Override
        public ResultDTO[] newArray(int size) {
            return new ResultDTO[size];
        }
    };
}