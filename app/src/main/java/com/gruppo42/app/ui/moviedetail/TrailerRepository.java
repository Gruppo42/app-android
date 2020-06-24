package com.gruppo42.app.ui.moviedetail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.Resource;
import com.gruppo42.app.api.models.TrailerQueryResultDTO;
import com.gruppo42.app.api.models.TrailerResultDTO;
import com.gruppo42.app.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrailerRepository {

    private static final String TAG = "TrailerRepository";

    private static TrailerRepository instance;
    private MovieApi moviesService;

    private TrailerRepository() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOVIES_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        moviesService = retrofit.create(MovieApi.class);
    }

    public static synchronized TrailerRepository getInstance() {
        if (instance == null) {
            instance = new TrailerRepository();
        }
        return instance;
    }

    public void getVideos(MutableLiveData<Resource<List<TrailerResultDTO>>> videosResource, int movie_id) {
        Call<TrailerQueryResultDTO> call = moviesService.getVideos(movie_id, Constants.MOVIES_API_KEY);

        call.enqueue(new Callback<TrailerQueryResultDTO>() {
            @Override
            public void onResponse(@NotNull Call<TrailerQueryResultDTO> call, @NotNull Response<TrailerQueryResultDTO> response) {
                Log.d(TAG, "onResponse: ");
                if (response.isSuccessful() && response.body() != null) {
                    Resource<List<TrailerResultDTO>> resource = new Resource<>();
                    resource.setData(response.body().getResults());
                    resource.setTotalResults(response.body().getTotal_results());
                    resource.setStatusCode(response.code());
                    resource.setStatusMessage(response.message());
                    videosResource.postValue(resource);
                } else if (response.errorBody() != null) {
                    Resource<List<TrailerResultDTO>> resource = new Resource<>();
                    resource.setStatusCode(response.code());
                    try {
                        resource.setStatusMessage(response.errorBody().string() + "- " + response.message());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    videosResource.postValue(resource);
                }
            }

            @Override
            public void onFailure(Call<TrailerQueryResultDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                Resource<List<TrailerResultDTO>> resource = new Resource<>();
                resource.setStatusMessage(t.getMessage());
                videosResource.postValue(resource);
            }
        });
    }
}
