package com.gruppo42.app.activities.movieActivity.recyclers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.gruppo42.app.R;
import com.gruppo42.app.activities.Login;
import com.gruppo42.app.activities.SplashScreenActivity;
import com.gruppo42.app.activities.movieActivity.MovieActivity;
import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.MovieReccQueryResultDTO;
import com.gruppo42.app.ui.dialogs.ChangeListener;
import com.gruppo42.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReccomendedRecylerAdapter extends RecyclerView.Adapter<ReccomendedRecylerAdapter.ReccomendedViewHolder> {

    private Context context;
    private List<MovieReccQueryResultDTO.ReccomendedMovieDTO> reccomendedMovies;
    private MovieApi api;
    private ChangeListener onEmptyResults;

    public ReccomendedRecylerAdapter(Context context, String movie_id)
    {
        super();
        this.context = context;
        reccomendedMovies = new ArrayList<>();
        api = MovieApi.Instance.get();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                api.getRecommendations(movie_id, Constants.MOVIES_API_KEY)
                        .enqueue(new Callback<MovieReccQueryResultDTO>() {
                            @Override
                            public void onResponse(Call<MovieReccQueryResultDTO> call, Response<MovieReccQueryResultDTO> response) {
                                Log.d("DEbug", response.toString());
                                if (response.isSuccessful()) {
                                    if (response.body().getResults().size() == 0) {
                                        if(onEmptyResults!=null)
                                            onEmptyResults.onChange(null);
                                        reccomendedMovies = new ArrayList<>();
                                        return;
                                    } else {
                                        reccomendedMovies = response.body().getResults().stream().filter(new Predicate<MovieReccQueryResultDTO.ReccomendedMovieDTO>() {
                                                                @Override
                                                                public boolean test(MovieReccQueryResultDTO.ReccomendedMovieDTO reccomendedMovieDTO) {
                                                                    return reccomendedMovieDTO.getPoster_path()!=null && reccomendedMovieDTO.getPoster_path().length()!=0;
                                                                }
                                                            }).collect(Collectors.toList());
                                    }
                                } else {
                                    if(onEmptyResults!=null)
                                        onEmptyResults.onChange(null);
                                    reccomendedMovies = new ArrayList<>();
                                    return;
                                }
                            }
                            @Override
                            public void onFailure(Call<MovieReccQueryResultDTO> call, Throwable t) {
                                reccomendedMovies = new ArrayList<>();
                                if(onEmptyResults!=null)
                                    onEmptyResults.onChange(null);
                            }
                        });
            }});
    }

    @NonNull
    @Override
    public ReccomendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reccomended_movie_item, parent, false);
        return new ReccomendedRecylerAdapter.ReccomendedViewHolder(v, null);
    }

    @Override
    public void onBindViewHolder(@NonNull ReccomendedViewHolder holder, int position) {
        MovieReccQueryResultDTO.ReccomendedMovieDTO reccMovie = reccomendedMovies.get(position);
        holder.id = reccMovie.getId();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Glide
                        .with(context)
                        .load("https://image.tmdb.org/t/p/w200/" + reccMovie.getPoster_path())
                        .centerCrop()
                        .fallback(R.drawable.baseline_movie_white_48dp)
                        .error(R.drawable.baseline_movie_white_48dp)
                        .into(holder.image);
            }});
        holder.name.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return reccomendedMovies.size();
    }

    public void setOnEmptyResults(ChangeListener onEmptyResults) {
        this.onEmptyResults = onEmptyResults;
    }

    public class ReccomendedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        String id;
        ImageView image;
        TextView name;

        public ReccomendedViewHolder(@NonNull View itemView, String movie_id) {
            super(itemView);
            id = movie_id;
            image = itemView.findViewById(R.id.picture);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MovieActivity.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(image, "poster");
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity)context, pairs);
            Bundle b = new Bundle();
            b.putCharSequence("movie", id); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            context.startActivity(intent, options.toBundle());
        }
    }

}
