package com.listing.book.jarvis.newsapp.Model;

public class News {
    private String mTitle;
    private String mDate;
    private String mSection;
    private String mThumbnail;

    private String mApiUrl;

    public News(String title, String date, String section, String thumbnail, String apiUrl) {
        mTitle = title;
        mDate = date;
        mSection = section;
        mThumbnail = thumbnail;
        mApiUrl = apiUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getApiUrl() {
        return mApiUrl;
    }
}
