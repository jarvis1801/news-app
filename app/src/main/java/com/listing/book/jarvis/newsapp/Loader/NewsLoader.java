package com.listing.book.jarvis.newsapp.Loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.listing.book.jarvis.newsapp.Model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader {
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList loadInBackground() {
        if (mUrl == null)
            return null;

        ArrayList<News> news = NewsUtil.fetchNewsList(mUrl);
        return news;
    }
}
