package com.cedro.musicplayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.media.Media;
import javafx.util.Pair;

/**
 * Class representing a music track
 */
public class MusicTrack {
    /**
     * Path to the music file
     */
    private Path filePath;

    private static final String UNKNOWN_TAG = Localization.getString("track_info_unknown");
    private SimpleStringProperty title = new SimpleStringProperty();
    private SimpleStringProperty artist = new SimpleStringProperty();
    private SimpleStringProperty album = new SimpleStringProperty();
    private SimpleStringProperty year = new SimpleStringProperty();
    private SimpleStringProperty genre = new SimpleStringProperty();


    /**
     * Private constructor
     * 
     * @param filePath - path to the music file
     */
    private MusicTrack(Path filePath) {
        this.filePath = filePath;

        this.title.set(UNKNOWN_TAG);
        this.artist.set(UNKNOWN_TAG);
        this.album.set(UNKNOWN_TAG);
        this.year.set(UNKNOWN_TAG);
        this.genre.set(UNKNOWN_TAG);
    }



    /**
     * Creates a MusicTrack object by checking the file the given path points to
     * 
     * @param filePath - path to a music file
     * @return MusicTrack object or null on error
     */
    public static MusicTrack fromFile(Path filePath) {
        if(Files.exists(filePath) && isAudioFile(filePath)) {
            MusicTrack track = new MusicTrack(filePath);
            
            try {
                Mp3File mp3File = new Mp3File(filePath.toString());
                if(!mp3File.hasId3v1Tag()) {
                    throw new InvalidDataException();
                }

                ID3v1 tags = mp3File.getId3v1Tag();
                track.title.set(tags.getTitle());
                track.artist.set(tags.getArtist());
                track.album.set(tags.getAlbum());
                track.year.set(tags.getYear());
                track.genre.set(tags.getGenreDescription());

            } catch (UnsupportedTagException | InvalidDataException | IOException e) {
                var artistAndTitle = artistAndTitleFromFilename(track.getFileName());
                track.artist.set(artistAndTitle.getKey());
                track.title.set(artistAndTitle.getValue());
            }

            return track;
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

        return fileName.endsWith(".mp3");
    }

    private static Pair<String, String> artistAndTitleFromFilename(String fileName) {
        String[] split = fileName.split(" - ");

        if(split.length == 2) {
            return new Pair<>(split[0], split[1]);
        }
        else {
            return new Pair<>(UNKNOWN_TAG, fileName);
        }
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
    public String getFileName() {
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

    public String getTitle() {
        return title.get();
    }

    public String getArtist() {
        return artist.get();
    }

    public String getAlbum() {
        return album.get();
    }

    public String getYear() {
        return year.get();
    }

    public String getGenre() {
        return genre.get();
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
