package com.gruppo42.app.api.models;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {
    String ENDPOINT = "https://gruppo42.azurewebsites.net/api/";

    @GET("user/me")
    Call<UserDTO> getUserDetails(@Header("AUTHORIZATION") String bearerToken);

    class Instance {

        static UserApi instance;

        public static UserApi get() {
            if (instance == null)
                instance = newUserApi();
            return instance;
        }

        public static UserApi newUserApi() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UserApi.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(UserApi.class);
        }
    }

}
