package com.cedro.musicplayer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class MusicDatabase {
    public static final String CONFIG_FILE_EXTENSION = ".mpconfig";

    private ObservableMap<Path, MusicTrack> trackMap = FXCollections.observableHashMap();
    private ObservableMap<Path, MusicAlbum> albumMap = FXCollections.observableHashMap();
    private ObservableList<MusicCollection> userCollectionList = FXCollections.observableArrayList();



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

    public ObservableMap<Path, MusicTrack> getTrackMap() {
        return trackMap;
    }

    public ObservableMap<Path, MusicAlbum> getAlbumMap() {
        return albumMap;
    }

    public ObservableList<MusicCollection> getUserCollectionList() {
        return userCollectionList;
    }
    


    // =========================== IO ===========================

    public void saveToConfigurationFile(Path filePath) throws IOException {
        JSONObject json = new JSONObject()
        .put("albums", new JSONArray(
            albumMap.values().stream()
            .map(MusicAlbum::toJSON)
            .collect(Collectors.toList())))
        .put("userCollections", new JSONArray(
            userCollectionList.stream()
            .map(MusicCollection::toJSON)
            .collect(Collectors.toList())));
        
        if(!filePath.toString().endsWith(CONFIG_FILE_EXTENSION)) {
            filePath = filePath.resolveSibling(filePath.getFileName() + CONFIG_FILE_EXTENSION);
        }

        Files.write(filePath, json.toString(4).getBytes());
    }

    public void requestSaveToConfigurationFile(Window window) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(Localization.getString("database_save_config_dialog_file_category"), "*" + CONFIG_FILE_EXTENSION);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Localization.getString("database_save_config_dialog_title"));
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        fileChooser.setInitialFileName("music" + MusicDatabase.CONFIG_FILE_EXTENSION);
        fileChooser.setInitialDirectory(new File("."));

        File selectedFile = fileChooser.showSaveDialog(window);
        if(selectedFile != null) {
            try {
                saveToConfigurationFile(selectedFile.toPath());
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(Localization.getString("database_save_config_dialog_error_title"));
                alert.setHeaderText(Localization.getString("database_save_config_dialog_error_header"));
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    // If error occurs returns non-empty string
    public void loadFromConfigurationFile(Path filePath) throws Exception {
        if(!filePath.toString().endsWith(CONFIG_FILE_EXTENSION)) {
            throw new Exception("File extension must be " + CONFIG_FILE_EXTENSION);
        }

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
    }

    public void requestLoadFromConfigurationFile(Window window) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(Localization.getString("database_load_config_dialog_file_category"), "*" + CONFIG_FILE_EXTENSION);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Localization.getString("database_load_config_dialog_title"));
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        fileChooser.setInitialDirectory(new File("."));

        File selectedFile = fileChooser.showOpenDialog(window);
        if(selectedFile != null) {
            try {
                loadFromConfigurationFile(selectedFile.toPath());
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(Localization.getString("database_load_config_dialog_error_title"));
                alert.setHeaderText(Localization.getString("database_load_config_dialog_error_header"));
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void loadFromFileSystem(Path dirPath) {
        var albums = MusicAlbum.fromDirectoryRecurse(dirPath);
        addAlbums(albums);
    }

    public void requestLoadFromFileSystem(Window window) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(Localization.getString("database_load_from_filesystem_dialog_title"));
        directoryChooser.setInitialDirectory(new File("."));

        File selectedDirectory = directoryChooser.showDialog(window);
        if(selectedDirectory != null) {
            loadFromFileSystem(selectedDirectory.toPath());
        }
    }
}
