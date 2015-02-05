package com.intertech.vidit.domain;

import java.io.Serializable;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
public class SongQuery implements Serializable {

    private String title;
    private String artist;

    public SongQuery(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    @SuppressWarnings("unused")
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "SongQuery{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
