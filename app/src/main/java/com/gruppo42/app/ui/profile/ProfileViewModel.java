package com.gruppo42.app.ui.profile;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> name;
    private MutableLiveData<String> profileImage;
    private MutableLiveData<String> surname;
    private MutableLiveData<String> username;
    private MutableLiveData<String> email;
    private MutableLiveData<List<String>> favorites;
    private MutableLiveData<List<String>> watchlist;
    private UserApi api;

    public ProfileViewModel(){
        super();
    }

    public ProfileViewModel(String token) {
        super();
        name = new MutableLiveData<>();
        profileImage = new MutableLiveData<>();
        surname = new MutableLiveData<>();
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
        favorites = new MutableLiveData<>();
        watchlist = new MutableLiveData<>();
        this.api = UserApi.Instance.get();
        //Prendi dati da api dando il token
        api.getUserDetails("Bearer "+ token).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                UserDTO user = response.body();
                name.setValue(user.getName());
                profileImage.setValue(user.getImage());
                surname.setValue(user.getSurname());
                username.setValue(user.getUsername());
                email.setValue(user.getEmail());
                favorites.setValue(user.getFavorites().stream().map(v -> v.toString()).collect(Collectors.toList()));
                watchlist.setValue(user.getWatchlist().stream().map(v -> v.toString()).collect(Collectors.toList()));
            }
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("Failure", t.toString());
            }
        });

    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(MutableLiveData<String> name) {
        this.name = name;
    }

    public MutableLiveData<String> getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MutableLiveData<String> profileImage) {
        this.profileImage = profileImage;
    }

    public MutableLiveData<String> getSurname() {
        return surname;
    }

    public void setSurname(MutableLiveData<String> surname) {
        this.surname = surname;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public void setUsername(MutableLiveData<String> username) {
        this.username = username;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(MutableLiveData<String> email) {
        this.email = email;
    }

    public MutableLiveData<List<String>> getFavorites() {
        return favorites;
    }

    public void setFavorites(MutableLiveData<List<String>> favorites) {
        this.favorites = favorites;

    }

    public MutableLiveData<List<String>> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(MutableLiveData<List<String>> watchlist) {
        this.watchlist = watchlist;
    }


}