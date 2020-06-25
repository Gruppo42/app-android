package com.gruppo42.app.ui.home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gruppo42.app.R;
import com.gruppo42.app.api.models.MovieItem;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.Resource;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.databinding.FragmentHomeMovieListBinding;
import com.gruppo42.app.ui.moviedetail.MovieDetailFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoviesByDateOrGenreFragment extends Fragment {

    private static final String TAG = "MoviesByDateOrGenreFragment";

    private HomeMoviesViewModel homeMoviesViewModel;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    private FragmentHomeMovieListBinding binding;

    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleItemCount;
    private int threshold = 1;

    private int page;
    private String today;
    private String with_genres;



    public MoviesByDateOrGenreFragment() {}

    public MoviesByDateOrGenreFragment(int page, String today, String with_genres) {
        this.page = page;
        this.today = today;
        this.with_genres = with_genres;
    }

    public static MoviesByDateOrGenreFragment newInstance(int page, String today, String with_genres) { return new MoviesByDateOrGenreFragment(page, today, with_genres); }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeMovieListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeMoviesViewModel = new HomeMoviesViewModel();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.moviesRecyclerView.setLayoutManager(layoutManager);

        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity(), getMovieByDateOrGenreList(with_genres), new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieItem movieItem) {
                HomeFragmentDirections.ShowMovieDetailAction action = HomeFragmentDirections.showMovieDetailAction(movieItem);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.moviesRecyclerView.setNestedScrollingEnabled(true);
        binding.moviesRecyclerView.setAdapter(homeRecyclerViewAdapter);
        //recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10); DA CHIAMARE PER OGNI RECYCLER VIEW (per ottimizzare)
        binding.moviesRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 20);
        binding.moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleItemCount = layoutManager.getChildCount();

                if (totalItemCount == visibleItemCount || (
                        totalItemCount <= (lastVisibleItem + threshold) && dx > 0 && !homeMoviesViewModel.isLoading()) &&
                        homeMoviesViewModel.getMoviesLiveData().getValue() != null &&
                        homeMoviesViewModel.getCurrentResults() != homeMoviesViewModel.getMoviesLiveData().getValue().getTotalResults()
                        ) {
                            Resource<List<ResultDTO>> movieListResource = new Resource<>();

                            MutableLiveData<Resource<List<ResultDTO>>> movieListMutableLiveData = homeMoviesViewModel.getMoviesLiveData();

                            if (movieListMutableLiveData.getValue() != null) {

                                homeMoviesViewModel.setLoading(true);

                                List<ResultDTO> currentResultDTOList = movieListMutableLiveData.getValue().getData();

                                currentResultDTOList.add(null);
                                movieListResource.setData(currentResultDTOList);
                                movieListResource.setStatusMessage(movieListMutableLiveData.getValue().getStatusMessage());
                                movieListResource.setTotalResults(movieListMutableLiveData.getValue().getTotalResults());
                                movieListResource.setStatusCode(movieListMutableLiveData.getValue().getStatusCode());
                                movieListResource.setLoading(true);
                                movieListMutableLiveData.postValue(movieListResource);

                                int page = homeMoviesViewModel.getPage() + 1;
                                homeMoviesViewModel.setPage(page);

                                homeMoviesViewModel.getMoreMoviesByDateOrGenreResource(today, with_genres);
                            }
                }
            }
        });

        final Observer<Resource<List<ResultDTO>>> observer = new Observer<Resource<List<ResultDTO>>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onChanged(Resource<List<ResultDTO>> moviesByDateOrGenreResource) {

                homeRecyclerViewAdapter.setData(moviesByDateOrGenreResource.getData());

                if(!moviesByDateOrGenreResource.isLoading()) {
                    homeMoviesViewModel.setLoading(false);
                    if (moviesByDateOrGenreResource.getData() != null) {
                        homeMoviesViewModel.setCurrentResults(moviesByDateOrGenreResource.getData().size());
                    }
                }

                if (moviesByDateOrGenreResource.getData() != null) {
                    Log.d(TAG, "Success - Total results: " + moviesByDateOrGenreResource.getTotalResults() + " Status code: " + moviesByDateOrGenreResource.getStatusCode() + "Status message: " + moviesByDateOrGenreResource.getStatusMessage());

                    for (int i = 0; i < moviesByDateOrGenreResource.getData().size(); i++) {
                        if (moviesByDateOrGenreResource.getData().get(i) != null && moviesByDateOrGenreResource.getData().get(i).getPoster_path() == null) {
                            Log.d(TAG, "Action ResultDTO: " + moviesByDateOrGenreResource.getData().get(i).getPoster_path());
                            moviesByDateOrGenreResource.getData().remove(i);
                        }
                    }
                } else {
                    Log.d(TAG, "Error - Status code: " + moviesByDateOrGenreResource.getStatusCode() + " Status message: " + moviesByDateOrGenreResource.getStatusMessage());
                }
            }
        };

        LiveData<Resource<List<ResultDTO>>> liveData = homeMoviesViewModel.getMoviesByDateOrGenreResource(today, with_genres);
        liveData.observe(getViewLifecycleOwner(), observer);
    }

    private List<ResultDTO> getMovieByDateOrGenreList(String with_genres) {

        Resource<List<ResultDTO>> movieByGenreListResource = homeMoviesViewModel.getMoviesByDateOrGenreResource(today, with_genres).getValue();

        if (movieByGenreListResource != null) {
            return movieByGenreListResource.getData();
        }

        return null;
    }
}
