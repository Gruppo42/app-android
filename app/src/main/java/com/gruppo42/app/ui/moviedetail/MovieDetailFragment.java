package com.gruppo42.app.ui.moviedetail;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.MovieItem;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.Resource;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.api.models.TrailerResultDTO;
import com.gruppo42.app.databinding.FragmentMovieDetailBinding;
import com.gruppo42.app.utils.Constants;
import com.gruppo42.app.utils.HashMapGenres;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = "MovieDetailFragment";

    private TrailerViewModel trailerViewModel;

    private FragmentMovieDetailBinding binding;

    public MovieDetailFragment() {

    }

    public static MovieDetailFragment newInstance() { return new MovieDetailFragment(); }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            MovieItem movieItem = MovieDetailFragmentArgs.fromBundle(bundle).getMovieItem();

            ImageView backdropImg = view.findViewById(R.id.detail_movie_backdrop);
            String backdropURL = Constants.MOVIES_SLIDER_IMAGE_BASE_URL + movieItem.getImageUrlSecondary();
            if (backdropURL != null) {
                Glide
                        .with(view)
                        .load(backdropURL)
                        .fallback(R.drawable.backdrop_not_found)
                        .error(R.drawable.backdrop_not_found)
                        .into(backdropImg);
            }

            TextView countdown = view.findViewById(R.id.countdown);
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            String movieDateString = movieItem.getDate();
            Date movieDate = null;
            try {
                movieDate = new SimpleDateFormat("yyyy-MM-dd").parse(movieDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = movieDate.getTime() - today.getTime();
            difference = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
            if (difference > 0) {
                difference++;
                ImageView gradientBgImageDownRight = view.findViewById(R.id.gradientBgImageDownRight);
                gradientBgImageDownRight.setVisibility(View.VISIBLE);
                countdown.setText(difference + " days left");
            }


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strToday = simpleDateFormat.format(today);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(10));
            ImageView posterImg = view.findViewById(R.id.detail_movie_poster);
            String posterURL = Constants.MOVIES_LIST_IMAGE_BASE_URL + movieItem.getImageUrl();
            if (posterURL != null) {
                Glide
                        .with(view)
                        .load(posterURL)
                        .fallback(R.drawable.baseline_movie_white_48dp)
                        .error(R.drawable.baseline_movie_white_48dp)
                        .apply(requestOptions)
                        .into(posterImg);
            }

            binding.detailMovieTitle.setText(movieItem.getTitle());

            binding.detailMovieYear.setText(movieItem.getYear());

            binding.detailMovieGenres.setText(movieItem.getStrGenres());

            binding.detailMovieDescription.setText(movieItem.getDescription());

            ImageButton trailerButton = view.findViewById(R.id.detail_movie_trailer);

            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trailerViewModel = new TrailerViewModel();
                    Log.d(TAG, "prova: " + movieItem.getId());
                    LiveData<Resource<List<TrailerResultDTO>>> liveData = trailerViewModel.getVideosResource(movieItem.getId());

                    Log.d(TAG, "cacca: " + liveData);
                    Log.d(TAG, "cacca 2: " + liveData.getValue());
                    Log.d(TAG, "cacca 3: " + liveData.getValue().getData());
                }
            });
        }
    }

    private List<TrailerResultDTO> getVideoList(int movie_id) {
        Resource<List<TrailerResultDTO>> videoListResource = trailerViewModel.getVideosResource(movie_id).getValue();

        if (videoListResource != null) {
            return videoListResource.getData();
        }

        return null;
    }
}
