package com.listing.book.jarvis.newsapp.List;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_title;
    public TextView tv_date;
    public TextView tv_section;
    public CardView card_view;

    public ImageView img_thumbnail;


    public NewsViewHolder(View itemView) {
        super(itemView);
    }
}
