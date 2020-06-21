package com.gruppo42.app.api.models;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

import java.nio.ByteBuffer;
import java.util.List;

public class UserDTO {
    @SerializedName("username")
    private String username;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("favorites")
    private List<MovieDTO> favorites;
    @SerializedName("watchList")
    private List<MovieDTO> watchlist;

    public UserDTO()
    {
    }

    public UserDTO(String username, String name, String surname, String email, String image, List<MovieDTO> favorites, List<MovieDTO> watchlist) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.image = image;
        this.favorites = favorites;
        this.watchlist = watchlist;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<MovieDTO> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<MovieDTO> favorites) {
        this.favorites = favorites;
    }

    public List<MovieDTO> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(List<MovieDTO> watchlist) {
        this.watchlist = watchlist;
    }

    public class MovieDTO
    {
        private String id;

        public MovieDTO() {}

        @Override
        public boolean equals(Object obj)
        {
            // If the object is compared with itself then return true
            if (obj == this) {
                return true;
            }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
            if (!(obj instanceof MovieDTO)) {
                return false;
            }

            // typecast o to Complex so that we can compare data members
            MovieDTO v = (MovieDTO) obj;

            // Compare the data members and return accordingly
            return this.id.equals(v.id);
        }

        @Override
        public int hashCode()
        {
            return Integer.parseInt(id);
        }

        @Override
        public String toString()
        {
            return this.id;
        }

        public MovieDTO(String id)
        {
            this.id = id;
        }

        public String getId() { return id; }

        public void setId(String id) { this.id = id; }
    }
}
