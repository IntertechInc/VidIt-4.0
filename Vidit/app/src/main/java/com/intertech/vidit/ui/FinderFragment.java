package com.intertech.vidit.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.intertech.vidit.R;
import com.intertech.vidit.activity.FinderActivity;
import com.intertech.vidit.dao.ViditDBAdapter;
import com.intertech.vidit.domain.YouTubeItem;

import java.util.ArrayList;
import java.util.List;

import static com.intertech.vidit.support.ViditApplication.YOU_TUBE_URL;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class FinderFragment extends Fragment {

    RecyclerView videoRecycler;
    VideoAdapter adapter;
    private ViditDBAdapter viditDb;
    private List<YouTubeItem> matchingVideos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viditDb = new ViditDBAdapter(getActivity().getApplicationContext());
        viditDb.open();
        View rootView = inflater.inflate(R.layout.fragment_finder, container, false);
        videoRecycler = (RecyclerView) rootView.findViewById(R.id.videosRecyclerView);
        videoRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        videoRecycler.setLayoutManager(layoutManager);
        adapter = new VideoAdapter(getActivity().getApplicationContext());
        videoRecycler.setAdapter(adapter);
        RecyclerListener listener = new RecyclerListener(getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                videoSelected(position);
            }
        });
        videoRecycler.addOnItemTouchListener(listener);
        return rootView;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            matchingVideos = (List<YouTubeItem>) savedInstanceState.getSerializable("matchingVideos");
            updateVideos(matchingVideos);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("matchingVideos", (ArrayList<?>)matchingVideos);
    }

    @Override
    public void onDestroyView() {
        if (viditDb != null) {
            viditDb.close();
        }
        super.onDestroyView();
    }

    public void addVideoToFavoritesList(YouTubeItem selectedYouTubeVideo) {
        if (viditDb.isDuplicateFavorite(selectedYouTubeVideo.getSearchArtist(), selectedYouTubeVideo.getSearchTitle(), selectedYouTubeVideo.getId())) {
            showDuplicateVideoAlert(selectedYouTubeVideo);
        } else {
            long returnId = viditDb.createFavorite(selectedYouTubeVideo.getSearchArtist(), selectedYouTubeVideo.getSearchTitle(), YOU_TUBE_URL + selectedYouTubeVideo.getId(), selectedYouTubeVideo.getDescription(), selectedYouTubeVideo.getTitle(), selectedYouTubeVideo.getId(), selectedYouTubeVideo.getThumbnail());
            if (returnId >= 0) {
                Toast.makeText(getActivity(), R.string.toast_favorite_added, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void videoSelected(int position) {
        YouTubeItem item = adapter.getItem(position);
        ((FinderActivity) getActivity()).launchVideoPlayer(item);
    }

    private void showDuplicateVideoAlert(YouTubeItem item) {
        String message = getString(R.string.duplicate_detected, item.getTitle());
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.duplicate_video)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null).show();
    }

    public void updateVideos(List<YouTubeItem> videos) {
        matchingVideos = videos;
        if (adapter != null) {
            adapter.updateVideos(videos);
        }
    }
}