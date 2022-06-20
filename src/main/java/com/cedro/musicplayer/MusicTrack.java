package com.cedro.musicplayer;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javafx.scene.media.Media;

public class MusicTrack {
    public static final List<String> AUDIO_EXTENSIONS = Arrays.asList(
        "aif", "aiff", "mp3", "mp4", "m4a", "m4v", "wav"
    );


    private Path filePath;
    private String name;
    private MusicAlbum parentAlbum;


    public static MusicTrack fromFile(Path filePath) {
        String fileName = filePath.getFileName().toString();

        if(AUDIO_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext))) {
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            
            MusicTrack track = new MusicTrack();
            track.filePath = filePath;
            track.name = name;
            return track;
        }

        return null;
    }

    public static boolean isAudioFile(Path filePath) {
        String fileName = filePath.getFileName().toString();

        return AUDIO_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext));
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getName() {
        return name;
    }

    public void setParentAlbum(MusicAlbum parentAlbum) {
        this.parentAlbum = parentAlbum;
    }

    public MusicAlbum getParentAlbum() {
        return parentAlbum;
    }

    Media loadMedia() {
        return new Media(this.filePath.toUri().toString());
    }
}
