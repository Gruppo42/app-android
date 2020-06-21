package com.gruppo42.app.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gruppo42.app.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private PagerAdapter adapter;
    private TextView nameSurname;
    private TextView username;
    private TextView watchlistCount;
    private TextView favlistCount;
    private ImageView imageView;
    private List<String> favList;
    private List<String> watchList;
    private String token;
    private ViewGroup container;

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("user", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkyNjgwOTM3LCJleHAiOjE1OTMyODU3Mzd9.CaaPDEMMwCaszk8PhMdHZoSl1fK7apQ-zGk0ua5gJrnA_705TiJ0mqdUs7aFGIbNAMRWO0zqi4Sgb6jSHZcCNw");
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        this.nameSurname = root.findViewById(R.id.textViewNameSurname);
        this.username = root.findViewById(R.id.textViewUsername);
        this.watchlistCount = root.findViewById(R.id.textViewWatchlistCount);
        this.favlistCount = root.findViewById(R.id.textViewFavoritesCount);
        this.imageView = root.findViewById(R.id.imageViewProfilePic);
        ViewPager2 pager = root.findViewById(R.id.pager);
        this.favList = new ArrayList<>();
        this.watchList = new ArrayList<>();
        adapter = new PagerAdapter(this, favList, watchList);
        pager.setAdapter(adapter);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                AnimatedVectorDrawable icon = (AnimatedVectorDrawable) tab.getIcon();
                icon.start();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                AnimatedVectorDrawable icon = (AnimatedVectorDrawable) tab.getIcon();
                icon.start();
            }
        });
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> addIcon(tab, position));
        mediator.attach();
        profileViewModel = new ProfileViewModel(token);
        initObservers();
        return root;
    }

    public void addIcon(TabLayout.Tab tab, int position)
    {
        if (position == 0) {
            tab.setIcon(R.drawable.avd_heart);
        } else {
            tab.setIcon(R.drawable.avd_eye);
        }
    }

    private void initObservers()
    {
        profileViewModel.getEmail().observeForever(s -> {
        });
        profileViewModel.getName().observeForever(s -> {
            this.nameSurname.setText(s + " " + profileViewModel.getName().getValue());
        });
        profileViewModel.getUsername().observeForever(s -> {
            this.username.setText("@"+s);
        });
        profileViewModel.getProfileImage().observeForever(s -> {
            byte[] decodedString = Base64.decode(s, Base64.DEFAULT);
            //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(this.container.getContext())
                    .asBitmap()
                    .load(decodedString)
                    .placeholder(new ColorDrawable(Color.GRAY))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .into(imageView);
        });
        profileViewModel.getFavorites().observeForever(l -> {
            this.adapter.setFavList(l);
            this.favlistCount.setText(new String(l.size()+""));
            adapter.notifyDataSetChanged();
        });

        profileViewModel.getWatchlist().observeForever(l -> {
            this.adapter.setWatchlist(l);
            this.watchlistCount.setText(new String(l.size()+""));
            adapter.notifyDataSetChanged();
        });
    }
}
