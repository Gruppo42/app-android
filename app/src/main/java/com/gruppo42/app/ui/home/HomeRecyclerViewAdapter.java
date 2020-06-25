package com.gruppo42.app.ui.home;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.util.List;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gruppo42.app.R;
import com.gruppo42.app.activities.movieActivity.MovieActivity;
import com.gruppo42.app.api.models.MovieItem;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.utils.Constants;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "HomeRecyclerViewAdapter";

    private static final int MOVIE_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

    public interface OnItemClickListener {
        void onItemClick(MovieItem movieItem);
    }

    private List<ResultDTO> resultDTOList;
    private LayoutInflater layoutInflater;
    private Context context;

    public HomeRecyclerViewAdapter(Context context, List<ResultDTO> resultDTOList, HomeRecyclerViewAdapter.OnItemClickListener l) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.resultDTOList = resultDTOList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MOVIE_VIEW_TYPE) {
            View view = this.layoutInflater.inflate(R.layout.movie_item, parent, false);
            return new MoviesViewHolder(view);
        } else {
            View view = this.layoutInflater.inflate(R.layout.loading_item, parent, false);
            return new LoadingMoviesViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MoviesViewHolder) {
            ((MoviesViewHolder) holder).id = resultDTOList.get(position).getId()+"";
            ((MoviesViewHolder) holder).bind(resultDTOList.get(position), null);
        } else if (holder instanceof LoadingMoviesViewHolder) {
            ((LoadingMoviesViewHolder) holder).progressBarLoadingMovies.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (resultDTOList != null) {
            return resultDTOList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (resultDTOList.get(position) == null) {
            return LOADING_VIEW_TYPE;
        } else {
            return MOVIE_VIEW_TYPE;
        }
    }

    public void setData(List<ResultDTO> resultDTOList) {
        if (resultDTOList != null) {
            this.resultDTOList = resultDTOList;
            notifyDataSetChanged();
        }
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        String id;
        ImageView imageViewMovieImage;

        MoviesViewHolder(View view) {
            super(view);
            imageViewMovieImage = view.findViewById(R.id.imageViewMoviePicture);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        void bind(ResultDTO resultDTO, OnItemClickListener onItemClickListener) {

            String imageUrl = Constants.MOVIES_LIST_IMAGE_BASE_URL + resultDTO.getPoster_path();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(10));

            if (imageUrl != null) {
                Glide
                        .with(itemView.getContext())
                        .load(imageUrl)
                        .fallback(R.drawable.baseline_movie_white_48dp)
                        .error(R.drawable.baseline_movie_white_48dp)
                        .transition(GenericTransitionOptions.with(R.transition.zoomin))
                        .apply(requestOptions)
                        .into(imageViewMovieImage);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieActivity.class);
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(imageViewMovieImage, "poster");
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation((Activity)context, pairs);
                    Bundle b = new Bundle();
                    b.putCharSequence("movie", id); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    context.startActivity(intent, options.toBundle());
                }
            });
        }
    }

    static class LoadingMoviesViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBarLoadingMovies;

        LoadingMoviesViewHolder(View view) {
            super(view);
            progressBarLoadingMovies = view.findViewById(R.id.progressBarLoadingMovies);
        }
    }
}
