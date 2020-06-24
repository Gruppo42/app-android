package com.gruppo42.app.ui.home;

import androidx.lifecycle.MutableLiveData;

import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.Resource;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeMoviesRepository {

    private static final String TAG = "HomeMoviesRepository";

    private static HomeMoviesRepository instance;
    private MovieApi moviesService;

    private HomeMoviesRepository() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOVIES_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        moviesService = retrofit.create(MovieApi.class);
    }

    public static synchronized HomeMoviesRepository getInstance() {
        if (instance == null) {
            instance = new HomeMoviesRepository();
        }
        return instance;
    }

    public void getTrendingMovies(MutableLiveData<Resource<List<ResultDTO>>> trendingMoviesResource, String media_type, String time_window) {
        Call<QueryResultDTO> call = moviesService.getTrendingMovies(media_type, time_window, Constants.MOVIES_API_KEY);

        call.enqueue(new Callback<QueryResultDTO>() {
            @Override
            public void onResponse(@NotNull Call<QueryResultDTO> call, @NotNull Response<QueryResultDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Resource<List<ResultDTO>> resource = new Resource<>();
                    resource.setData(response.body().getResults());
                    for (int i = 0; i < resource.getData().size(); i++) {
                        if (resource.getData().get(i).getBackdrop_path() == null) {
                            resource.getData().remove(i);
                        }
                    }
                    resource.setTotalResults(response.body().getTotal_results());
                    resource.setStatusCode(response.code());
                    resource.setStatusMessage(response.message());
                    trendingMoviesResource.postValue(resource);
                } else if (response.errorBody() != null) {
                    Resource<List<ResultDTO>> resource = new Resource<>();
                    resource.setStatusCode(response.code());
                    try {
                        resource.setStatusMessage(response.errorBody().string() + "- " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    trendingMoviesResource.postValue(resource);
                }
            }

            @Override
            public void onFailure(Call<QueryResultDTO> call, Throwable t) {
                Resource<List<ResultDTO>> resource = new Resource<>();
                resource.setStatusMessage(t.getMessage());
                trendingMoviesResource.postValue(resource);
            }
        });
    }

    public void getMoviesByDateOrGenre(MutableLiveData<Resource<List<ResultDTO>>> moviesByDateOrGenreResource, int page, String today, String with_genres) {

        Call<QueryResultDTO> call = null;

        if (today == null) {
            call = moviesService.getMoviesByDateOrGenre(page, null, with_genres, Constants.MOVIES_API_KEY);
        } else {
            call = moviesService.getMoviesByDateOrGenre(page, today, null, Constants.MOVIES_API_KEY);
        }

        call.enqueue(new Callback<QueryResultDTO>() {
            @Override
            public void onResponse(@NotNull Call<QueryResultDTO> call, @NotNull Response<QueryResultDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Resource<List<ResultDTO>> resource = new Resource<>();

                    if (moviesByDateOrGenreResource.getValue() != null && moviesByDateOrGenreResource.getValue().getData() != null) {
                        List<ResultDTO> currentResultDTOList = moviesByDateOrGenreResource.getValue().getData();
                        currentResultDTOList.remove(currentResultDTOList.size() - 1);
                        currentResultDTOList.addAll(response.body().getResults());
                        resource.setData(currentResultDTOList);
                        for (int i = 0; i < resource.getData().size(); i++) {
                            if (resource.getData().get(i).getPoster_path() == null) {
                                resource.getData().remove(i);
                            }
                        }
                    } else {
                        resource.setData(response.body().getResults());
                        for (int i = 0; i < resource.getData().size(); i++) {
                            if (resource.getData().get(i).getPoster_path() == null) {
                                resource.getData().remove(i);
                            }
                        }
                    }

                    resource.setTotalResults(response.body().getTotal_results());
                    resource.setStatusCode(response.code());
                    resource.setStatusMessage(response.message());
                    resource.setLoading(false);
                    moviesByDateOrGenreResource.postValue(resource);
                } else if (response.errorBody() != null) {
                    Resource<List<ResultDTO>> resource = new Resource<>();
                    resource.setStatusCode(response.code());
                    try {
                        resource.setStatusMessage(response.errorBody().string() + "- " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    moviesByDateOrGenreResource.postValue(resource);
                }
            }

            @Override
            public void onFailure(Call<QueryResultDTO> call, Throwable t) {
                Resource<List<ResultDTO>> resource = new Resource<>();
                resource.setStatusMessage(t.getMessage());
                moviesByDateOrGenreResource.postValue(resource);
            }
        });
    }
}
