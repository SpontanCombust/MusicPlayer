package com.cedro.musicplayer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class Jukebox {
    public final SimpleObjectProperty<Media> currentTrack = new SimpleObjectProperty<>();
    public final SimpleIntegerProperty currentTrackIndex = new SimpleIntegerProperty(0);
    public final SimpleStringProperty currentTrackName = new SimpleStringProperty();
    public final SimpleObjectProperty<Duration> currentTrackTime = new SimpleObjectProperty<>();


    private static Jukebox instance = null;

    private Playlist playlist = new Playlist();
    private MediaPlayer mediaPlayer = null;


    public static Jukebox getInstance() {
        if(instance == null) {
            instance = new Jukebox();
        }
        
        return instance;
    }
    
    public Playlist getPlaylist() {
        return this.playlist;
    }

    public boolean isPlaying() {
        if(mediaPlayer != null) {
            return this.mediaPlayer.getStatus().equals(Status.PLAYING);
        }

        return false;
    }

    public boolean isPaused() {
        if(mediaPlayer != null) {
            return this.mediaPlayer.getStatus().equals(Status.PAUSED);
        }

        return false;
    }

    public void selectTrack(int newIndex) {
        var tracks = playlist.getTracks();
        
        if(newIndex >= 0 && newIndex < tracks.size()) {
            boolean wasPlayingBefore = this.isPlaying();
            
            Media media = tracks.get(newIndex);
            this.mediaPlayer = new MediaPlayer(media);
            this.mediaPlayer.setOnReady(() -> onTrackReady(newIndex, media, wasPlayingBefore));
        }
    }

    public void play() {
        if(mediaPlayer != null) {
            this.mediaPlayer.play();
        }
    }

    public void pause() {
        if(mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }

    public void selectNextTrack() {
        this.selectTrack(this.currentTrackIndex.get() + 1);
    }

    public void selectPreviousTrack() {
        this.selectTrack(this.currentTrackIndex.get() - 1);
    }


    private Jukebox() {
        
    }

    private void onTrackReady(int index, Media media, boolean wasPlayingBefore) {
        this.currentTrackIndex.set(index);
        this.currentTrack.set(media);
        this.currentTrackName.set(playlist.getTrackNames().get(index));
        this.currentTrackTime.unbind();
        this.currentTrackTime.set(new Duration(0.0));
        this.currentTrackTime.bind(this.mediaPlayer.currentTimeProperty());

        if(wasPlayingBefore) {
            this.mediaPlayer.play();
        }
    }
}
