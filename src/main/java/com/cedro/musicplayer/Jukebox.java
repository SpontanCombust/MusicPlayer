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
    public final SimpleIntegerProperty currentTrackIndex = new SimpleIntegerProperty();
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

    public void next() {
        this.currentTrackIndex.add(1);
    }

    public void previous() {
        this.currentTrackIndex.subtract(1);
    }


    private Jukebox() {
        currentTrackIndex.addListener(
            (obeservable, oldVal, newVal) -> {
                onTrackIndexChanged(newVal.intValue());
            }
        );
    }

    private void onTrackIndexChanged(int newIndex) {
        var tracks = playlist.getTracks();

        if(newIndex >= 0 && newIndex < tracks.size()) {
            boolean isPlaying = this.isPlaying();

            Media media = tracks.get(newIndex);
            this.mediaPlayer = new MediaPlayer(media);
            this.currentTrack.set(media);
            this.currentTrackName.set(playlist.getTrackNames().get(newIndex));
            this.currentTrackTime.bind(this.mediaPlayer.currentTimeProperty());

            if(isPlaying) {
                this.mediaPlayer.play();
            }
        }
    }
}
