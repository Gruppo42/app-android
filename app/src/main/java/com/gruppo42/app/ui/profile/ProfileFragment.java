package com.gruppo42.app.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gruppo42.app.R;
import com.gruppo42.app.ui.dialogs.ChangeListener;
import com.gruppo42.app.ui.profile.dialogs.editor.EditorFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private PagerAdapter adapter;
    private TextView nameSurname;
    private TextView username;
    private TextView watchlistCount;
    private TextView favlistCount;
    private ImageView imageView;
    private byte[] decodedString;
    private String email;
    private Button editButton;
    private List<String> favList;
    private List<String> watchList;
    private String token;
    private ViewGroup container;
    private ShimmerFrameLayout shimmer;

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("user", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkyNzk1ODg3LCJleHAiOjE1OTM0MDA2ODd9.HadRp2srca8WlO3VcVr1x5CLOT6i3USoYLO8HTZyjiHtenupH7BBkO7KV_7hznacTIDCQhWL6oHovvee5Nzkeg");
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        this.nameSurname = root.findViewById(R.id.textViewNameSurname);
        this.username = root.findViewById(R.id.textViewUsername);
        this.shimmer = root.findViewById(R.id.shimmer_view_container);
        this.watchlistCount = root.findViewById(R.id.textViewWatchlistCount);
        this.favlistCount = root.findViewById(R.id.textViewFavoritesCount);
        this.imageView = root.findViewById(R.id.imageViewProfilePic);
        Glide.with(getContext())
                .asDrawable()
                .load(new ColorDrawable(Color.GRAY))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(imageView);
        this.editButton = root.findViewById(R.id.buttonEdit);
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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                if(drawable==null)
                    return;
                Bitmap bitmap = drawable.getBitmap();
                ///EditorFragment dialogEditor = new EditorFragment(bitmap, nameSurname.getText()+"", username.getText()+"", email);
                EditorFragment dialogEditor = new EditorFragment(decodedString, nameSurname.getText()+"", username.getText()+"", email);
                dialogEditor.show(getParentFragmentManager(), "tag");
                dialogEditor.setImageListener(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        Glide.with(getContext())
                                .asBitmap()
                                .load((byte[])object)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .circleCrop()
                                .into(imageView);
                    }
                });
                dialogEditor.setMailListener(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                    }
                });
                dialogEditor.setNameListener(new ChangeListener() {
                    @Override
                    public void onChange(Object object) {
                        nameSurname.setText((String)object);
                    }
                });
            }
        });
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
            this.email = profileViewModel.getEmail().getValue();
        });
        profileViewModel.getName().observeForever(s -> {
            this.nameSurname.setText(profileViewModel.getName().getValue());
        });
        profileViewModel.getUsername().observeForever(s -> {
            this.username.setText("@"+s);
        });
        profileViewModel.getProfileImage().observeForever(s -> {
            if(s==null || s.length()==0)
                return;
            decodedString = Base64.decode(s, Base64.DEFAULT);
            //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(this.container.getContext())
                    .asBitmap()
                    .load(decodedString)
                    .placeholder(new ColorDrawable(Color.GRAY))
                    .error(new ColorDrawable(Color.GRAY))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .addListener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
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
