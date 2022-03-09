package com.cedro.musicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javafx.scene.media.Media;

public class Playlist {
    private final List<String> AUDIO_EXTENSIONS;
    private ArrayList<Media> playlistTracks = new ArrayList<>();

    public Playlist() {
        AUDIO_EXTENSIONS = Arrays.asList(
            "aif", "aiff", 
            "mp3",
            "mp4", "m4a", "m4v",
            "wav");
    }

    public void addTracks(Collection<Media> tracks) {
        playlistTracks.addAll(tracks);
    }

    public Media getTrack(int i) {
        return playlistTracks.get(i);
    }

    public void clearTracks() {
        playlistTracks.clear();
    }

    public boolean isAudioFile(File file) {
        int dotIndex = file.getName().lastIndexOf(".");
        if( dotIndex != -1 ) {
            String extension = file.getName().substring(dotIndex + 1);
            return AUDIO_EXTENSIONS.stream().anyMatch(e -> e.equals(extension));
        }

        return false;
    }
}
