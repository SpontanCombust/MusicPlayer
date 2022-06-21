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

public class MusicCollection {
    public static final Image DEFAULT_COVER_IMAGE = new Image(MusicAlbum.class.getResourceAsStream("record_disk.png"));
    
    protected String name;
    protected Path coverImagePath;
    protected List<Path> tracksPaths = new ArrayList<>();


    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public void setCoverImagePath(Path coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public Path getCoverImagePath() {
        return coverImagePath;
    }

    public Image getCoverImage() {
        if(coverImagePath != null) {
            return new Image(coverImagePath.toUri().toString());
        }

        return DEFAULT_COVER_IMAGE;
    }

    public List<Path> getTracksPaths() {
        return this.tracksPaths;
    }

    public List<MusicTrack> getTracks() {
        return Jukebox.getInstance()
        .getMusicDatabase()
        .getTrackMap()
        .values().stream()
        .filter(t -> this.tracksPaths.contains(t.getFilePath()))
        .collect(Collectors.toList());
    }
    
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

    public static MusicCollection fromJSON(JSONObject json) throws JSONException {
        MusicCollection collection = new MusicAlbum();
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
