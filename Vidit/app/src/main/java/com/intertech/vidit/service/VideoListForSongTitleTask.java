package com.intertech.vidit.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.intertech.vidit.domain.SongQuery;
import com.intertech.vidit.domain.YouTubeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.intertech.vidit.support.ViditApplication.APP_NAME;
import static com.intertech.vidit.support.ViditApplication.GOOGLE_API_KEY;
import static com.intertech.vidit.support.ViditApplication.LOG_TAG;
import static com.intertech.vidit.support.ViditApplication.VIDEO_BROADCAST;
import static com.intertech.vidit.support.ViditApplication.VIDEO_EXTRA;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class VideoListForSongTitleTask extends AsyncTask<SongQuery, Void, ArrayList<YouTubeItem>> {

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final String SEARCH_LIST = "id,snippet";
    public static final String SEARCH_TYPE = "video";
    public static final String DEFALUT_MAX_RESULTS = "50";
    public static final String DEFAULT_SEARCH_ORDER = "relevance";

    private long maxResults;
    private String searchOrder;
    private Context context;

    public VideoListForSongTitleTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        maxResults = Long.parseLong(prefs.getString("preferences_max_videos", DEFALUT_MAX_RESULTS));
        searchOrder = prefs.getString("preference_video_order", DEFAULT_SEARCH_ORDER);
    }

    protected void onPostExecute(ArrayList<YouTubeItem> videos) {
        sendBroadcast(videos);
        if (videos != null) {
            Log.d(LOG_TAG, "Async Task complete - returning videos list, size: " + videos.size());
        } else {
            Log.d(LOG_TAG, "Async Task complete - no videos matching.");
        }
    }

    private void sendBroadcast(ArrayList<YouTubeItem> videos) {
        if (videos != null) {
            Log.d(LOG_TAG, "# of videos found on YouTube:  " + videos.size());
        } else {
            Log.d(LOG_TAG, "# of videos found on YouTube:  " + 0);
        }
        Intent intent = new Intent(VIDEO_BROADCAST);
        intent.putExtra(VIDEO_EXTRA, videos);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    protected ArrayList<YouTubeItem> doInBackground(SongQuery... query) {
        if (query.length > 0 && !isCancelled()) {
            try {
                YouTubeRequestInitializer initializer = new YouTubeRequestInitializer(GOOGLE_API_KEY);
                YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName(APP_NAME).setYouTubeRequestInitializer(initializer).build();
                YouTube.Search.List search = youtube.search().list(SEARCH_LIST);
                Log.d(LOG_TAG, "Making YouTube request for:  " + query[0].getTitle() + "+" + query[0].getArtist());
                search.setQ(query[0].getTitle() + "+" + query[0].getArtist());
                search.setType(SEARCH_TYPE);
                search.setMaxResults(maxResults);
                search.setVideoSyndicated("true");
                search.setVideoEmbeddable("true");
                search.setOrder(searchOrder);
                if (!isCancelled()) {
                    return extractResults(search.execute(), query[0]);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private ArrayList<YouTubeItem> extractResults(SearchListResponse response, SongQuery query) {
        List<SearchResult> results = response.getItems();
        // need specific ArrayList since it is Serializable/Parceable
        ArrayList<YouTubeItem> items = new ArrayList<>();
        YouTubeItem youTubeItem;

        for (SearchResult result : results) {
            youTubeItem = new YouTubeItem();
            youTubeItem.setId(result.getId().getVideoId());
            youTubeItem.setTitle(result.getSnippet().getTitle());
            youTubeItem.setDescription(result.getSnippet().getDescription());
            youTubeItem.setThumbnail(result.getSnippet().getThumbnails().getDefault().getUrl());
            youTubeItem.setSearchArtist(query.getArtist());
            youTubeItem.setSearchTitle(query.getTitle());
            items.add(youTubeItem);
        }
        return items;
    }
}
