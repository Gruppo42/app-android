package com.gruppo42.app.api.models;

import com.gruppo42.app.ui.search.BitmapConverterFactory;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    String ENDPOINT = "https://api.themoviedb.org/3/";

    @GET("genre/movie/list")
    Call<GenresDTO> getGenre(@Query("api_key") String api_key);
    @GET("search/movie")
    Call<QueryResultDTO> findMovieWith(@Query("api_key") String api_key,
                                       @Query("language") String language,
                                       @Query("page") String page,
                                       @Query("query") String query,
                                       @Query("region") String region,
                                       @Query("primary_release_year") String year);
    @GET("trending/{media_type}/{time_window}")
    Call<QueryResultDTO> getTrendingMovies(@Path("media_type") String media_type,
                                           @Path("time_window") String time_window,
                                           @Query("api_key") String api_key);
    @GET("discover/movie")
    Call<QueryResultDTO> getMoviesByDateOrGenre(@Query("page") int page,
                                                @Query("primary_release_date.gte") String primary_release_date_gte,
                                                @Query("with_genres") String with_genres,
                                                @Query("api_key") String api_key);

    class Instance {

        static MovieApi instance;

        public static MovieApi get() {
            if (instance == null)
                instance = newMovieApi();
            return instance;
        }

        public static MovieApi newMovieApi() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieApi.ENDPOINT)
                    .addConverterFactory(BitmapConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(MovieApi.class);
        }
    }
}
