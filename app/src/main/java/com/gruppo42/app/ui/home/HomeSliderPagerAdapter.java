package com.gruppo42.app.ui.home;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.gruppo42.app.R;
import com.gruppo42.app.activities.movieActivity.MovieActivity;
import com.gruppo42.app.api.models.MovieItem;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.utils.Constants;

import java.util.List;

public class HomeSliderPagerAdapter extends PagerAdapter {

    private static final String TAG = "HomeSliderPagerAdapter";

    public interface OnItemClickListener {
        void onItemClick(MovieItem movieItem);
    }

    private Context context;
    private List<ResultDTO> resultDTOList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public HomeSliderPagerAdapter(Context context, List<ResultDTO> resultDTOList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.resultDTOList = resultDTOList;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View slideLayout = this.layoutInflater.inflate(R.layout.slide_item, null);

        TextView slideTitle = slideLayout.findViewById(R.id.slideMovieTitle);
        slideTitle.setText(resultDTOList.get(position).getTitle());
        ImageView slideImg = slideLayout.findViewById(R.id.slideImage);

        String imageUrl = Constants.MOVIES_SLIDER_IMAGE_BASE_URL + resultDTOList.get(position).getBackdrop_path();

        if (imageUrl != null) {
            Glide
                    .with(slideLayout)
                    .load(imageUrl)
                    .fallback(R.drawable.backdrop_not_found)
                    .error(R.drawable.backdrop_not_found)
                    .into(slideImg);
        }

        slideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieItem movieItem = new MovieItem(resultDTOList.get(position));
                Intent intent = new Intent(context, MovieActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(slideImg, "backdrop");
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity)context, pairs);
                Bundle b = new Bundle();
                b.putCharSequence("movie", movieItem.getId()+""); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                context.startActivity(intent, options.toBundle());
            }
        });

        container.addView(slideLayout);
        return slideLayout;
    }

    @Override
    public int getCount() {
        return resultDTOList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}