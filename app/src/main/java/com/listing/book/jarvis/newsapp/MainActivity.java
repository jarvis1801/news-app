package com.listing.book.jarvis.newsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.listing.book.jarvis.newsapp.List.NewsAdapter;
import com.listing.book.jarvis.newsapp.Loader.NewsLoader;
import com.listing.book.jarvis.newsapp.Loader.SectionLoader;
import com.listing.book.jarvis.newsapp.Model.News;
import com.listing.book.jarvis.newsapp.Model.Section;


import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.HttpUrl;

public class MainActivity extends FragmentActivity {
    private static final int NEWS_LOADER_ID = 2;
    private static final int NEWS_SECTION_ID = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout swipe;

    private MaterialSpinner spinner_sections;
    private ArrayList<String> sectionsTitleList = new ArrayList<>();
    private HashMap<String, String> sectionsMap = new HashMap();
    private int spinnerPosition = 0;
    private String section = "";

    private AlertDialog alertDialog;
    private ProgressBar progress_bar;

    private int currentPage = 1;
    private boolean pageNoResponse = false;
    private boolean loading = false;

    private LoaderManager loaderManager;

    private ArrayList newsList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_listing);

        alertDialog = new SpotsDialog.Builder().setContext(MainActivity.this).build();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        spinner_sections = (MaterialSpinner) findViewById(R.id.spinner_sections);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        loaderManager = getSupportLoaderManager();

        if (isConnected) {
            loaderManager.initLoader(NEWS_SECTION_ID, null, getNewsSectionsList).forceLoad();
            loaderManager.initLoader(NEWS_LOADER_ID, null, getNewsList).forceLoad();
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && loading == false) {
                    if (!pageNoResponse) {
                        loaderManager.restartLoader(NEWS_LOADER_ID, null, getNewsList).forceLoad();
                        loading = true;
                    }
                }
            }
        });

        spinner_sections.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                resetNewsAdapter();
                currentPage = 1;

                section = sectionsMap.get(sectionsTitleList.get(position));

                loaderManager.initLoader(NEWS_LOADER_ID, null, getNewsList).forceLoad();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetNewsAdapter();
                currentPage = 1;
                loaderManager.initLoader(NEWS_LOADER_ID, null, getNewsList).forceLoad();
            }
        });
    }


    private LoaderManager.LoaderCallbacks<ArrayList<News>> getNewsList
            = new LoaderManager.LoaderCallbacks<ArrayList<News>>() {
        @Override
        public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
            if (currentPage == 1)
                showDialog("Loading ...");
            else
                showLoadingIcon();
            String url = Config.url + "search";

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("api-key", Config.API_KEY);
            urlBuilder.addQueryParameter("page-size", Config.LOADING_PAGE_SIZE);
            urlBuilder.addQueryParameter("show-elements", "all");
            urlBuilder.addQueryParameter("show-fields", "thumbnail");
            urlBuilder.addQueryParameter("page", String.valueOf(currentPage));
            if (!section.equals("")) {
                urlBuilder.addQueryParameter("section", section);
            }

            return new NewsLoader(MainActivity.this, urlBuilder.toString());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<News>> loader, ArrayList<News> news) {
            if (swipe.isRefreshing()) {
                swipe.setRefreshing(false);
            }
            if (news == null) {
                loadingError();
                pageNoResponse = true;
                if (currentPage == 1)
                    dismissDialog();
                else
                    hideLoadingIcon();
                return;
            }
            newsList.addAll(news);
            if (currentPage == 1)
                dismissDialog();
            else
                hideLoadingIcon();
            updateUI();
            loading = false;
            loaderManager.destroyLoader(NEWS_LOADER_ID);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<News>> loader) {
            if (swipe.isRefreshing()) {
                swipe.setRefreshing(false);
            }
            if (currentPage == 1)
                dismissDialog();
            else
                hideLoadingIcon();
            loadingError();
        }
    };

    private LoaderManager.LoaderCallbacks<ArrayList<Section>> getNewsSectionsList
            = new LoaderManager.LoaderCallbacks<ArrayList<Section>>() {
        @Override
        public Loader<ArrayList<Section>> onCreateLoader(int id, Bundle args) {
            String url = Config.url + "sections";
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("api-key", Config.API_KEY);

            return new SectionLoader(MainActivity.this, urlBuilder.toString());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<Section>> loader, ArrayList<Section> sections) {
            setSectionList(sections);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<Section>> loader) {
            loadingError();
        }
    };

    private void showDialog(String msg) {
        alertDialog.setMessage(msg);
        alertDialog.show();
    }

    private void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void showLoadingIcon() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIcon() {
        progress_bar.setVisibility(View.GONE);
    }

    private void loadingError() {
        Toasty.error(this, "Network or api problem. Please try again later.", Toast.LENGTH_SHORT, true).show();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new NewsAdapter(getApplicationContext(), newsList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        currentPage++;
    }

    private void resetNewsAdapter() {
        newsList.removeAll(newsList);
        mAdapter.notifyDataSetChanged();
    }

    private void setSectionList(ArrayList<Section> sections) {
        sectionsTitleList.add("All");
        sectionsMap.put("All", "");
        for (int i = 0; i < sections.size(); i++) {
            sectionsTitleList.add(sections.get(i).getTitle());
            sectionsMap.put(sections.get(i).getTitle(), sections.get(i).getId());
        }
        spinner_sections.setItems(sectionsTitleList);
    }
}