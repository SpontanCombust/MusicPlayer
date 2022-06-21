package com.cedro.musicplayer;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MusicDatabase {
    public static final String DB_FILE_EXTENSION = ".musicdb";

    private HashMap<Path, MusicTrack> trackMap = new HashMap<>();
    private HashMap<Path, MusicAlbum> albumMap = new HashMap<>();
    private ArrayList<MusicCollection> userCollectionList = new ArrayList<>();



    // ==================== MODIFIER WRAPPERS ===================

    public void clearAlbums() {
        albumMap.clear();
    }

    public void addAlbum(MusicAlbum album) {
        this.albumMap.put(album.getDirPath().toAbsolutePath(), album);
        album.tracksPaths.forEach(p -> {
            MusicTrack track = MusicTrack.fromFile(p);
            if(track != null) {
                this.trackMap.put(p, track);
            } else {
                album.tracksPaths.remove(p);
            }
        });
    }

    public void addAlbums(List<MusicAlbum> albums) {
        albums.forEach(this::addAlbum);
    }

    public void clearUserCollections() {
        userCollectionList.clear();
    }

    public void addUserCollection(MusicCollection collection) {
        this.userCollectionList.add(collection);
    }

    public void addUserCollections(List<MusicCollection> collections) {
        this.userCollectionList.addAll(collections);
    }


    // ================== READ ONLY ACCESSORS ==================

    public Map<Path, MusicTrack> getTrackMap() {
        return Collections.unmodifiableMap(trackMap);
    }

    public Map<Path, MusicAlbum> getAlbumMap() {
        return Collections.unmodifiableMap(albumMap);
    }

    public List<MusicCollection> getUserCollectionList() {
        return Collections.unmodifiableList(userCollectionList);
    }
    


    // =========================== IO ===========================

    public void saveToFile(Path filePath) throws IOException {
        JSONObject json = new JSONObject()
        .put("albums", new JSONArray(
            albumMap.values().stream()
            .map(MusicAlbum::toJSON)
            .collect(Collectors.toList())))
        .put("userCollections", new JSONArray(
            userCollectionList.stream()
            .map(MusicCollection::toJSON)
            .collect(Collectors.toList())));
        
        if(!filePath.toString().endsWith(DB_FILE_EXTENSION)) {
            filePath = filePath.resolveSibling(filePath.getFileName() + DB_FILE_EXTENSION);
        }

        Files.write(filePath, json.toString().getBytes());
    }

    // If error occurs returns non-empty string
    public String loadFromFile(Path filePath) {
        if(!filePath.toString().endsWith(DB_FILE_EXTENSION)) {
            return "File extension must be " + DB_FILE_EXTENSION;
        }

        try {
            FileReader fr = new FileReader(filePath.toFile());
            JSONTokener tokener = new JSONTokener(fr);
            JSONObject root = new JSONObject(tokener);

            JSONArray albumsJSON = root.getJSONArray("albums");
            for(int i = 0; i < albumsJSON.length(); i++) {
                MusicAlbum album = MusicAlbum.fromJSON(albumsJSON.getJSONObject(i));
                if(album != null) {
                    addAlbum(album);
                }
            }

            JSONArray collectionsJSON = root.getJSONArray("userCollections");
            for(int i = 0; i < collectionsJSON.length(); i++) {
                MusicCollection collection = MusicCollection.fromJSON(collectionsJSON.getJSONObject(i));
                if(collection != null) {
                    addUserCollection(collection);
                }
            }
            
        } catch (Exception e) {
            return e.toString();
        }

        return null;
    }
}
