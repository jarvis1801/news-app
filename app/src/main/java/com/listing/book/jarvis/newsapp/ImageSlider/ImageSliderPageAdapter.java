package com.listing.book.jarvis.newsapp.ImageSlider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.listing.book.jarvis.newsapp.DetailActivity;
import com.listing.book.jarvis.newsapp.Model.NewDetail;
import com.listing.book.jarvis.newsapp.R;

import java.util.ArrayList;

public class ImageSliderPageAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList mImageList;

    public ImageSliderPageAdapter(Context context, ArrayList imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public int getCount() {
        return mImageList.size() > 0 ? mImageList.size() : 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (ConstraintLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = mInflater.inflate(R.layout.image_slider_layout, container, false);
        ImageView img_slider = (ImageView) v.findViewById(R.id.img_slider);
        TextView tv_no_image = (TextView) v.findViewById(R.id.tv_no_image);

        if (mImageList.size() > 0) {
            Glide.with(mContext)
                    .load(mImageList.get(position))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .fitCenter())
                    .into(img_slider);
        } else {
            tv_no_image.setText("No image");
        }


        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
