package com.intertech.vidit.activity;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.intertech.vidit.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.intertech.vidit.support.ViditApplication.LOG_TAG;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class AboutActivity extends ActionBarActivity {

    ReadInfoTask infoTask;
    TextView info;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.padded_vidit_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(R.string.about_title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //launch thread to read info text from raw file
        infoTask = new ReadInfoTask();
        infoTask.execute();
    }

    //AsyncTask to read info text from raw text file and post to TextView.
    private class ReadInfoTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... args) {
            InputStream inputStream = getResources().openRawResource(
                    R.raw.intertech_info);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i;
            try {
                i = inputStream.read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = inputStream.read();
                }
                inputStream.close();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error reading info text.");
                e.printStackTrace();
                Resources res = getResources();
                return (res.getString(R.string.providedby_label));
            }
            return byteArrayOutputStream.toString();
        }

        protected void onPostExecute(String infoString) {
            info = (TextView) findViewById(R.id.infoTextView);
            info.setText(Html.fromHtml(infoString));
            info.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
