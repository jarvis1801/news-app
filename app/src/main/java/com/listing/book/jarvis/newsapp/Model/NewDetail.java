package com.listing.book.jarvis.newsapp.Model;

import java.util.ArrayList;

public class NewDetail extends Object {
    private String mContent;
    private ArrayList mImageList;

    public NewDetail(String content, ArrayList imageList) {
        mContent = content;
        mImageList = imageList;
    }

    public String getContent() {
        return mContent;
    }

    public ArrayList getImageList() {
        return mImageList;
    }
}
