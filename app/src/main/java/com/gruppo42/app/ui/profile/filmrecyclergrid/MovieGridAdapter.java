package com.gruppo42.app.ui.profile.filmrecyclergrid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.MovieDetailsDTO;
import com.gruppo42.app.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {
    private List<String> movieIDs;
    private Map<String, String> cache;
    private LayoutInflater mInflater;
    private MovieApi api;
    private Context context;

    public MovieGridAdapter(Context context, List<String> movieIDs)
    {
        super();
        this.cache = new HashMap<>();
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.movieIDs = movieIDs;
        this.api = MovieApi.Instance.get();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        String url = cache.get(movieIDs.get(position));
        if (url!=null)
            load_image(url, holder.getImageView());
        else
            api.getDeatils(movieIDs.get(position), Constants.MOVIES_API_KEY).enqueue(new Callback<MovieDetailsDTO>() {
                @Override
                public void onResponse(Call<MovieDetailsDTO> call, Response<MovieDetailsDTO> response) {
                    Log.d("Response: ", response.toString());
                    String imageUrl = response.body().getPoster_path();
                    cache.put(movieIDs.get(position), imageUrl);
                    load_image(imageUrl, holder.getImageView());
                }
                @Override
                public void onFailure(Call<MovieDetailsDTO> call, Throwable t) {
                }
            });
        }

    @Override
    public int getItemCount() {
        return movieIDs.size();
    }

    private void load_image(String url, @NonNull ImageView view)
    {
        Glide
                .with(context)
                .load("https://image.tmdb.org/t/p/w300/"+url)
                .fallback(R.drawable.ic_placeholder_err)
                .error(R.drawable.ic_placeholder_err)
                .transition(GenericTransitionOptions.with(R.transition.zoomin))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.picture);
        }

        public ImageView getImageView() {
            return image;
        }

        public void setImageView(ImageView image) {
            this.image = image;
        }
    }
}