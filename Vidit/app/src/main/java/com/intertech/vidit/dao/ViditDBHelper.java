package com.intertech.vidit.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.intertech.vidit.support.ViditApplication.*;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class ViditDBHelper extends SQLiteOpenHelper {

    static int THIS_VERSION = 2;

    public ViditDBHelper(Context ctx) {
        super(ctx, "viditfavorites", null, THIS_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "Creating database schema.");
        final String sql = "create table " + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ARTIST + " TEXT NOT NULL, " + KEY_TITLE + " TEXT NOT NULL, " + KEY_URI + " TEXT NOT NULL, " + KEY_DESCRIPTION + " TEXT, " + KEY_VIDEO_TITLE + " TEXT," + KEY_ID + " TEXT NOT NULL, " + KEY_THUMBNAIL + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            final String drop = "drop table if exists " + DATABASE_TABLE;
            db.execSQL(drop);
            onCreate(db);
        } else {
            Log.i(LOG_TAG, "No schema to upgrade.");
        }
    }

}
