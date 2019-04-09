package com.listing.book.jarvis.newsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.listing.book.jarvis.newsapp.ImageSlider.CustomPager;
import com.listing.book.jarvis.newsapp.ImageSlider.ImageSliderPageAdapter;
import com.listing.book.jarvis.newsapp.Loader.NewDetailLoader;
import com.listing.book.jarvis.newsapp.Loader.NewsLoader;
import com.listing.book.jarvis.newsapp.Model.NewDetail;
import com.listing.book.jarvis.newsapp.Model.News;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.HttpUrl;

public class DetailActivity extends FragmentActivity {
    private TextView tv_content;
    private ScrollView sv_scroll;
    private AlertDialog alertDialog;
    private Timer timer;
    private static int current_image_position = 0;
    private CustomPager vp_image_slider;
    private ImageSliderPageAdapter imageSliderAdapter;
    private ArrayList imageList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        alertDialog = new SpotsDialog.Builder().setContext(DetailActivity.this).build();

        sv_scroll = (ScrollView) findViewById(R.id.sv_scroll);

        tv_content = findViewById(R.id.tv_content);
        vp_image_slider = (CustomPager) findViewById(R.id.vp_image_slider);

        final LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1, null, getNewsDetail).forceLoad();
    }


    private LoaderManager.LoaderCallbacks<NewDetail> getNewsDetail
            = new LoaderManager.LoaderCallbacks<NewDetail>() {
        @Override
        public Loader<NewDetail> onCreateLoader(int id, Bundle args) {
            Intent intent = getIntent();
            String apiUrl = intent.getStringExtra("apiUrl");
            Log.e("error", apiUrl);

            showDialog("Loading ...");

            HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
            urlBuilder.addQueryParameter("api-key", Config.API_KEY);
            urlBuilder.addQueryParameter("show-blocks", "body");

            return new NewDetailLoader(DetailActivity.this, urlBuilder.toString());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<NewDetail> loader, NewDetail newDetail) {
            if (newDetail == null) {
                loadingError();
            }
            dismissDialog();
            imageList = newDetail.getImageList();
            imageSliderAdapter = new ImageSliderPageAdapter(DetailActivity.this, imageList);
            vp_image_slider.setAdapter(imageSliderAdapter);
            createSlideShow();

            Log.e("error", "" + imageSliderAdapter.getCount());
            tv_content.setText(Html.fromHtml(newDetail.getContent()));
        }

        @Override
        public void onLoaderReset(@NonNull Loader<NewDetail> loader) {
            loadingError();
            dismissDialog();
        }
    };

    private void showDialog(String msg) {
        alertDialog.setMessage(msg);
        alertDialog.show();
    }

    private void loadingError() {
        Toasty.error(this, "Network or api problem. Please try again later.", Toast.LENGTH_SHORT, true).show();
    }

    private void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void createSlideShow() {
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (current_image_position == imageList.size()) {
//                    current_image_position = 0;
//                }
//
//                if (imageList.size() > 1) {
//                    vp_image_slider.setCurrentItem(current_image_position++);
//                } else {
//                    vp_image_slider.setCurrentItem(0);
//                }
//            }
//        };
//
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(runnable);
//            }
//        }, 1500, 3000);
    }

    public static int getCurrentImagePosition() {
        return current_image_position;
    }
}
