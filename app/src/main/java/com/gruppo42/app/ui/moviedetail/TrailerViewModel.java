package com.gruppo42.app.ui.moviedetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gruppo42.app.api.models.Resource;
import com.gruppo42.app.api.models.TrailerResultDTO;

import java.util.List;

public class TrailerViewModel extends ViewModel {

    private static final String TAG = "TrailerViewModel";

    private MutableLiveData<Resource<List<TrailerResultDTO>>> videos;

    public TrailerViewModel() {}

    public LiveData<Resource<List<TrailerResultDTO>>> getVideosResource(int movie_id) {

        if (videos == null) {
            videos = new MutableLiveData<>();
            Log.d(TAG, "getVideosResource: Download the videos from Internet");
            TrailerRepository.getInstance().getVideos(videos, movie_id);
        }

        Log.d(TAG, "getVideosResource: Videos");
        return videos;
    }

    public MutableLiveData<Resource<List<TrailerResultDTO>>> getVideosLiveData() { return videos; }
}
