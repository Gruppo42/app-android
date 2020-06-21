package com.gruppo42.app.ui.moviedetail;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.databinding.FragmentMovieDetailBinding;
import com.gruppo42.app.utils.Constants;
import com.gruppo42.app.utils.HashMapGenres;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = "MovieDetailFragment";

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
            ResultDTO resultDTO = MovieDetailFragmentArgs.fromBundle(bundle).getResultDTO();

            ImageView backdropImg = view.findViewById(R.id.detail_movie_backdrop);
            String backdropURL = Constants.MOVIES_SLIDER_IMAGE_BASE_URL + resultDTO.getBackdrop_path();
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
            String movieDateString = resultDTO.getRelease_date();
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
                countdown.setText(difference + " days left");
            }


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strToday = simpleDateFormat.format(today);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(10));
            ImageView posterImg = view.findViewById(R.id.detail_movie_poster);
            String posterURL = Constants.MOVIES_LIST_IMAGE_BASE_URL + resultDTO.getPoster_path();
            if (posterURL != null) {
                Glide
                        .with(view)
                        .load(posterURL)
                        .fallback(R.drawable.baseline_movie_white_48dp)
                        .error(R.drawable.baseline_movie_white_48dp)
                        .apply(requestOptions)
                        .into(posterImg);
            }

            binding.detailMovieTitle.setText(resultDTO.getTitle());

            binding.detailMovieYear.setText(resultDTO.getRelease_date().substring(0, 4));

            String genres = "";
            HashMapGenres hashMapGenres = new HashMapGenres();
            for (int i = 0; i < resultDTO.getGenre_ids().length; i++) {
                genres += hashMapGenres.getIdGenresToGenres().get(resultDTO.getGenre_ids()[i]);
                genres += ", ";
            }
            if (genres.length() != 0) {
                genres = genres.substring(0, genres.length() - 2);
            } else {
                genres = "No genres available";
            }
            binding.detailMovieGenres.setText(genres);

            binding.detailMovieDescription.setText(resultDTO.getOverview());
        }
    }
}
