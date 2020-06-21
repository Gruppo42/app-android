package com.gruppo42.app.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.gruppo42.app.api.models.ResultDTO;
import com.gruppo42.app.utils.Constants;

import java.util.List;

public class HomeSliderPagerAdapter extends PagerAdapter {

    private static final String TAG = "HomeSliderPagerAdapter";

    public interface OnItemClickListener {
        void onItemClick(ResultDTO resultDTO);
    }

    private List<ResultDTO> resultDTOList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public HomeSliderPagerAdapter(Context context, List<ResultDTO> resultDTOList, OnItemClickListener onItemClickListener) {
        this.resultDTOList = resultDTOList;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View slideLayout = this.layoutInflater.inflate(R.layout.slide_item, null);

        ImageView slideImg = slideLayout.findViewById(R.id.slideImage);

        String imageUrl = Constants.MOVIES_SLIDER_IMAGE_BASE_URL + resultDTOList.get(position).getPoster_path();
        String imageNewUrl = null;

        if (imageUrl != null) {
            imageNewUrl = imageUrl.replace("http://", "https://").trim();
            Glide
                    .with(slideLayout)
                    .load(imageNewUrl)
                    .fallback(R.drawable.baseline_movie_white_48dp)
                    .error(R.drawable.baseline_movie_white_48dp)
                    .into(slideImg);
        }

        slideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(resultDTOList.get(position));
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