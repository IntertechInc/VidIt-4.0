package com.intertech.vidit.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intertech.vidit.R;
import com.intertech.vidit.domain.YouTubeItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {

    List<YouTubeItem> videos = new ArrayList<>();
    private Context context;

    public VideoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.video_item_row, viewGroup, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder videoHolder, int i) {
        if (videos != null && videos.size() > 0) {
            YouTubeItem item = videos.get(i);
            videoHolder.videoTitleTextView.setText(item.getTitle());
            videoHolder.videoDescriptionTextView.setText(item.getDescription());
            Picasso.with(context).load(item.getThumbnail()).into(videoHolder.videoThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if (videos != null) {
            return videos.size();
        }
        return 0;
    }

    public void updateVideos(List<YouTubeItem> newVideos) {
        videos = newVideos;
        notifyDataSetChanged();
    }

    public YouTubeItem getItem(int position) {
        if (videos != null && videos.size() >= position) {
            return videos.get(position);
        }
        return null;
    }
}
