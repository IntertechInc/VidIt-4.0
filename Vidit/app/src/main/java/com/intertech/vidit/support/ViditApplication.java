package com.intertech.vidit.support;

import android.app.Application;
import android.preference.PreferenceManager;

import com.intertech.vidit.R;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class ViditApplication extends Application {

    public static final String APP_NAME = "Vid-it";
    //public static final String LOG_TAG = "Vid-it";
    public static final String TEST_LOG_TAG = "Vid-it-testing";

    public static final String AUDIO_MIME_TYPE = "audio/*";
    public static final String GOOGLE_API_KEY = "AIzaSyC64yyVrXOuVNbG5m9nUsj7DrO3CWnFDbk";
    public static final String YOU_TUBE_URL = "https://www.youtube.com/watch?v=";

    // Extra Data keys
    public static final String VIDEO_EXTRA = "video";

    public static final String SONG_QUERY = "query";

    // Broadcast Intent
    public static final String VIDEO_BROADCAST = "vidit.intertech.com.videos";

    // Intent IDs
    public static final int FIND_SONG_INTENT = 10;
    public static final int PLAY_SONG_INTENT = 20;

    // DB fields
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ARTIST = "lib_artist";
    public static final String KEY_TITLE = "lib_title";
    public static final String KEY_URI = "video_uri";
    public static final String KEY_DESCRIPTION = "video_description";
    public static final String KEY_VIDEO_TITLE = "video_title";
    public static final String KEY_ID = "video_id";
    public static final String KEY_THUMBNAIL = "video_thumbnail";

    // DB table
    public static final String DATABASE_TABLE = "favorites";

    private boolean introDismissed = false;

    public boolean isIntroDismissed() {
        return introDismissed;
    }

    public void setIntroDismissed(boolean hasIntroDismissed) {
        this.introDismissed = hasIntroDismissed;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // set the default preferences if not set yet
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // Setup Analytics
        GoogleAnalytics.getInstance().initTracker(this);
    }

    // TEST QUERY URL
    // https://www.googleapis.com/youtube/v3/search?part=snippet&q=come%20fly%20with%20me%20+%20frank%20sinatra&maxResults=50&key=AIzaSyC64yyVrXOuVNbG5m9nUsj7DrO3CWnFDbk

}
