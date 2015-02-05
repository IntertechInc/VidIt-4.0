package com.intertech.vidit.service;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.intertech.vidit.R;
import com.intertech.vidit.domain.FavoriteItem;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class ViditVideoPlayer {

    Context context;

    public ViditVideoPlayer(Context context) {
        this.context = context;
    }

    public boolean playVideo(FavoriteItem favorite) {
        if (isNetworkAvailable()) {
            String videoId = favorite.getUri();
            try {
                context.startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(videoId)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION));
                return true;
            } catch (ActivityNotFoundException notFoundException) {
                Toast.makeText(context, R.string.toast_invalid_track_url, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.toast_no_connectivity, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
