package com.listing.book.jarvis.newsapp.Model;

public class Section {
    private String mId;
    private String mTitle;

    public Section(String id, String title) {
        mId = id;
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
