package com.listing.book.jarvis.newsapp.Loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.listing.book.jarvis.newsapp.Model.Section;

import java.util.ArrayList;

public class SectionLoader extends AsyncTaskLoader {
    private String mUrl;

    public SectionLoader(Context context, String url) {
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

        ArrayList sections = SectionUtil.fetchSectionList(mUrl);
        return sections;
    }
}