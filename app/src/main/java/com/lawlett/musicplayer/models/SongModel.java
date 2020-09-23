package com.lawlett.musicplayer.models;

public class SongModel {
    String song;
    String artist;

    public SongModel(String song, String artist) {
        this.song = song;
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public String getArtist() {
        return artist;
    }
}
