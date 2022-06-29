package com.cedro.musicplayer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A class representing a music album.
 */
public class MusicAlbum extends MusicCollection {
    /**
     * All the extenions that cover images can have
     */
    public static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
        "bmp", "gif", "jpg", "jpeg", "png"
    );


    /**
     * Absolute directory path to the album in the system
     */
    protected Path dirPath;


    /**
     * Creates a MusicAlbum object by doing a shallow scan on a given directory. 
     * If comes accross an image sets it as the album cover.
     * 
     * @param directoryPath - Absolute directory path to the album in the system
     * @return MusicAlbum - object or null on error
     */
    public static MusicAlbum fromDirectory(Path directoryPath) {
        List<File> files = Arrays.asList(directoryPath.toFile().listFiles());

        if(files.stream().anyMatch(f -> MusicTrack.isAudioFile(f.toPath().toAbsolutePath()))) {
            MusicAlbum album = new MusicAlbum();
            album.dirPath = directoryPath.toAbsolutePath();
            album.name = directoryPath.getFileName().toString();

            files.stream()
            .filter(f -> f.isFile())
            .forEach(f -> {
                Path filePath = f.toPath().toAbsolutePath();
                if(MusicTrack.isAudioFile(filePath)) {
                    album.tracksPaths.add(filePath);
                } else if(album.coverImagePath == null && isImageFile(f.toPath())) {
                    album.coverImagePath = filePath;
                }
            });

            return album;
        }

        return null;
    }

    /**
     * Creates a list of MusicAlbum objects by doing a recursive scan on a given directory. 
     * If comes accross an image sets it as the album cover.
     * 
     * @param directoryPath - Absolute directory path to the album in the system
     * @return List<MusicAlbum> - list of albums
     */
    public static List<MusicAlbum> fromDirectoryRecurse(Path rootDirectory) {
        List<MusicAlbum> albums = new ArrayList<>();
        File fileDir = rootDirectory.toFile();

        if(fileDir.isDirectory() && !fileDir.isHidden()) {
            MusicAlbum album = fromDirectory(rootDirectory);
            if(album != null) {
                albums.add(album);
            }
            
            for(File f: fileDir.listFiles()) {
                List<MusicAlbum> subAlbums = fromDirectoryRecurse(f.toPath());
                albums.addAll(subAlbums);
            }
        }

        return albums;
    }

    /**
     * Checks whether a file at this path is an image file.
     * 
     * @param filePath - path to the file
     * @return boolean - true if it is an image file, false otherwise
     */
    private static boolean isImageFile(Path filePath) {
        String fileName = filePath.getFileName().toString();

        return IMAGE_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext));
    }

    /**
     * Returns the absolute path to the album in the system.
     * @return Path - absolute path to the album in the system
     */
    public Path getDirPath() {
        return dirPath;
    }


    /**
     * Returns the JSON representation of the album.
     */
    public JSONObject toJSON() {
        return new JSONObject()
        .put("dirPath", dirPath.toString())
        .put("name", name)
        .put("coverImagePath", coverImagePath != null ? coverImagePath.toString() : null)
        .put("tracks", new JSONArray().putAll(
            tracksPaths
            .stream()
            .map(p -> p.toString())
            .collect(Collectors.toList())));
    }

    /**
     * Creates a MusicAlbum object from a JSON representation.
     * 
     * @param json - parsed JSON object
     * @return MusicAlbum - created album
     * @throws JSONException
     */
    public static MusicAlbum fromJSON(JSONObject json) throws JSONException {
        MusicAlbum album = new MusicAlbum();
        album.dirPath = Paths.get(json.getString("dirPath"));
        album.name = json.getString("name");

        if(json.isNull("coverImagePath")) {
            album.coverImagePath = null;
        } else {
            album.coverImagePath = Paths.get(json.getString("coverImagePath"));
        }
        
        json.getJSONArray("tracks")
        .forEach(o -> album.tracksPaths.add(Paths.get((String)o)));
        
        return album;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        MusicAlbum other = (MusicAlbum) obj;
        if (dirPath == null) {
            if (other.dirPath != null)
                return false;
        } else if (!dirPath.equals(other.dirPath))
            return false;
        return true;
    }
}
