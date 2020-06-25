package com.gruppo42.app.activities.movieActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gruppo42.app.R;
import com.gruppo42.app.activities.movieActivity.recyclers.ActorRecyclerAdapter;
import com.gruppo42.app.activities.movieActivity.recyclers.ReccomendedRecylerAdapter;
import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.MovieDetailsDTO;
import com.gruppo42.app.api.models.OMDBApi;
import com.gruppo42.app.api.models.OMDBResponse;
import com.gruppo42.app.api.models.UserApi;
import com.gruppo42.app.api.models.UserApiResponse;
import com.gruppo42.app.api.models.UserDTO;
import com.gruppo42.app.api.models.VideoQueryDTO;
import com.gruppo42.app.databinding.ActivityMovieBinding;
import com.gruppo42.app.session.SessionManager;
import com.gruppo42.app.ui.dialogs.ChangeListener;
import com.gruppo42.app.utils.Constants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private CountDownTimer countDownTimer;
    private boolean isFABOpen = false;
    private boolean inwatchlist = false;
    private boolean infavoritelist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        Bundle b = getIntent().getExtras();
        if(b!=null && b.getCharSequence("movie", null)!=null)
            currentMovieID = b.getCharSequence("movie", null).toString();
        else
            finish();
        if(currentMovieID==null || currentMovieID.length()==0)
            finish();
        movieApi = MovieApi.Instance.get();
        userApi = UserApi.Instance.get();
        omdbApi = OMDBApi.Instance.get();
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                actorRecyclerAdapter = new ActorRecyclerAdapter(MovieActivity.this, new ArrayList<>());
                actorRecyclerAdapter.setOnEmptyResults(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        binding.actorTitle.setVisibility(View.GONE);
                        binding.actorsRecycler.setVisibility(View.GONE);
                    }
                });
                binding.actorsRecycler.setHasFixedSize(true);
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(MovieActivity.this, LinearLayoutManager.HORIZONTAL, false);
                binding.actorsRecycler.setLayoutManager(layoutManager);
                binding.actorsRecycler.setNestedScrollingEnabled(true);
                binding.actorsRecycler.getRecycledViewPool().setMaxRecycledViews(0, 20);
                binding.actorsRecycler.setAdapter(actorRecyclerAdapter);

                reccomendedRecylerAdapter = new ReccomendedRecylerAdapter(MovieActivity.this, currentMovieID);
                reccomendedRecylerAdapter.setOnEmptyResults(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        binding.reccomendedRecycler.setVisibility(View.GONE);
                        binding.reccTitle.setVisibility(View.GONE);
                    }
                });
                binding.reccomendedRecycler.setHasFixedSize(true);
                LinearLayoutManager layoutManager2
                        = new LinearLayoutManager(MovieActivity.this, LinearLayoutManager.HORIZONTAL, false);
                binding.reccomendedRecycler.setLayoutManager(layoutManager2);
                binding.reccomendedRecycler.setNestedScrollingEnabled(true);
                binding.reccomendedRecycler.getRecycledViewPool().setMaxRecycledViews(0, 20);
                binding.reccomendedRecycler.setAdapter(reccomendedRecylerAdapter);
            }
        });
        prepareView();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
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
                                            youTubePlayerView.setVisibility(View.VISIBLE);
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
        });
    }
    @Override
    public void onDestroy() {
        youTubePlayerView.release();
        super.onDestroy();
    }

    private void prepareView()
    {
        if(sessionManager.isLoggedIn()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    userApi.getUserDetails(sessionManager.getUserAuthorization())
                            .enqueue(new Callback<UserDTO>() {
                                @Override
                                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                                    Log.d("DEBUG", response.toString());
                                    if (response.isSuccessful()) {
                                        favorites = response.body().getFavorites().stream().map(s -> s.toString()).collect(Collectors.toList());
                                        watchlist = response.body().getWatchlist().stream().map(s -> s.toString()).collect(Collectors.toList());
                                        infavoritelist = favorites.stream().anyMatch(currentMovieID::equals);
                                        inwatchlist = watchlist.stream().anyMatch(currentMovieID::equals);
                                        if(infavoritelist)
                                            binding.fabfav.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_24));
                                        else
                                            binding.fabfav.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_border_24));
                                        if(inwatchlist)
                                            binding.fabwatch.setImageDrawable(getDrawable(R.drawable.ic_baseline_tv_off_24));
                                        else
                                            binding.fabwatch.setImageDrawable(getDrawable(R.drawable.ic_baseline_tv_24));
                                    }
                                }
                                @Override
                                public void onFailure(Call<UserDTO> call, Throwable t) {
                                }
                            });
                    binding.fabwatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(inwatchlist) {
                                userApi.removeFromWatchlist(sessionManager.getUserAuthorization(), currentMovieID)
                                        .enqueue(new Callback<UserApiResponse>() {
                                            @Override
                                            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                                                if (response.isSuccessful()) {
                                                    Log.d("REMOVED", "REMOVED MOVIE FROM WATCHLIST");
                                                    inwatchlist = false;
                                                    binding.fabwatch.setImageDrawable(getDrawable(R.drawable.ic_baseline_tv_24));
                                                    sessionManager.needRefresh(true);
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                                            }
                                        });
                            }
                            else
                            {
                                userApi.addToWatchlist(sessionManager.getUserAuthorization(), currentMovieID)
                                        .enqueue(new Callback<UserApiResponse>() {
                                            @Override
                                            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                                                if (response.isSuccessful()) {
                                                    Log.d("ADDED", "ADDED MOVIE TO WATCHLIST");
                                                    inwatchlist = true;
                                                    sessionManager.needRefresh(true);
                                                    binding.fabwatch.setImageDrawable(getDrawable(R.drawable.ic_baseline_tv_off_24));
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<UserApiResponse> call, Throwable t) {

                                            }
                                        });
                            }
                        }
                    });
                }
            });

            binding.scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if ((oldScrollX - scrollY) < 0) {
                        binding.floatingbutton.hide();
                        collapseFABMenu();
                    } else {
                        binding.floatingbutton.show();
                    }
                }
            });
            binding.floatingbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isFABOpen) {
                        isFABOpen = false;
                        collapseFABMenu();
                    } else {
                        isFABOpen = true;
                        binding.fabwatch.show();
                        binding.fabfav.show();
                        expandFABMenu();
                    }
                }
            });

            binding.fabfav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(infavoritelist) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                userApi.removeFromFavorites(sessionManager.getUserAuthorization(), currentMovieID)
                                        .enqueue(new Callback<UserApiResponse>() {
                                            @Override
                                            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                                                if (response.isSuccessful()) {
                                                    Log.d("REMOVED", "REMOVED MOVIE FROM FAVORITES");
                                                    infavoritelist = false;
                                                    binding.fabfav.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_border_24));
                                                    sessionManager.needRefresh(true);
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                                            }
                                        });
                            }
                        });
                    }
                    else
                    {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                userApi.addToFavorites(sessionManager.getUserAuthorization(), currentMovieID)
                                        .enqueue(new Callback<UserApiResponse>() {
                                            @Override
                                            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                                                if (response.isSuccessful()) {
                                                    Log.d("ADDED", "ADDED MOVIE TO FAVORITES");
                                                    infavoritelist = true;
                                                    binding.fabfav.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_24));
                                                    sessionManager.needRefresh(true);
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<UserApiResponse> call, Throwable t) {

                                            }
                                        });
                            }
                        });
                    }
                }
            });
        }
        else
        {
            binding.floatingbutton.setVisibility(View.GONE);
            binding.fabfav.setVisibility(View.GONE);
            binding.fabwatch.setVisibility(View.GONE);
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                movieApi.getDeatils(currentMovieID, Constants.MOVIES_API_KEY)
                        .enqueue(new Callback<MovieDetailsDTO>() {
                            @Override
                            public void onResponse(Call<MovieDetailsDTO> call, Response<MovieDetailsDTO> response) {
                                if(response.isSuccessful()) {
                                    movieDetailsDTO = response.body();
                                    binding.runtime.setText(movieDetailsDTO.getRuntime() + " min");
                                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                                    binding.title.setText(movieDetailsDTO.getOriginal_title());
                                    if(movieDetailsDTO.getRelease_date()==null || movieDetailsDTO.getRelease_date().length()==0)
                                        binding.releaseData.setVisibility(View.GONE);
                                    try {
                                        releaseDate = new GregorianCalendar();
                                        releaseDate.setTime(parser.parse(movieDetailsDTO.getRelease_date()));
                                        if(releaseDate.getTimeInMillis()>Calendar.getInstance().getTimeInMillis())
                                            countDownTimer = new CountDownTimer(releaseDate.getTimeInMillis()-Calendar.getInstance().getTimeInMillis(), 1000) {
                                                @SuppressLint("DefaultLocale")
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    long seconds = (int) (millisUntilFinished / 1000) % 60 ;
                                                    long minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                                                    long hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
                                                    long days   = (int) ((millisUntilFinished / (1000*60*60*24)));
                                                    binding.releaseData.setText("Releases in: " + days+"d "+hours+"h "+minutes+"m "+seconds+"s");
                                                }
                                                @Override
                                                public void onFinish() { }
                                            }.start();
                                        else
                                            binding.releaseData.setVisibility(View.GONE);
                                    } catch (Exception e)
                                    {
                                        binding.releaseData.setVisibility(View.GONE);
                                    }


                                    if(movieDetailsDTO.getOverview()==null || movieDetailsDTO.getOverview().length()==0) {
                                        binding.overview.setVisibility(View.GONE);
                                        binding.overviewTitle.setVisibility(View.GONE);
                                    }
                                    binding.overview.setText(movieDetailsDTO.getOverview());
                                    Glide
                                            .with(MovieActivity.this)
                                            .load("https://image.tmdb.org/t/p/w300/" + movieDetailsDTO.getPoster_path())
                                            .centerCrop()
                                            .fallback(R.drawable.baseline_movie_white_48dp)
                                            .error(R.drawable.baseline_movie_white_48dp)
                                            .into(binding.poster);
                                    Log.d("BACKDROP**********", movieDetailsDTO.getBackdrop_path() + "");
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
                                                    if (response.isSuccessful()) {
                                                        OMDBResponse om = response.body();
                                                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                                                        Log.d("DEBUG*********************************************", response.toString());
                                                        if(om.getActors()!=null)
                                                            actorRecyclerAdapter.setActors(Arrays.asList(om.getActors().split(", ")));
                                                        else {
                                                            actorRecyclerAdapter.setActors(new ArrayList<>());
                                                            binding.actorTitle.setVisibility(View.GONE);
                                                        }
                                                        actorRecyclerAdapter.notifyDataSetChanged();
                                                        binding.country.setText(om.getCountry());
                                                        binding.year.setText(om.getYear());
                                                        try {
                                                            animateValue(binding.metascore, (int) (Float.parseFloat(om.getMetascore()) * 10), null, 1000);
                                                        } catch (Exception e) {
                                                            binding.metascore.setText("-");
                                                        }
                                                        try {
                                                            animateValue(binding.userscore, (int) (Float.parseFloat(om.getImdbRating()) * 10), null, 1000);
                                                        } catch (Exception e) {
                                                            binding.userscore.setText("-");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<OMDBResponse> call, Throwable t) {
                                                    Log.d("OMDB FAIL**********", t.toString());
                                                }
                                            });
                                }}
                            @Override
                            public void onFailure(Call<MovieDetailsDTO> call, Throwable t) {
                            }
                        });
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
    private void expandFABMenu(){
        isFABOpen = true;
        binding.fabfav.animate().translationY(-140);
        binding.fabwatch.animate().translationY(-260);
        binding.fabwatch.show();
        binding.fabfav.show();
    }

    private void collapseFABMenu(){
        isFABOpen=false;
        binding.fabfav.animate().translationY(0);
        binding.fabwatch.animate().translationY(0);
        binding.fabwatch.hide();
        binding.fabfav.hide();
    }

}