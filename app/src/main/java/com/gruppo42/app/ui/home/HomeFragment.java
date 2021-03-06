package com.gruppo42.app.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gruppo42.app.R;
import com.gruppo42.app.utils.Constants;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        insertMoviesFragment();
    }

    private void insertMoviesFragment() {

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strTomorrow = simpleDateFormat.format(tomorrow);
        Log.d(TAG, "insertMoviesFragment: " + strTomorrow);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutTrending, TrendingMoviesFragment.newInstance());
                fragmentTransaction.replace(R.id.frameLayoutUpcoming, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, strTomorrow, null));
                fragmentTransaction.replace(R.id.frameLayoutAction, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.ACTION));
                fragmentTransaction.replace(R.id.frameLayoutAdventure, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.ADVENTURE));
                fragmentTransaction.replace(R.id.frameLayoutAnimation, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.ANIMATION));
                fragmentTransaction.replace(R.id.frameLayoutComedy, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.COMEDY));
                fragmentTransaction.replace(R.id.frameLayoutCrime, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.CRIME));
                fragmentTransaction.replace(R.id.frameLayoutDocumentary, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.DOCUMENTARY));
                fragmentTransaction.replace(R.id.frameLayoutDrama, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.DRAMA));
                fragmentTransaction.replace(R.id.frameLayoutFamily, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.FAMILY));
                fragmentTransaction.replace(R.id.frameLayoutFantasy, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.FANTASY));
                fragmentTransaction.replace(R.id.frameLayoutHistory, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.HISTORY));
                fragmentTransaction.replace(R.id.frameLayoutHorror, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.HORROR));
                fragmentTransaction.replace(R.id.frameLayoutMusic, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.MUSIC));
                fragmentTransaction.replace(R.id.frameLayoutMystery, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.MYSTERY));
                fragmentTransaction.replace(R.id.frameLayoutRomance, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.ROMANCE));
                fragmentTransaction.replace(R.id.frameLayoutScienceFiction, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.SCIENCE_FICTION));
                fragmentTransaction.replace(R.id.frameLayoutThriller, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.THRILLER));
                fragmentTransaction.replace(R.id.frameLayoutWar, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.WAR));
                fragmentTransaction.replace(R.id.frameLayoutWestern, MoviesByDateOrGenreFragment.newInstance(Constants.MOVIES_BY_GENRE_PAGE, null, Constants.WESTERN));
                fragmentTransaction.commit();
            }
        });
    }
}
