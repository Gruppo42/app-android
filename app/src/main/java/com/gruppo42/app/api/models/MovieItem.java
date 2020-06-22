package com.gruppo42.app.api.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.gruppo42.app.utils.HashMapGenres;

import java.util.ArrayList;
import java.util.List;


public class MovieItem implements Parcelable
{
    private int id;
    private String title;
    private String date;
    private String year;
    private String language;
    private List<String> genre;
    private int[] genre_ids;
    private String imageUrl;
    private String imageUrlSecondary;
    private String description;

    public MovieItem(ResultDTO result)
    {
        this.id = result.id;
        this.title = result.getTitle();
        this.date = result.getRelease_date();
        if(result.getRelease_date()!=null && result.getRelease_date().length()>4)
            this.year = result.release_date.substring(0, 4);
        this.language = result.getOriginal_language();
        this.genre = new ArrayList<>();
        this.genre_ids = result.getGenre_ids();
        if(result.getPoster_path() != null) {
            this.imageUrl = result.getPoster_path();//.substring(1);
        }
        if(result.getBackdrop_path() != null) {
            this.imageUrlSecondary = result.getBackdrop_path();
        }
        this.description = result.getOverview();
    }

    public MovieItem(String title, String year,
                    String language,
                    List<String> genre, String imageUrl) {
        this.title = title;
        this.year = year;
        this.language = language;
        this.genre = genre;
        this.imageUrl = imageUrl;;
    }

    public MovieItem(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlSecondary() {
        return imageUrlSecondary;
    }

    public void setImageUrlSecondary(String imageUrlSecondary) {
        this.imageUrlSecondary = imageUrlSecondary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrGenres() {

        String genres = "";
        HashMapGenres hashMapGenres = new HashMapGenres();
        for (int i = 0; i < this.getGenre_ids().length; i++) {
            genres += hashMapGenres.getIdGenresToGenres().get(this.getGenre_ids()[i]);
            genres += ", ";
        }
        if (genres.length() != 0) {
            genres = genres.substring(0, genres.length() - 2);
        } else {
            genres = "No genres available";
        }

        return genres;
    }

    protected MovieItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        year = in.readString();
        language = in.readString();
        in.readStringList(genre);
        in.readIntArray(genre_ids);
        imageUrl = in.readString();
        imageUrlSecondary = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(year);
        dest.writeString(language);
        dest.writeStringList(genre);
        dest.writeIntArray(genre_ids);
        dest.writeString(imageUrl);
        dest.writeString(imageUrlSecondary);
        dest.writeString(description);
    }

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
