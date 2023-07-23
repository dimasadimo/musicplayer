package com.example.musicplayer.service;

public class Result {
    private int trackId;
    private String artistName;
    private String trackName;
    private String previewUrl;
    private String artworkUrl100;
    private String trackTimeMillis;

    private boolean isPlaying;

    private  int state;

    public Result(int newtrackId , String newArtistName, String newTrackName, String newPreviewUrl, String newArtworkUrl100, String newTrackTimeMillis, boolean newIsPlaying){
        trackId = newtrackId;
        artistName = newArtistName;
        trackName = newTrackName;
        previewUrl = newPreviewUrl;
        artworkUrl100 = newArtworkUrl100;
        trackTimeMillis = newTrackTimeMillis;
        isPlaying = newIsPlaying;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }


    public int getState() {
        return state;
    }

    public String getTrackTimeMillis() {
        return trackTimeMillis;
    }

    public void setTrackTimeMillis(String trackTimeMillis) {
        this.trackTimeMillis = trackTimeMillis;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setState(int state) {
        this.state = state;
    }
}

