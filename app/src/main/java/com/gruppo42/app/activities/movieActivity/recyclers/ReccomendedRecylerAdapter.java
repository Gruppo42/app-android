package com.gruppo42.app.activities.movieActivity.recyclers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
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
import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.MovieReccQueryResultDTO;
import com.gruppo42.app.ui.dialogs.ChangeListener;
import com.gruppo42.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

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
                                        onEmptyResults.onChange(null);
                                        reccomendedMovies = new ArrayList<>();
                                        return;
                                    } else {
                                        reccomendedMovies = response.body().getResults();
                                        notifyDataSetChanged();
                                    }
                                } else {
                                    onEmptyResults.onChange(null);
                                    reccomendedMovies = new ArrayList<>();
                                    return;
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieReccQueryResultDTO> call, Throwable t) {
                                reccomendedMovies = new ArrayList<>();
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
        return new ReccomendedRecylerAdapter.ReccomendedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReccomendedViewHolder holder, int position) {
        MovieReccQueryResultDTO.ReccomendedMovieDTO reccMovie = reccomendedMovies.get(position);
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

    public class ReccomendedViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;
        public ReccomendedViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.picture);
            name = itemView.findViewById(R.id.name);
        }
    }
}
