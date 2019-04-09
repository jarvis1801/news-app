package com.listing.book.jarvis.newsapp.Loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.listing.book.jarvis.newsapp.Model.NewDetail;
import com.listing.book.jarvis.newsapp.Model.News;

import java.util.ArrayList;

public class NewDetailLoader extends AsyncTaskLoader {
    private String mUrl;

    public NewDetailLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public NewDetail loadInBackground() {
        if (mUrl == null)
            return null;

        NewDetail news = NewsUtil.fetchNewDetail(mUrl);
        return news;
    }
}
