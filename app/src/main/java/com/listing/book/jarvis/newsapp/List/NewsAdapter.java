package com.listing.book.jarvis.newsapp.List;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.listing.book.jarvis.newsapp.DetailActivity;
import com.listing.book.jarvis.newsapp.Model.News;
import com.listing.book.jarvis.newsapp.R;
import com.listing.book.jarvis.newsapp.Util.Constant;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private ArrayList<News> mData;
    private Context mContext;

    public NewsAdapter(Context context, ArrayList<News> data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = (View) LayoutInflater.from(mContext)
                .inflate(R.layout.news, parent, false);

        NewsViewHolder holder = new NewsViewHolder(view);

        holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
        holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
        holder.tv_section = (TextView) view.findViewById(R.id.tv_section);
        holder.img_thumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);
        holder.card_view = (CardView) view.findViewById(R.id.card_view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        final News currentNews = (News) mData.get(position);

        holder.tv_title.setText(Constant.subString(currentNews.getTitle()));

        DateTime dt = new DateTime(currentNews.getDate());
        holder.tv_date.setText(dt.toString("d MMM, YYYY"));
        holder.tv_section.setText(currentNews.getSection());

        Glide.with(mContext)
                .load(currentNews.getThumbnail())
                .into(holder.img_thumbnail);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailActivity.class);
                i.putExtra("apiUrl", currentNews.getApiUrl());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
