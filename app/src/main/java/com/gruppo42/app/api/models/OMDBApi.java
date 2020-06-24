package com.gruppo42.app.api.models;


import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OMDBApi {

    static String ENDPOINT = "http://www.omdbapi.com/";

    @GET
    Response<OMDBResponse> getMovieDetails(@Query("apikey") String api_key,
                                            @Query("i") String movieId);


    public static class Instance {
        static OMDBApi instance;

        public static OMDBApi get() {
            if (instance == null)
                instance = newOMDBApi();
            return instance;
        }

        public static OMDBApi newOMDBApi() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(OMDBApi.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(OMDBApi.class);
        }
    }
}
