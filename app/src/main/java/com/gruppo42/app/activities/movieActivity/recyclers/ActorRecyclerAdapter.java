package com.gruppo42.app.activities.movieActivity.recyclers;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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
import com.gruppo42.app.activities.MainActivity;
import com.gruppo42.app.activities.SplashScreenActivity;
import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.MovieQueryResultDTO;
import com.gruppo42.app.ui.dialogs.ChangeListener;
import com.gruppo42.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorRecyclerAdapter extends RecyclerView.Adapter<ActorRecyclerAdapter.ActorViewHolder> {

    private Context context;
    private List<String> actors;
    private MovieApi api;
    private ChangeListener onEmptyResults;

    public ActorRecyclerAdapter()
    {
        super();
        actors = new ArrayList<>();
        api = MovieApi.Instance.get();
    }

    public ActorRecyclerAdapter(Context context, List<String> actors)
    {
        super();
        this.context = context;
        this.actors = actors;
        api = MovieApi.Instance.get();
    }

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_recycler_item, parent, false);
        return new ActorRecyclerAdapter.ActorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        String actor = actors.get(position);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                api.getActorDetails(Constants.MOVIES_API_KEY, actor)
                        .enqueue(new Callback<MovieQueryResultDTO>() {
                            @Override
                            public void onResponse(Call<MovieQueryResultDTO> call, Response<MovieQueryResultDTO> response) {
                                Log.d("Debug", response.toString());
                                if(response.isSuccessful())
                                {
                                    List<MovieQueryResultDTO.ActorDTO> actors = response.body().getResults();
                                    if(actors==null || actors.size()==0)
                                        return;
                                    else {
                                        Glide
                                                .with(context)
                                                .load("https://image.tmdb.org/t/p/w300/"+actors.get(0).getProfile_path())
                                                .circleCrop()
                                                .fallback(R.drawable.no_image_actor)
                                                .error(R.drawable.no_image_actor)
                                                .transition(GenericTransitionOptions.with(R.transition.zoomin))
                                                .into(holder.image);
                                        holder.name.setText(actor);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieQueryResultDTO> call, Throwable t) {
                                Log.d("Debug", "Fails");
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public void setOnEmptyResults(ChangeListener onEmptyResults) {
        this.onEmptyResults = onEmptyResults;
    }

    public class ActorViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;

        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.actorName);
        }

    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }
}
