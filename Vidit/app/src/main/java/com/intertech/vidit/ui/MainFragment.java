package com.intertech.vidit.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.intertech.vidit.R;
import com.intertech.vidit.domain.FavoriteItem;
import com.intertech.vidit.service.ViditVideoPlayer;

import java.util.ArrayList;
import java.util.List;


/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class MainFragment extends Fragment {

    RecyclerView favoritesRecycler;
    private FavoriteItemAdapter adapter;
    private ViditVideoPlayer videoPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        favoritesRecycler = (RecyclerView) rootView.findViewById(R.id.favoritesRecyclerView);
        favoritesRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecycler.setLayoutManager(layoutManager);
        adapter = new FavoriteItemAdapter(getActivity().getApplicationContext());
        favoritesRecycler.setAdapter(adapter);
        RecyclerListener listener = new RecyclerListener(getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                favoriteSelected(position);
            }
        });
        favoritesRecycler.addOnItemTouchListener(listener);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            SwipeDismissRecyclerViewTouchListener touchListener = getSwipeDismissRecyclerViewTouchListener();
            favoritesRecycler.setOnTouchListener(touchListener);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        getActivity().findViewById(R.id.mainProgressBar).setVisibility(View.GONE);
        super.onResume();
        adapter.updateFavorites();
        if (adapter.getItemCount() <= 0) {
            Toast.makeText(getActivity(), R.string.toast_no_favorites,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        videoPlayer = null;
        adapter.closeAdapter();
        super.onDestroyView();
    }

    public void removeSelected() {
        List<Integer> selected = new ArrayList<>();
        if (adapter != null) {
            for (int x = 0; x < adapter.getItemCount(); x++) {
                FavoriteItemViewHolder holder = (FavoriteItemViewHolder) favoritesRecycler.findViewHolderForPosition(x);
                if (holder != null && holder.favoriteItemCB.isChecked()) {
                    selected.add(x);
                }
            }
        }
        if (selected.size() > 0) {
            int[] selectedPositions = new int[selected.size()];
            for (int y = 0; y < selected.size(); y++) {
                selectedPositions[y] = selected.get(y);
            }
            adapter.removeFavorites(selectedPositions);
            // a bit hokey but need to uncheck all check boxes after removing the checked ones.
            for (int x = 0; x < adapter.getItemCount(); x++) {
                FavoriteItemViewHolder holder = (FavoriteItemViewHolder) favoritesRecycler.findViewHolderForPosition(x);
                if (holder != null && holder.favoriteItemCB != null) {
                    holder.favoriteItemCB.setChecked(false);
                }
            }
        } else {
            Toast.makeText(getActivity(), R.string.toast_no_favorites_selected, Toast.LENGTH_SHORT).show();
        }
    }

    private void favoriteSelected(int position) {
        FavoriteItem selectedItem = adapter.getSelected(position);
        if (selectedItem != null) {
            getActivity().findViewById(R.id.mainProgressBar).setVisibility(View.VISIBLE);
            if (videoPlayer == null) {
                videoPlayer = new ViditVideoPlayer(getActivity());
            }
            if (!videoPlayer.playVideo(selectedItem)) { // if video player is not playing, dismiss the progress
                getActivity().findViewById(R.id.mainProgressBar).setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(getActivity(), R.string.toast_unknown_error,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private SwipeDismissRecyclerViewTouchListener getSwipeDismissRecyclerViewTouchListener() {
        return new SwipeDismissRecyclerViewTouchListener(
                favoritesRecycler,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        adapter.removeFavorites(reverseSortedPositions);
                        Toast.makeText(getActivity(), R.string.toast_favorite_removed, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}