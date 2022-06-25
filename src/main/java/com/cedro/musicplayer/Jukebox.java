package com.cedro.musicplayer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class Jukebox {
    public final SimpleObjectProperty<Media> currentTrack = new SimpleObjectProperty<>();
    public final SimpleIntegerProperty currentTrackIndex = new SimpleIntegerProperty(0);
    public final SimpleStringProperty currentTrackName = new SimpleStringProperty();
    public final SimpleObjectProperty<Duration> currentTrackTime = new SimpleObjectProperty<>(new Duration(0.0));
    public final SimpleObjectProperty<Status> currentTrackStatus = new SimpleObjectProperty<>(Status.UNKNOWN);
    public final SimpleObjectProperty<Image> currentTrackCoverImage = new SimpleObjectProperty<>(MusicAlbum.DEFAULT_COVER_IMAGE);
    public final SimpleFloatProperty trackVolume = new SimpleFloatProperty(1.f);
    public final SimpleBooleanProperty playlistAutoplay = new SimpleBooleanProperty(false); 
    public final SimpleBooleanProperty playlistLooping = new SimpleBooleanProperty(false); 


    private static Jukebox instance = null;

    private MediaPlayer mediaPlayer = null;

    private ObservableList<MusicTrack> playlist = FXCollections.observableArrayList();
    private MusicDatabase musicDatabase = new MusicDatabase();


    private Jukebox() {
        
    }

    public static Jukebox getInstance() {
        if(instance == null) {
            instance = new Jukebox();
        }
        
        return instance;
    }
    


    public ObservableList<MusicTrack> getPlaylist() {
        return this.playlist;
    }

    public MusicDatabase getMusicDatabase() {
        return this.musicDatabase;
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

    public void selectTrack(int newIndex, boolean shouldPlay) {
        if(newIndex >= 0 && newIndex < playlist.size()) {
            if(mediaPlayer != null) {
                this.mediaPlayer.dispose();
            }
            
            Media media = playlist.get(newIndex).loadMedia();
            this.mediaPlayer = new MediaPlayer(media);
            this.mediaPlayer.setOnReady(() -> onTrackReady(newIndex, media, shouldPlay));
            this.mediaPlayer.setOnEndOfMedia(() -> onTrackFinished());
        }
    }

    public void selectTrack(int newIndex) {
        this.selectTrack(newIndex, this.isPlaying());
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

    public void seek(Duration seekTime) {
        if(mediaPlayer != null) {
            this.mediaPlayer.seek(seekTime);
        }
    }

    public void selectNextTrack() {
        int idx = this.currentTrackIndex.get() + 1;
        if(idx < playlist.size()) {
            this.selectTrack(idx);
        } else if(this.playlistLooping.get()){
            this.selectTrack(0);
        }
    }

    public void selectPreviousTrack() {
        int idx = this.currentTrackIndex.get() - 1;
        if(idx >= 0) {
            this.selectTrack(idx);
        } else if(this.playlistLooping.get()){
            this.selectTrack(playlist.size() - 1);
        }
    }

    public Duration getCurrentTrackDuration() {
        if(this.mediaPlayer != null) {
            return this.mediaPlayer.getMedia().getDuration();
        }

        return Duration.UNKNOWN;
    }

    public Duration getCurrentTrackTime() {
        if(this.mediaPlayer != null) {
            return this.mediaPlayer.getCurrentTime();
        }

        return Duration.UNKNOWN;
    }

    public void setVolume(float volume) {
        this.trackVolume.set(volume);
        if(this.mediaPlayer != null) {
            this.mediaPlayer.setVolume(volume);
        }
    }

    private void onTrackReady(int index, Media media, boolean wasPlayingBefore) {
        this.currentTrack.set(media);
        this.currentTrackIndex.set(index);
        this.currentTrackName.set(playlist.get(index).getName());
        this.currentTrackTime.unbind();
        this.currentTrackTime.set(new Duration(0.0));
        this.currentTrackTime.bind(this.mediaPlayer.currentTimeProperty());
        this.currentTrackStatus.bind(this.mediaPlayer.statusProperty());
        this.currentTrackCoverImage.set(playlist.get(index).getParentAlbum().getCoverImage());

        this.mediaPlayer.setVolume(this.trackVolume.floatValue());

        if(wasPlayingBefore) {
            this.mediaPlayer.play();
        }
    }

    private void onTrackFinished() {
        if(this.playlistAutoplay.get()) {
            this.selectNextTrack();
        } else {
            // simply seeking to the beginning or stopping doesn't seem to work as it should
            // it always seeks a little bit after the beginning
            // the hack is to just reselect the song
            this.selectTrack(this.currentTrackIndex.get(), false);
        }
    }
}
