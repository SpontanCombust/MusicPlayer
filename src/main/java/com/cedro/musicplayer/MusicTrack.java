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


    private MusicTrack(Path filePath) {
        this.filePath = filePath;
    }

    public static MusicTrack fromFile(Path filePath) {
        String fileName = filePath.getFileName().toString();

        if(AUDIO_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext))) {
            return new MusicTrack(filePath.toAbsolutePath());
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
        String fileName = filePath.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public Path getParentAlbumPath() {
        return filePath.getParent();
    }

    public MusicAlbum getParentAlbum() {
        return Jukebox.getInstance()
        .getMusicDatabase()
        .getAlbumMap()
        .get(this.getParentAlbumPath());
    }

    Media loadMedia() {
        return new Media(this.filePath.toUri().toString());
    }
}
