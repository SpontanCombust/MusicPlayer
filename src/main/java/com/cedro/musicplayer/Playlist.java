package com.cedro.musicplayer;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

public class Playlist {
    private final List<String> AUDIO_EXTENSIONS;
    private ArrayList<Media> tracks = new ArrayList<>();
    private ArrayList<String> trackNames = new ArrayList<>();

    public Playlist() {
        AUDIO_EXTENSIONS = Arrays.asList(
            "aif", "aiff", 
            "mp3",
            "mp4", "m4a", "m4v",
            "wav");
    }

    public void loadTracksFromDirectory(File directory, boolean scanRecursively) {
        var files = directory.listFiles();
        if(files != null) {
            for(File f: files) {
                if(f.isDirectory() && scanRecursively) {
                    this.loadTracksFromDirectory(f, scanRecursively);
                } else if(this.isAudioFile(f)){
                    var mediaOpt = loadMedia(f.getAbsolutePath());
                    if(mediaOpt.isPresent()) {
                        trackNames.add(f.getName());
                        tracks.add(mediaOpt.get());
                    }
                }
            }
        }
    }

    public ArrayList<Media> getTracks() {
        return tracks;
    }

    public ArrayList<String> getTrackNames() {
        return trackNames;
    }

    public void clearTracks() {
        tracks.clear();
    }

    
    
    private boolean isAudioFile(File file) {
        int dotIndex = file.getName().lastIndexOf(".");
        if( dotIndex != -1 ) {
            String extension = file.getName().substring(dotIndex + 1);
            return AUDIO_EXTENSIONS.stream().anyMatch(e -> e.equals(extension));
        }

        return false;
    }

    private Optional<Media> loadMedia(String path) {
        Optional<Media> opt = Optional.empty();

        try {
            var media = new Media(Paths.get(path).toUri().toString());
            opt = Optional.of(media);
        } catch (MediaException e) {
            System.out.println(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return opt;
    }
}
