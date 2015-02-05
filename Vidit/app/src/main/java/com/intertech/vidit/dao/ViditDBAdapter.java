package com.intertech.vidit.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.intertech.vidit.domain.FavoriteItem;

import static com.intertech.vidit.support.ViditApplication.*;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class ViditDBAdapter {

    // DB QUERY
    private static final String WHERE_CLAUSE = KEY_ARTIST + "=? and " + KEY_TITLE + "=? and " + KEY_ID + "=?";

    // DB sort order
    private static final String SORT_ORDER = "lib_title asc";

    private Context context;
    private SQLiteDatabase database;
    private ViditDBHelper dbHelper;

    public ViditDBAdapter(Context context) {
        this.context = context;
    }

    public ViditDBAdapter open() throws SQLException {
        dbHelper = new ViditDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public long createFavorite(String artist, String title, String uri, String description, String videoTitle, String videoId, String thumbnail) {
        if (!isDuplicateFavorite(artist, title, videoId)) {
            ContentValues initialValues = createContentValues(artist, title, uri, description, videoTitle, videoId, thumbnail);
            return database.insert(DATABASE_TABLE, null, initialValues);
        }
        return -1;
    }

    public boolean deleteFavorite(int rowId) {
        String[] ids = {"" + rowId};
        return database.delete(DATABASE_TABLE, KEY_ROWID + "=?", ids) > 0;
    }

    private Cursor fetchAllFavorites() {
        return database.query(DATABASE_TABLE, null, null, null, null, null,
                SORT_ORDER);
    }

    public List<FavoriteItem> getFavorites() {
        Cursor c = fetchAllFavorites();
        c.moveToFirst();
        List<FavoriteItem> items = new ArrayList<>();
        while (!c.isAfterLast()) {
            items.add(new FavoriteItem(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7)));
            c.moveToNext();
        }
        c.close();
        return items;
    }

    private Cursor fetchMatchingFavorites(String artist, String title, String videoId) {
        String[] vals = {artist, title, videoId};
        return database.query(DATABASE_TABLE, null, WHERE_CLAUSE, vals, null, null,
                SORT_ORDER);
    }

    public boolean isDuplicateFavorite(String artist, String title, String videoId) {
        boolean foundDupe = false;
        Cursor c = fetchMatchingFavorites(artist, title, videoId);
        if (c.getCount() > 0) {
            foundDupe = true;
        }
        c.close();
        return foundDupe;
    }

    private ContentValues createContentValues(String artist, String title, String uri, String description, String videoTitle, String videoId, String thumbnail) {
        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST, artist);
        values.put(KEY_TITLE, title);
        values.put(KEY_URI, uri);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_VIDEO_TITLE, videoTitle);
        values.put(KEY_ID, videoId);
        values.put(KEY_THUMBNAIL, thumbnail);
        return values;
    }

}
