package com.gruppo42.app.utils;

import java.util.HashMap;

public class HashMapGenres {
    private HashMap<Integer, String> idGenresToGenres;

    public HashMapGenres() {
        this.idGenresToGenres = new HashMap<Integer, String>();
        this.idGenresToGenres.put(28, "Action");
        this.idGenresToGenres.put(12, "Adventure");
        this.idGenresToGenres.put(16, "Animation");
        this.idGenresToGenres.put(35, "Comedy");
        this.idGenresToGenres.put(80, "Crime");
        this.idGenresToGenres.put(99, "Documentary");
        this.idGenresToGenres.put(18, "Drama");
        this.idGenresToGenres.put(10751, "Family");
        this.idGenresToGenres.put(14, "Fantasy");
        this.idGenresToGenres.put(36, "History");
        this.idGenresToGenres.put(27, "Horror");
        this.idGenresToGenres.put(10402, "Music");
        this.idGenresToGenres.put(9648, "Mystery");
        this.idGenresToGenres.put(10749, "Romance");
        this.idGenresToGenres.put(878, "Science Fiction");
        this.idGenresToGenres.put(53, "Thriller");
        this.idGenresToGenres.put(10752, "War");
        this.idGenresToGenres.put(37, "Western");
        this.idGenresToGenres.put(10770, "TV Movie");
    }

    public HashMap<Integer, String> getIdGenresToGenres() {
        return idGenresToGenres;
    }

    public void setIdGenresToGenres(HashMap<Integer, String> idGenresToGenres) {
        this.idGenresToGenres = idGenresToGenres;
    }
}
