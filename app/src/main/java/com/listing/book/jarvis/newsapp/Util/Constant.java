package com.listing.book.jarvis.newsapp.Util;

import android.app.AlertDialog;
import android.content.Context;

import com.listing.book.jarvis.newsapp.Loader.NewsLoader;
import com.listing.book.jarvis.newsapp.MainActivity;

import dmax.dialog.SpotsDialog;

public class Constant {
    public static String subString(String text) {
        if (text.length() > 45)
            return text.substring(0, 45) + "...";
        return text;
    }
}
