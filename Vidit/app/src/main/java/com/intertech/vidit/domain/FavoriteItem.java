package com.intertech.vidit.domain;

/* Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 *
 * Created by jwhite on 1/25/2015.
 */
@SuppressWarnings("unused")
public class FavoriteItem {
    private int id;
    private String artist;
    private String title;
    private String uri;
    private String description;
    private String videoTitle;
    private String videoId;
    private String thumbnail;

    public FavoriteItem(int id, String artist, String title, String uri, String description, String videoTitle, String videoId, String thumbnail) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.uri = uri;
        this.description = description;
        this.videoTitle = videoTitle;
        this.videoId = videoId;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String toString() {
        return artist + " / " + title;
    }

}
