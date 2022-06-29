package com.cedro.musicplayer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javafx.scene.media.Media;

/**
 * Class representing a music track
 */
public class MusicTrack {
    /**
     * Supported audio file formats
     */
    public static final List<String> AUDIO_EXTENSIONS = Arrays.asList(
        "aif", "aiff", "mp3", "mp4", "m4a", "m4v", "wav"
    );


    /**
     * Path to the music file
     */
    private Path filePath;


    /**
     * Constructor
     * 
     * @param filePath - path to the music file
     */
    private MusicTrack(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Creates a MusicTrack object by checking the file the given path points to
     * 
     * @param filePath - path to a music file
     * @return MusicTrack object or null on error
     */
    public static MusicTrack fromFile(Path filePath) {
        if(Files.exists(filePath)) {
            String fileName = filePath.getFileName().toString();
    
            if(AUDIO_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext))) {
                return new MusicTrack(filePath.toAbsolutePath());
            }
        }

        return null;
    }

    /**
     * Returns whether a path points to an audio file
     * @param filePath - path to a file
     * @return true if the file is an audio file, false otherwise
     */
    public static boolean isAudioFile(Path filePath) {
        String fileName = filePath.getFileName().toString();

        return AUDIO_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext));
    }

    /**
     * Returns the path to the music file
     * @return Path - path to the music file
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Returns the name of the music file
     * @return String - name of the music file
     */
    public String getName() {
        String fileName = filePath.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * Returns the path of a parent album of this track
     * @return Path - path of the parent album
     */
    public Path getParentAlbumPath() {
        return filePath.getParent();
    }

    /**
     * Returns the MusicAlbum object, which is a parent of this track by fetching it from the database
     * @return MusicAlbum object
     */
    public MusicAlbum getParentAlbum() {
        return Jukebox.getInstance()
        .getMusicDatabase()
        .getAlbumMap()
        .get(this.getParentAlbumPath());
    }

    /**
     * Loads the music media using the path at which this track is located
     * @return Media object
     */
    Media loadMedia() {
        return new Media(this.filePath.toUri().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MusicTrack other = (MusicTrack) obj;
        if (filePath == null) {
            if (other.filePath != null)
                return false;
        } else if (!filePath.equals(other.filePath))
            return false;
        return true;
    }
}
