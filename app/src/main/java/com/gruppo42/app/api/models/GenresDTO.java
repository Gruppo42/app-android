package com.gruppo42.app.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class GenresDTO
{
    @SerializedName("genres")
    public ArrayList<Genre> genres;

    public GenresDTO()
    {

    }

    public GenresDTO(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genre) {
        this.genres = genre;
    }

    public class Genre
    {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;

        public Genre(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public Genre(){
        }

        @Override
        public String toString()
        {
            return  this.name;
        }
    }
}


