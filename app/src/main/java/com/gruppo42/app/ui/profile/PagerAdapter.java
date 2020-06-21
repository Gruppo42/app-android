package com.gruppo42.app.ui.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gruppo42.app.ui.profile.filmrecyclergrid.MovieGridFragment;

import java.util.List;

public class PagerAdapter extends FragmentStateAdapter {

    private List<String> favList;
    private List<String> watchlist;

    public PagerAdapter(@NonNull Fragment fragment, List<String> favoritesList, List<String> watchList) {
        super(fragment);
        this.favList = favoritesList;
        this.watchlist = watchList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0)
            return new MovieGridFragment(favList);
        else
            return new MovieGridFragment(watchlist);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public List<String> getFavList() {
        return favList;
    }

    public void setFavList(List<String> favList) {
        this.favList = favList;
    }

    public List<String> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(List<String> watchlist) {
        this.watchlist = watchlist;
    }
}
