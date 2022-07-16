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

/**
 * Singleton class responsible for playing and keeping track of the state of currenty played music. 
 * Also stores a MusicDatabase object.
 */
public class Jukebox {
    /**
     * Media object property of the current track
     */
    public final SimpleObjectProperty<Media> currentTrack = new SimpleObjectProperty<>();
    /**
     * Index property of the current track
     */
    public final SimpleIntegerProperty currentTrackIndex = new SimpleIntegerProperty(0);
    /** 
     * Name property of the current track
     */
    public final SimpleStringProperty currentTrackName = new SimpleStringProperty();
    /**
     * Point in time property of the currently played track
     */
    public final SimpleObjectProperty<Duration> currentTrackTime = new SimpleObjectProperty<>(new Duration(0.0));
    /**
     * Status property (playing, paused etc.) of the current track
     */
    public final SimpleObjectProperty<Status> currentTrackStatus = new SimpleObjectProperty<>(Status.UNKNOWN);
    /**
     * Cover image property of the album that the current track belongs to
     */
    public final SimpleObjectProperty<Image> currentTrackCoverImage = new SimpleObjectProperty<>(MusicAlbum.DEFAULT_COVER_IMAGE);
    /**
     * Volume property of the music to be played
     */
    public final SimpleFloatProperty trackVolume = new SimpleFloatProperty(1.f);
    /**
     * Boolean property signaling whether tracks should be played automatically after the previous ones finish 
     */
    public final SimpleBooleanProperty playlistAutoplay = new SimpleBooleanProperty(false); 
    /**
     * Boolean property signaling whether playlist should loop back to the beginning after it goes to the end
     */
    public final SimpleBooleanProperty playlistLooping = new SimpleBooleanProperty(false); 


    /**
     * Singleton instance
     */
    private static Jukebox instance = null;

    /** MediaPlayer object that takes the media of the currently played music */
    private MediaPlayer mediaPlayer = null;

    /**
     * List of tracks that can actively be selected to be played
     */
    private ObservableList<MusicTrack> playlist = FXCollections.observableArrayList();
    /**
     * MusicDatabase object of the music tracks, albums and user collections loaded from the system
     */
    private MusicDatabase musicDatabase = new MusicDatabase();


    private Jukebox() {
        
    }

    /**
     * Get the singleton instance of the Jukebox
     * 
     * @return Jukebox - instance of the Jukebox
     */
    public static Jukebox getInstance() {
        if(instance == null) {
            instance = new Jukebox();
        }
        
        return instance;
    }
    


    /**
     * Get list of tracks in the playlist
     * 
     * @return ObservableList<MusicTrack> - list of tracks in the playlist
     */
    public ObservableList<MusicTrack> getPlaylist() {
        return this.playlist;
    }

    /**
     * Get the music database
     * 
     * @return MusicDatabase - music database
     */
    public MusicDatabase getMusicDatabase() {
        return this.musicDatabase;
    }


    
    /**
     * Return whether any music is currently playing
     * 
     * @return boolean - true if music is playing, false otherwise
     */
    public boolean isPlaying() {
        if(mediaPlayer != null) {
            return this.mediaPlayer.getStatus().equals(Status.PLAYING);
        }

        return false;
    }

    /**
     * Return whether any music is currently paused
     * 
     * @return boolean - true if music is paused, false otherwise
     */
    public boolean isPaused() {
        if(mediaPlayer != null) {
            return this.mediaPlayer.getStatus().equals(Status.PAUSED);
        }

        return false;
    }

    /**
     * Select and prepare a track from a given index in the playlist
     * 
     * @param newIndex - index of the track to be selected
     * @param shouldPlay - whether the track should be played after it is selected and ready
     */
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

    /**
     * Select and prepare a track from a given index in the playlist. 
     * It will automatically play the track if any track was being played beforehand.
     * 
     * @param newIndex - index of the track to be selected
     */
    public void selectTrack(int newIndex) {
        this.selectTrack(newIndex, this.isPlaying());
    }

    /**
     * Resume playing the music if it's selected
     */
    public void play() {
        if(mediaPlayer != null) {
            this.mediaPlayer.play();
        }
    }

    /** 
     * Pause playing the music if it's being played
     */
    public void pause() {
        if(mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }

    /**
     * Go to a given time point in the current music track
     * 
     * @param seekTime - time point to go to
     */
    public void seek(Duration seekTime) {
        if(mediaPlayer != null) {
            this.mediaPlayer.seek(seekTime);
        }
    }

    /** 
     * Select the next available track in the playlist if possible
     */
    public void selectNextTrack() {
        int idx = this.currentTrackIndex.get() + 1;
        if(idx < playlist.size()) {
            this.selectTrack(idx);
        } else if(this.playlistLooping.get()){
            this.selectTrack(0);
        }
    }

    /** 
     * Select the previous available track in the playlist if possible
     */
    public void selectPreviousTrack() {
        int idx = this.currentTrackIndex.get() - 1;
        if(idx >= 0) {
            this.selectTrack(idx);
        } else if(this.playlistLooping.get()){
            this.selectTrack(playlist.size() - 1);
        }
    }

    /**
     * Get the duration of the current track
     */
    public Duration getCurrentTrackDuration() {
        if(this.mediaPlayer != null) {
            return this.mediaPlayer.getMedia().getDuration();
        }

        return Duration.UNKNOWN;
    }

    /**
     * Get the current time point in the current track
     */
    public Duration getCurrentTrackTime() {
        if(this.mediaPlayer != null) {
            return this.mediaPlayer.getCurrentTime();
        }

        return Duration.UNKNOWN;
    }

    /**
     * Set the music volume
     * 
     * @param volume - volume to set between 0 and 1
     */
    public void setVolume(float volume) {
        this.trackVolume.set(volume);
        if(this.mediaPlayer != null) {
            this.mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Method called when it is prepared after selecting it.
     * Sets up all the properties of the current track.
     *  
     * @param index - index of the track
     * @param media - media object for that music track
     * @param wasPlayingBefore - if the music was playing before it was selected
     */
    private void onTrackReady(int index, Media media, boolean wasPlayingBefore) {
        this.currentTrack.set(media);
        this.currentTrackIndex.set(index);
        this.currentTrackName.set(playlist.get(index).getFileName());
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

    /**
     * Method called when the current track finishes playing.
     * Selects the next track in the playlist if it's looping.
     */
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
