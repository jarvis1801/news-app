package com.listing.book.jarvis.newsapp.Loader;

import android.text.TextUtils;
import android.util.Log;

import com.listing.book.jarvis.newsapp.Model.News;
import com.listing.book.jarvis.newsapp.Model.Section;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SectionUtil {
    private static final String LOG_TAG = "SectionUtil";

        private static URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Problem building the URL ", e);
            }
            return url;
        }

        public static ArrayList<Section> fetchSectionList(String requestUrl) {
            URL url = createUrl(requestUrl);

            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            ArrayList<Section> sections = extractFeatureFromJson(jsonResponse);

            return sections;
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

        private static ArrayList<Section> extractFeatureFromJson(String data) {
            if (TextUtils.isEmpty(data)) {
                return null;
            }

            ArrayList<Section> sectionsList = new ArrayList<>();

            try {
                JSONObject baseJson = new JSONObject(data);

                JSONObject response = baseJson.getJSONObject("response");

                JSONArray resultArray = response.getJSONArray("results");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject currentNews = resultArray.getJSONObject(i);

                    String id = currentNews.optString("id");

                    String title = currentNews.optString("webTitle");

                    sectionsList.add(new Section(id, title));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sectionsList;
        }
}
