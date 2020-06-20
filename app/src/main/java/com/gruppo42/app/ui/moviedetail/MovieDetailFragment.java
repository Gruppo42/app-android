package com.gruppo42.app.ui.moviedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.databinding.FragmentMovieDetailBinding;

import org.jetbrains.annotations.NotNull;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            ResultDTO resultDTO = MovieDetailFragmentArgs.fromBundle(bundle).getResultDTO();
            binding.textViewMovieTitle.setText(resultDTO.getTitle());
        }
    }
}
