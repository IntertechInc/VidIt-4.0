package com.intertech.vidit.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.intertech.vidit.R;
import com.intertech.vidit.dao.ViditDBAdapter;
import com.intertech.vidit.domain.FavoriteItem;
import com.intertech.vidit.support.PaletteTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class FavoriteItemAdapter extends RecyclerView.Adapter<FavoriteItemViewHolder> {

    private ViditDBAdapter adapter = null;
    private List<FavoriteItem> favoriteItemList = null;
    private Context context;
    private SharedPreferences prefs;

    public FavoriteItemAdapter(Context context) {
        super();
        this.context = context;
        initializeDataSource(context);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public FavoriteItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.favorite_item_row, viewGroup, false);
        return new FavoriteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteItemViewHolder favoriteItemViewHolder, int i) {
        FavoriteItem item = favoriteItemList.get(i);
        favoriteItemViewHolder.favoriteItemTitleTextView.setText(item.getTitle());
        favoriteItemViewHolder.favoriteItemArtistTextView.setText(item.getArtist());
        favoriteItemViewHolder.favoriteItemVideoTitleTextView.setText(item.getVideoTitle());
        if (prefs.getBoolean("preference_display_video_url", true)) {
            favoriteItemViewHolder.favoriteItemURITextView.setText(item.getUri());
        } else {
            favoriteItemViewHolder.favoriteItemURITextView.setText("");
        }

        final RelativeLayout layout = favoriteItemViewHolder.favoriteLayout;
        // use picasso to load image and palette to set color of each card's layout
        final ImageView imageView = favoriteItemViewHolder.favoriteItemThumbnailImageView;
        Picasso.with(context)
                .load(item.getThumbnail())
                .transform(PaletteTransformation.instance())
                .into(imageView, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap(); // Ew!
                        Palette palette = PaletteTransformation.getPalette(bitmap);
                        layout.setBackgroundColor(palette.getLightVibrantColor(0));
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (favoriteItemList != null) {
            return favoriteItemList.size();
        }
        return 0;
    }

    public void closeAdapter() {
        if (adapter != null) {
            adapter.close();
        }
    }

    public void updateFavorites() {
        favoriteItemList = adapter.getFavorites();
        notifyDataSetChanged();
    }

    public FavoriteItem getSelected(int position) {
        return favoriteItemList.get(position);
    }

    private void initializeDataSource(Context context) {
        adapter = new ViditDBAdapter(context);
        adapter.open();
        favoriteItemList = adapter.getFavorites();
    }

    public void removeFavorites(int[] removedPositions) {
        FavoriteItem item;
        for (int removedPosition : removedPositions) {
            item = favoriteItemList.get(removedPosition);
            adapter.deleteFavorite(item.getId());
            notifyItemRemoved(removedPosition);
        }
        updateFavorites();
    }
}
