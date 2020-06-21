package com.gruppo42.app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.Resource;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.databinding.FragmentHomeMovieSliderBinding;
import com.gruppo42.app.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrendingMoviesFragment extends Fragment {

    private static final String TAG = "TrendingMoviesFragment";

    private HomeMoviesViewModel homeMoviesViewModel;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    private List<ResultDTO> resultDTOList;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private HomeSliderPagerAdapter sliderAdapter;

    private FragmentHomeMovieSliderBinding binding;

    public TrendingMoviesFragment() { }

    public static TrendingMoviesFragment newInstance() { return new TrendingMoviesFragment(); }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeMovieSliderBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeMoviesViewModel = new HomeMoviesViewModel();

        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity(), getTrendingMovieList(Constants.TRENDING_MOVIES_MEDIA_TYPE, Constants.TRENDING_MOVIES_TIME_WINDOW), new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ResultDTO resultDTO) {
                HomeFragmentDirections.ShowMovieDetailAction action = HomeFragmentDirections.showMovieDetailAction(resultDTO);
                Navigation.findNavController(view).navigate(action);
            }
        });

        viewPager = getActivity().findViewById(R.id.trendingMoviesSlider);
        tabLayout = getActivity().findViewById(R.id.trendingMoviesTabLayout);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TrendingMoviesFragment.SliderTimer(), 4000, 6000);
        tabLayout.setupWithViewPager(viewPager, true);
        resultDTOList = new ArrayList<>();

        final Observer<Resource<List<ResultDTO>>> observer = new Observer<Resource<List<ResultDTO>>>() {
            @Override
            public void onChanged(Resource<List<ResultDTO>> trendingMoviesResource) {

                homeRecyclerViewAdapter.setData(trendingMoviesResource.getData());

                if (trendingMoviesResource.getData() != null) {
                    Log.d(TAG, "Success - Total results: " + trendingMoviesResource.getTotalResults() + " Status code: " + trendingMoviesResource.getStatusCode() + " Status message: " + trendingMoviesResource.getStatusMessage());

                    for (int i = 0; i < trendingMoviesResource.getData().size(); i++) {
                        if (trendingMoviesResource.getData().get(i) != null && trendingMoviesResource.getData().get(i).getBackdrop_path() == null) {
                            trendingMoviesResource.getData().remove(i);
                        }
                    }

                    for (int i = 0; i < Constants.SLIDER_LIMIT; i++) {
                        if (trendingMoviesResource.getData().get(i) != null) {
                            resultDTOList.add(trendingMoviesResource.getData().get(i));
                            Log.d(TAG, i + " = " + resultDTOList.get(i).getPoster_path());
                            Log.d(TAG, "Trending ResultDTO: " + trendingMoviesResource.getData().get(i).getTitle());
                        }
                    }

                    sliderAdapter = new HomeSliderPagerAdapter(getActivity(), resultDTOList, new HomeSliderPagerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ResultDTO movie) {
                            HomeFragmentDirections.ShowMovieDetailAction action = HomeFragmentDirections.showMovieDetailAction(movie);
                            Navigation.findNavController(view).navigate(action);
                        }
                    });
                    viewPager.setAdapter(sliderAdapter);

                } else {
                    Log.d(TAG, "Error - Status code: " + trendingMoviesResource.getStatusCode() + " Status message: " + trendingMoviesResource.getStatusMessage());
                }
            }
        };

        LiveData<Resource<List<ResultDTO>>> liveData = homeMoviesViewModel.getTrendingMoviesResource(Constants.TRENDING_MOVIES_MEDIA_TYPE, Constants.TRENDING_MOVIES_TIME_WINDOW);
        liveData.observe(getViewLifecycleOwner(), observer);
    }

    private List<ResultDTO> getTrendingMovieList(String media_type, String time_window) {

        Resource<List<ResultDTO>> trendingMovieListResource = homeMoviesViewModel.getTrendingMoviesResource(media_type, time_window).getValue();

        if (trendingMovieListResource != null) {
            return trendingMovieListResource.getData();
        }

        return null;
    }

    class SliderTimer extends TimerTask {

        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < resultDTOList.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }
}