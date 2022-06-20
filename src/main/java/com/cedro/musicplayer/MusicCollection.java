package com.cedro.musicplayer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class MusicCollection {
    public static final Image DEFAULT_COVER_IMAGE = new Image(MusicAlbum.class.getResourceAsStream("record_disk.png"));
    
    protected String name;
    protected Path coverImagePath;

    protected List<MusicTrack> tracks = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setCoverImagePath(Path coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public Image getCoverImage() {
        if(coverImagePath != null) {
            return new Image(coverImagePath.toUri().toString());
        }

        return DEFAULT_COVER_IMAGE;
    }

    public List<MusicTrack> getTracks() {
        return tracks;
    }
}
