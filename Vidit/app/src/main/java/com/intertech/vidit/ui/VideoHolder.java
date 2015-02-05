package com.intertech.vidit.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intertech.vidit.R;


/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class VideoHolder extends RecyclerView.ViewHolder {
    protected TextView videoTitleTextView;
    protected TextView videoDescriptionTextView;
    protected ImageView videoThumbnail;

    public VideoHolder(View videoView) {
        super(videoView);
        videoTitleTextView = (TextView) videoView.findViewById(R.id.videoTitleTV);
        videoDescriptionTextView = (TextView) videoView.findViewById(R.id.videoDescriptionTV);
        videoThumbnail = (ImageView) videoView.findViewById(R.id.videoThumbnailIV);
    }

}
