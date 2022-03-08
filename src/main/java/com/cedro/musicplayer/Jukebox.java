package com.cedro.musicplayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Jukebox {
    private MediaPlayer player = null;

    public Jukebox() {

    }

    void setupMediaPlayer(String pathToMedia) {
        player = new MediaPlayer(new Media(pathToMedia));
    }

    MediaPlayer getMediaPlayer() {
        return player;
    }
}
