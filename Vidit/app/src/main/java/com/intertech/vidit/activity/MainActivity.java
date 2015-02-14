package com.intertech.vidit.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.intertech.vidit.R;
import com.intertech.vidit.domain.SongQuery;
import com.intertech.vidit.support.GoogleAnalytics;
import com.intertech.vidit.support.ViditApplication;
import com.intertech.vidit.ui.MainFragment;

import static com.intertech.vidit.support.ViditApplication.SONG_QUERY;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class MainActivity extends ActionBarActivity {

    MainFragment fragment;
    AlertDialog flash;
    ViditApplication app;
    Dialog alertDialog;
    Dialog manulQueryDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.padded_vidit_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(R.string.favorites_title);
        setContentView(R.layout.activity_main);
        fragment = new MainFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        app = (ViditApplication)this.getApplication();
        if (!app.isIntroDismissed()) {
            showCredits();
            app.setIntroDismissed(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance().sendScreenView(getClass().getSimpleName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        if (manulQueryDialog != null) {
            manulQueryDialog.dismiss();
        }
        if (flash != null) {
            flash.dismiss();
            flash = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_find) {
            startActivity(new Intent(this, FinderActivity.class));
        } else if (id == R.id.action_query) {
            showManualQueryPrompt();
        } else if (id == R.id.action_remove) {
            removeSelected();
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeSelected() {
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.remove_favorites_title)
                .setMessage(R.string.remove_favorites)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                fragment.removeSelected();
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
    }

    private void showCredits() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.credit_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        builder.setView(layout);
        String dismissString = getResources().getString(R.string.dismiss);
        builder.setPositiveButton(dismissString, new OkOnClickListener());
        flash = builder.create();
        flash.show();
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            if ((flash != null) && (flash.isShowing())) {
                flash.dismiss();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showManualQueryPrompt() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.manual_song_query_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        final EditText titleEditText = (EditText) promptView.findViewById(R.id.manualTitleEditText);
        final EditText artistEditText = (EditText) promptView.findViewById(R.id.manualArtistEditText);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        final AlertDialog songQuery = alertDialogBuilder.create();
        songQuery.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = songQuery.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = titleEditText.getText().toString();
                        String artist = artistEditText.getText().toString();
                        if (title.length() <= 0 || artist.length() <= 0) {
                            Toast.makeText(MainActivity.this, R.string.error_no_title_or_artist, Toast.LENGTH_SHORT).show();
                        } else {
                            SongQuery query = new SongQuery(title, artist);
                            Intent intent = new Intent(MainActivity.this, FinderActivity.class);
                            intent.putExtra(SONG_QUERY, query);
                            startActivity(intent);
                            songQuery.dismiss();
                        }
                    }
                });
            }
        });
        MainActivity.this.manulQueryDialog = songQuery;
        songQuery.show();
    }
}
