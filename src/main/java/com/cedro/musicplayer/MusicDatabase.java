package com.cedro.musicplayer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicDatabase {
    private HashMap<Path, MusicTrack> trackMap = new HashMap<>();
    private HashMap<Path, MusicAlbum> albumMap = new HashMap<>();
    private ArrayList<MusicCollection> userCollectionList = new ArrayList<>();


    public void clearAlbums() {
        albumMap.clear();
    }

    public void addAlbum(MusicAlbum album) {
        this.albumMap.put(album.getDirPath(), album);
        album.tracks.forEach(t -> this.trackMap.put(t.getFilePath(), t));
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


    // ==== READ ONLY ACCESSORS ====
    public Map<Path, MusicTrack> getTrackMap() {
        return Collections.unmodifiableMap(trackMap);
    }

    public Map<Path, MusicAlbum> getAlbumMap() {
        return Collections.unmodifiableMap(albumMap);
    }

    public List<MusicCollection> getUserCollectionList() {
        return Collections.unmodifiableList(userCollectionList);
    }
    
}
