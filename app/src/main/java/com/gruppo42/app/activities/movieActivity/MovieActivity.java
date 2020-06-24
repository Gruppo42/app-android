package com.gruppo42.app.activities.movieActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Interpolator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gruppo42.app.R;
import com.gruppo42.app.activities.movieActivity.recyclers.ActorRecyclerAdapter;
import com.gruppo42.app.activities.movieActivity.recyclers.ReccomendedRecylerAdapter;
import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.MovieDetailsDTO;
import com.gruppo42.app.api.models.OMDBApi;
import com.gruppo42.app.api.models.OMDBResponse;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserDTO;
import com.gruppo42.app.api.models.VideoQueryDTO;
import com.gruppo42.app.databinding.ActivityMovieBinding;
import com.gruppo42.app.session.SessionManager;
import com.gruppo42.app.utils.Constants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;
    private OMDBApi omdbApi;
    private Calendar releaseDate;
    private MovieApi movieApi;
    private UserApi userApi;
    private List<String> favorites;
    private MovieDetailsDTO movieDetailsDTO = null;
    private String currentMovieID;
    private List<String> watchlist;
    private SessionManager sessionManager;
    private YouTubePlayerView youTubePlayerView;
    private ActorRecyclerAdapter actorRecyclerAdapter;
    private  ReccomendedRecylerAdapter reccomendedRecylerAdapter;
    private boolean inwatchlist = false;
    private boolean infavoritelist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        currentMovieID = this.getSharedPreferences("userSession", Context.MODE_PRIVATE).getString("current", "38286");
        movieApi = MovieApi.Instance.get();
        userApi = UserApi.Instance.get();
        omdbApi = OMDBApi.Instance.get();
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        actorRecyclerAdapter = new ActorRecyclerAdapter(MovieActivity.this, new ArrayList<>());
        binding.actorsRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MovieActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.actorsRecycler.setLayoutManager(layoutManager);
        binding.actorsRecycler.setNestedScrollingEnabled(true);
        binding.actorsRecycler.getRecycledViewPool().setMaxRecycledViews(0, 20);
        binding.actorsRecycler.setAdapter(actorRecyclerAdapter);

        reccomendedRecylerAdapter = new ReccomendedRecylerAdapter(MovieActivity.this, currentMovieID);
        binding.reccomendedRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(MovieActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.reccomendedRecycler.setLayoutManager(layoutManager2);
        binding.reccomendedRecycler.setNestedScrollingEnabled(true);
        binding.reccomendedRecycler.getRecycledViewPool().setMaxRecycledViews(0, 20);
        binding.reccomendedRecycler.setAdapter(reccomendedRecylerAdapter);

        prepareView();
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        movieApi.getVideos(currentMovieID, Constants.MOVIES_API_KEY)
                .enqueue(new Callback<VideoQueryDTO>() {
                    @Override
                    public void onResponse(Call<VideoQueryDTO> call, Response<VideoQueryDTO> response) {
                        Log.d("Debug", response.toString());
                        if(response.isSuccessful() && response.body().getYoutubeVideoID()!=null)
                            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                @Override
                                public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                                    super.onReady(youTubePlayer);
                                    youTubePlayer.cueVideo(response.body().getYoutubeVideoID(), 0f);
                                }
                            });
                        else
                        {
                            binding.youtubePlayerView.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Call<VideoQueryDTO> call, Throwable t) {
                        binding.youtubePlayerView.setVisibility(View.GONE);
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    private void prepareView()
    {
        if(sessionManager.isLoggedIn())
            userApi.getUserDetails(sessionManager.getUserAuthorization())
                    .enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            Log.d("DEBUG", response.toString());
                            if(response.isSuccessful())
                            {
                                favorites = response.body().getFavorites().stream().map(s -> s.toString()).collect(Collectors.toList());
                                watchlist = response.body().getWatchlist().stream().map(s -> s.toString()).collect(Collectors.toList());
                                infavoritelist = favorites.stream().anyMatch(currentMovieID::equals);
                                inwatchlist = watchlist.stream().anyMatch(currentMovieID::equals);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {

                        }
                    });
        else
        {
            //binding.inwatchlist.setVisibility(View.GONE);
            //binding.infavorites.setVisibility(View.GONE);
        }
        movieApi.getDeatils(currentMovieID, Constants.MOVIES_API_KEY)
                .enqueue(new Callback<MovieDetailsDTO>() {
                    @Override
                    public void onResponse(Call<MovieDetailsDTO> call, Response<MovieDetailsDTO> response) {
                        if(response.isSuccessful())
                        {
                            movieDetailsDTO = response.body();
                            binding.runtime.setText(movieDetailsDTO.getRuntime()+" min");
                            if(movieDetailsDTO.getRelease_date()!=null && movieDetailsDTO.getRelease_date().length()>=4)
                            {
                                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    releaseDate = new GregorianCalendar();
                                    releaseDate.setTime(parser.parse(movieDetailsDTO.getRelease_date()));
                                    //binding.year.setText(releaseDate.get(Calendar.YEAR));
                                } catch (ParseException e) {
                                }
                            }
                            binding.title.setText(movieDetailsDTO.getOriginal_title());
                            binding.overview.setText(movieDetailsDTO.getOverview());
                            Glide
                                .with(MovieActivity.this)
                                .load("https://image.tmdb.org/t/p/w300/" + movieDetailsDTO.getPoster_path())
                                .centerCrop()
                                .fallback(R.drawable.baseline_movie_white_48dp)
                                .error(R.drawable.baseline_movie_white_48dp)
                                .into(binding.poster);
                            Log.d("BACKDROP**********", movieDetailsDTO.getBackdrop_path());
                            binding.backdrop.setVisibility(View.VISIBLE);
                            Glide
                                    .with(MovieActivity.this)
                                    .load("https://image.tmdb.org/t/p/w500/" + movieDetailsDTO.getBackdrop_path())
                                    .centerCrop()
                                    .fallback(R.drawable.baseline_movie_white_48dp)
                                    .error(R.drawable.baseline_movie_white_48dp)
                                    .into(binding.backdrop);
                            Log.d("OMDB**********", "************************");
                            omdbApi.getMovieDetails(Constants.OMOVIE_API_KEY, movieDetailsDTO.getImdb_id())
                                    .enqueue(new Callback<OMDBResponse>() {
                                        @Override
                                        public void onResponse(Call<OMDBResponse> call, Response<OMDBResponse> response) {
                                            Log.d("DEBUG*********************************************", response.toString());
                                            if(response.isSuccessful())
                                            {
                                                OMDBResponse om = response.body();
                                                Log.d("DEBUG*********************************************", response.toString());
                                                actorRecyclerAdapter.setActors(Arrays.asList(om.getActors().split(", ")));
                                                actorRecyclerAdapter.notifyDataSetChanged();
                                                binding.country.setText(om.getCountry());
                                                binding.year.setText(om.getYear());
                                                try {
                                                    animateValue(binding.metascore, (int)(Float.parseFloat(om.getMetascore())*10), null, 3000);
                                                } catch (Exception e)
                                                {
                                                    binding.metascore.setText("-");
                                                }
                                                try {
                                                    animateValue(binding.userscore, (int)(Float.parseFloat(om.getImdbRating())*10), null, 3000);
                                                } catch (Exception e)
                                                {
                                                    binding.userscore.setText("-");
                                                }
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<OMDBResponse> call, Throwable t) {
                                            Log.d("OMDB FAIL**********", t.toString());
                                        }
                                    });
                            Log.d("OMDB END**********", "************************");
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieDetailsDTO> call, Throwable t) {

                    }
                });
    }

    private void animateValue(final TextView view, int toVal, TimeInterpolator interpolator, int duration)
    {
        if(interpolator==null)
            interpolator = new AccelerateDecelerateInterpolator();
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, toVal);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        if(duration<=0)
            duration=5000;
        animator.setDuration(duration); // here you set the duration of the anim
        animator.start();
    }

}