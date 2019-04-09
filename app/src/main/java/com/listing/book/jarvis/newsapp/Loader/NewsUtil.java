package com.listing.book.jarvis.newsapp.Loader;

import android.text.TextUtils;
import android.util.Log;

import com.listing.book.jarvis.newsapp.Model.NewDetail;
import com.listing.book.jarvis.newsapp.Model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsUtil {
    private static final String LOG_TAG = "NewsUtil";

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    public static ArrayList<News> fetchNewsList(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to get data.");
            return null;
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<News> extractFeatureFromJson(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }

        ArrayList<News> newsList = new ArrayList<>();

        try {
            JSONObject baseJson = new JSONObject(data);

            JSONObject response = baseJson.getJSONObject("response");

            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentNews = resultArray.getJSONObject(i);

                String title = currentNews.optString("webTitle");

                String date = currentNews.optString("webPublicationDate");

                String sectionName = currentNews.optString("sectionName");

                String apiUrl = currentNews.optString("apiUrl");

                JSONObject fields = currentNews.optJSONObject("fields");

                String thumbnail = null;
                if (fields != null) {
                    thumbnail = fields.optString("thumbnail");
                }
                newsList.add(new News(title, date, sectionName, thumbnail, apiUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    public static NewDetail fetchNewDetail(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        NewDetail news = extractNewDetailFromJson(jsonResponse);

        return news;
    }

    private static NewDetail extractNewDetailFromJson(String data) {
        String responseString = "";
        ArrayList imageList = new ArrayList();

        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            JSONObject baseJson = new JSONObject(data);

            JSONObject response = baseJson.getJSONObject("response");

            JSONObject content = response.getJSONObject("content");

            JSONObject blocks = content.getJSONObject("blocks");

            JSONArray body = blocks.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                JSONObject currentObj = body.getJSONObject(i);

                responseString += currentObj.optString("bodyHtml");

                JSONArray element = currentObj.getJSONArray("elements");

                for (int e = 0; e < element.length(); e++) {
                    JSONObject currentElement = element.getJSONObject(e);

                    JSONArray assets = currentElement.getJSONArray("assets");
                    for (int y = 0; y < assets.length(); y++) {
                        JSONObject currentAsset = assets.getJSONObject(y);


                        if (currentAsset.optString("type").equals("image")) {
                            imageList.add(currentAsset.optString("file"));
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new NewDetail(responseString, imageList);
    }
}
