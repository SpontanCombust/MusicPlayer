package com.cedro.musicplayer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.scene.image.Image;

/**
 * Class representing a collection of music tracks.
 */
public class MusicCollection {
    /**
     * Image object of the default cover used for the collection.
     */
    public static final Image DEFAULT_COVER_IMAGE = new Image(MusicAlbum.class.getResourceAsStream("record_disk.png"));
    
    /**
     * Name of the collection.
     */
    protected String name;
    /**
     * Path to the cover image of the collection.
     */
    protected Path coverImagePath;
    /**
     * List of paths to the tracks in the collection.
     */
    protected List<Path> tracksPaths = new ArrayList<>();


    /**
     * Sets the name of the collection.
     * @param name - name of the collection
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the name of the collection.
     * @return String - name of the collection
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the path to the cover image of the collection.
     * @param coverImagePath - path to the cover image of the collection
     */
    public void setCoverImagePath(Path coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    /**
     * Returns the path to the cover image of the collection.
     * @return Path - path to the cover image of the collection
     */
    public Path getCoverImagePath() {
        return coverImagePath;
    }

    /**
     * Returns the Image object of the cover image of the collection.
     * @return Image - Image object of the cover image of the collection
     */
    public Image getCoverImage() {
        if(coverImagePath != null) {
            return new Image(coverImagePath.toUri().toString());
        }

        return DEFAULT_COVER_IMAGE;
    }

    /**
     * Returns paths to the tracks in the collection.
     * @return List<Path> - paths to the tracks in the collection
     */
    public List<Path> getTracksPaths() {
        return this.tracksPaths;
    }

    /**
     * Fetches tracks from the database based on the stored track paths.
     * @return List<MusicTrack> - music tracks assigned to collection
     */
    public List<MusicTrack> getTracks() {
        return Jukebox.getInstance()
        .getMusicDatabase()
        .getTrackMap()
        .values().stream()
        .filter(t -> this.tracksPaths.contains(t.getFilePath()))
        .collect(Collectors.toList());
    }

    /**
     * Adds tracks to the collection
     * @param tracks - tracks to add
     */
    public void addTracks(List<MusicTrack> tracks) {
        tracks.stream().forEach(t -> this.tracksPaths.add(t.getFilePath()));
    }

    /**
     * Removes tracks from the collection
     * @param tracks - tracks to remove
     */
    public void removeTracks(List<MusicTrack> tracks) {
        tracks.stream().forEach(t -> this.tracksPaths.remove(t.getFilePath()));
    }
    
    /**
     * Returns a JSON representation of the collection
     * @return JSONObject - JSON representation of the collection
     */
    public JSONObject toJSON() {
        return new JSONObject()
        .put("name", name)
        .put("coverImagePath", coverImagePath != null ? coverImagePath.toString() : null)
        .put("tracks", new JSONArray().putAll(
            tracksPaths
            .stream()
            .map(p -> p.toString())
            .collect(Collectors.toList())));
    }

    /**
     * Creates a MusicCollection object based on its JSON representation
     * @param json - JSON representation of the collection
     * @return MusicCollection object
     * @throws JSONException
     */
    public static MusicCollection fromJSON(JSONObject json) throws JSONException {
        MusicCollection collection = new MusicCollection();
        collection.name = json.getString("name");

        if(json.isNull("coverImagePath")) {
            collection.coverImagePath = null;
        } else {
            collection.coverImagePath = Paths.get(json.getString("coverImagePath"));
        }
        
        json.getJSONArray("tracks")
        .forEach(o -> collection.tracksPaths.add(Paths.get((String)o)));
        
        return collection;
    }
}
