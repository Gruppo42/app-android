package com.gruppo42.app.ui.profile.filmrecyclergrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class FilmImageView extends ImageView {

    private int valx, valy;
    private boolean first;

    public FilmImageView(Context context) {
        super(context);
    }

    public FilmImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilmImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        double aspect_ratio = 94.0/141.0;
        int height = (int)(((double)getMeasuredWidth())/aspect_ratio);
        //setMeasuredDimension(getMeasuredWidth(), height); //Snap to width
        if(!first)
        {
            this.valx = getMeasuredWidth();
            this.valy = height;
        }
        else {
            setMeasuredDimension(this.valx, this.valy);
            return;
        }
        setMeasuredDimension(getMeasuredWidth(), height);
    }
}