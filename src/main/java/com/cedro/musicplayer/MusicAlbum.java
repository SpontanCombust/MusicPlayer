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


public class MusicAlbum extends MusicCollection {
    public static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
        "bmp", "gif", "jpg", "jpeg", "png"
    );


    protected Path dirPath;


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

    private static boolean isImageFile(Path filePath) {
        String fileName = filePath.getFileName().toString();

        return IMAGE_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext));
    }

    public Path getDirPath() {
        return dirPath;
    }


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
}
