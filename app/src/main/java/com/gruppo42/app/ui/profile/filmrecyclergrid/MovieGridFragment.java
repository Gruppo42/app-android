package com.gruppo42.app.ui.profile.filmrecyclergrid;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruppo42.app.R;

import java.util.Arrays;
import java.util.List;

public class MovieGridFragment extends Fragment {

    private List<String> movieIDS;
    private MovieGridAdapter adapter;

    public static MovieGridFragment newInstance() {
        return new MovieGridFragment();
    }

    public MovieGridFragment(List<String> movieIDS) {
        this.movieIDS = movieIDS;
        Log.d("MovieGrid", "Created with " + movieIDS.size() + " elements");
    }

    public MovieGridFragment(){
        this.movieIDS = Arrays.asList("458156", "458156", "458156", "458156", "458156", "458156", "458156", "458156", "458156", "458156");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_grid_fragment, container, false);
        TextView nothingFound = root.findViewById(R.id.textViewNothing);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerGrid);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false));
        this.adapter = new MovieGridAdapter(this.getContext(), this.movieIDS);
        recyclerView.setAdapter(adapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 20);
        int spanCount = 4; // 3 columns
        int spacing = 5; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        if(this.movieIDS.size()==0)
            nothingFound.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public List<String> getMovieIDS() {
        return movieIDS;
    }

    public void setMovieIDS(List<String> movieIDS) {
        this.movieIDS = movieIDS;
    }

}