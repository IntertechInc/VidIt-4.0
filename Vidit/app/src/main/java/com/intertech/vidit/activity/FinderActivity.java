package com.intertech.vidit.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.intertech.vidit.R;
import com.intertech.vidit.domain.SongQuery;
import com.intertech.vidit.domain.YouTubeItem;
import com.intertech.vidit.service.VideoListForSongTitleTask;
import com.intertech.vidit.ui.FinderFragment;

import java.util.ArrayList;
import java.util.List;

import static com.intertech.vidit.support.ViditApplication.AUDIO_MIME_TYPE;
import static com.intertech.vidit.support.ViditApplication.FIND_SONG_INTENT;
import static com.intertech.vidit.support.ViditApplication.LOG_TAG;
import static com.intertech.vidit.support.ViditApplication.PLAY_SONG_INTENT;
import static com.intertech.vidit.support.ViditApplication.SONG_QUERY;
import static com.intertech.vidit.support.ViditApplication.VIDEO_BROADCAST;
import static com.intertech.vidit.support.ViditApplication.VIDEO_EXTRA;
import static com.intertech.vidit.support.ViditApplication.YOU_TUBE_URL;


/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class FinderActivity extends ActionBarActivity {

    private static final String FINDER_FRAG_TAG = "finderFrag";
    private static final String SELECTED_YOUTUBE_VIDEO_KEY = "selectedYouTubeVideo";

    VideoListReceiver receiver;
    private FinderFragment fragment;
    private YouTubeItem selectedYouTubeVideo;
    VideoListForSongTitleTask videoTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);
        fragment = new FinderFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, FINDER_FRAG_TAG)
                    .commit();
            SongQuery query = getSongQuery();
            if (query != null){
                requestSongsAsync(query);
            } else {
                launchMusicPlayer();
            }
        } else {
            selectedYouTubeVideo = (YouTubeItem) savedInstanceState.getSerializable(SELECTED_YOUTUBE_VIDEO_KEY);
            fragment = (FinderFragment) getSupportFragmentManager().findFragmentByTag(FINDER_FRAG_TAG);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.padded_vidit_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(R.string.find_videos_title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        receiver = new VideoListReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(VIDEO_BROADCAST));
    }

    private SongQuery getSongQuery(){
        if (getIntent().getExtras() != null) {
           return (SongQuery) getIntent().getExtras().getSerializable(SONG_QUERY);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        try {
            this.unregisterReceiver(receiver);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Unregistering already occurred on video broadcast");
        }
        if (videoTask != null) {
            videoTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // called after returning from music selection app or playing music app
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        findViewById(R.id.finderProgressBar).setVisibility(View.VISIBLE);
        // returning from song selection
        if (requestCode == FIND_SONG_INTENT) {
            if (resultCode == RESULT_OK) {
                Uri musicURI = data.getData();
                Log.d(LOG_TAG, "Song selected video:  " + musicURI);
                // get the actual song info from the selected and call to get videos for it.
                getSongFromMusicCollection(musicURI);
            } else {  // no song selected from music player - return to main with nothing
                Toast.makeText(this, R.string.toast_no_track_selected, Toast.LENGTH_SHORT).show();
                findViewById(R.id.finderProgressBar).setVisibility(View.GONE);
                this.finish();
            }
            // returning from playing video on user selected video player
        } else if (requestCode == PLAY_SONG_INTENT) {
            Log.d(LOG_TAG, "Video played and requesting permission to add to favorites.");
            showSaveVideoFavoriteAlert();
        }
    }

    private void showSaveVideoFavoriteAlert() {
        findViewById(R.id.finderProgressBar).setVisibility(View.GONE);
        new AlertDialog.Builder(this)
                .setTitle(R.string.save_video_dialog_title)
                .setMessage(R.string.save_video_question)
                .setPositiveButton(R.string.vidit_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                fragment.addVideoToFavoritesList(selectedYouTubeVideo);
                            }
                        }).setNegativeButton(R.string.vidit_no, null).show();
    }

    // Starts user's choice of music player for song selection.
    public void launchMusicPlayer() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                AUDIO_MIME_TYPE);
        startActivityForResult(intent, FIND_SONG_INTENT);
    }

    // Start users choice of video player for video selection
    public void launchVideoPlayer(YouTubeItem item) {
        findViewById(R.id.finderProgressBar).setVisibility(View.VISIBLE);
        String videoUri = YOU_TUBE_URL + item.getId();
        selectedYouTubeVideo = item;
        startActivityForResult(
                new Intent(Intent.ACTION_VIEW, Uri.parse(videoUri)),
                PLAY_SONG_INTENT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_YOUTUBE_VIDEO_KEY, selectedYouTubeVideo);
    }

    // get selected music music song from users library and make call to async task to find videos for that song
    private void getSongFromMusicCollection(Uri musicURI) {
        String[] projs = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};
        Cursor c = getContentResolver().query(musicURI, projs, null, null, null);
        c.moveToFirst();
        SongQuery selectedFavoriteItem = new SongQuery(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)), c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        Log.d(LOG_TAG, "Selected collection title is:  " + selectedFavoriteItem.getTitle());
        Log.d(LOG_TAG, "selected collection artist is:  " + selectedFavoriteItem.getArtist());
        c.close();
        // execute async task to get video data
        requestSongsAsync(selectedFavoriteItem);
    }

    private void requestSongsAsync(SongQuery query){
        videoTask = new VideoListForSongTitleTask(getApplicationContext());
        videoTask.execute(query);
    }

    private class VideoListReceiver extends BroadcastReceiver {
        @Override
        @SuppressWarnings("unchecked")
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Receiver returned list of applicable videos");
            updateVideoList((ArrayList<YouTubeItem>) intent.getSerializableExtra(VIDEO_EXTRA));
        }
    }

    private void updateVideoList(ArrayList<YouTubeItem> videos) {
        if (videos != null && videos.size() > 0) {
            fragment.updateVideos(videos);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.no_videos_title)
                    .setMessage(R.string.no_videos_found)
                    .setPositiveButton(R.string.vidit_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    launchMusicPlayer();
                                }
                            }).setNegativeButton(R.string.vidit_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            }).show();
        }
        if (findViewById(R.id.finderProgressBar) != null) {
            findViewById(R.id.finderProgressBar).setVisibility(View.GONE);
        }
    }
}
