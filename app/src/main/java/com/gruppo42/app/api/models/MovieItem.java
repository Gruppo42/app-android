package com.gruppo42.app.api.models;

import java.util.ArrayList;
import java.util.List;


public class MovieItem
{
    private int id;
    private String title;
    private String year;
    private String language;
    private List<String> genre;
    private String imageUrl;
    private String imageUrlSecondary;

    public MovieItem(ResultDTO result)
    {
        this.id = result.id;
        this.title = result.getTitle();
        if(result.getRelease_date()!=null && result.getRelease_date().length()>4)
            this.year = result.release_date.substring(0, 4);
        this.language = result.getOriginal_language();
        this.genre = new ArrayList<>();
        if(result.getPoster_path()!=null)
            this.imageUrl = result.getPoster_path().substring(1);
        this.imageUrlSecondary = result.getBackdrop_path();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<String> getGenre() {
        return this.genre;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

}
