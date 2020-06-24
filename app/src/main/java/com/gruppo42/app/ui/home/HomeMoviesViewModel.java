package com.gruppo42.app.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.Resource;
import com.gruppo42.app.api.models.ResultDTO;

import java.util.List;

public class HomeMoviesViewModel extends ViewModel {

    private static final String TAG = "HomeMoviesViewModel";

    private MutableLiveData<Resource<List<ResultDTO>>> movies;
    private int page = 1;
    private int currentResults;
    private boolean isLoading;

    public HomeMoviesViewModel() {}

    public LiveData<Resource<List<ResultDTO>>> getTrendingMoviesResource(String media_type, String time_window) {

        if (movies == null) {
            movies = new MutableLiveData<>();
            Log.d(TAG, "getTrendingMovies: Download the trending movies from Internet");
            HomeMoviesRepository.getInstance().getTrendingMovies(movies, media_type, time_window);
        }

        Log.d(TAG, "getTrendingMovies: Movies");
        return movies;
    }

    public LiveData<Resource<List<ResultDTO>>> getMoviesByDateOrGenreResource(String today, String with_genres) {

       if (movies == null) {
           movies = new MutableLiveData<>();
           Log.d(TAG, "getMoviesByGenre: Download the movies by genre from Internet");
           HomeMoviesRepository.getInstance().getMoviesByDateOrGenre(movies, page, today, with_genres);
       }

        Log.d(TAG, "getMoviesByGenre: Movies");
        return movies;
    }

    public LiveData<Resource<List<ResultDTO>>> getMoreMoviesByDateOrGenreResource(String today, String with_genres) {
        Log.d(TAG, "getMovies: More Movies");
        HomeMoviesRepository.getInstance().getMoviesByDateOrGenre(movies, page, today, with_genres);

        return movies;
    }

    public MutableLiveData<Resource<List<ResultDTO>>> getMoviesLiveData() {
        return movies;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) { this.page = page; }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
