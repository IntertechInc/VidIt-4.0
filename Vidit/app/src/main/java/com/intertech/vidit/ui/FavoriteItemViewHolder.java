package com.intertech.vidit.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intertech.vidit.R;


/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class FavoriteItemViewHolder extends RecyclerView.ViewHolder {

    protected TextView favoriteItemArtistTextView;
    protected TextView favoriteItemTitleTextView;
    protected TextView favoriteItemVideoTitleTextView;
    protected TextView favoriteItemURITextView;
    protected ImageView favoriteItemThumbnailImageView;
    protected CheckBox favoriteItemCB;
    protected RelativeLayout favoriteLayout;

    public FavoriteItemViewHolder(View itemView) {
        super(itemView);
        favoriteLayout = (RelativeLayout) itemView.findViewById(R.id.cardLayout);
        favoriteItemArtistTextView = (TextView) itemView.findViewById(R.id.favoriteItemArtistTV);
        favoriteItemTitleTextView = (TextView) itemView.findViewById(R.id.favoriteItemTitleTV);
        favoriteItemVideoTitleTextView = (TextView) itemView.findViewById(R.id.favoriteVideoTitleTV);
        favoriteItemURITextView = (TextView) itemView.findViewById(R.id.favoriteVideoURL);
        favoriteItemThumbnailImageView = (ImageView) itemView.findViewById(R.id.favoriteItemThumbnailIV);
        favoriteItemCB = (CheckBox) itemView.findViewById(R.id.favoriteItemCB);
    }
}
