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

/**
 * A class that stores all tracks, albums and user collections
 */
public class MusicDatabase {
    /**
     * Extension of application's config file
     */
    public static final String CONFIG_FILE_EXTENSION = ".mpconfig";

    /**
     * Map of all stored music tracks
     */
    private ObservableMap<Path, MusicTrack> trackMap = FXCollections.observableHashMap();
    /**
     * Map of all stored albums
     */
    private ObservableMap<Path, MusicAlbum> albumMap = FXCollections.observableHashMap();
    /**
     * List of all stored user collections
     */
    private ObservableList<MusicCollection> userCollectionList = FXCollections.observableArrayList();



    // ==================== MODIFIER WRAPPERS ===================

    /**
     * Deletes all stored albums
     */
    public void clearAlbums() {
        albumMap.clear();
    }

    /**
     * Stores the album object and all the tracks that it contains 
     * @param album - music album
     */
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

    /**
     * Stores multiple album objects
     * @param albums - music albums
     */
    public void addAlbums(List<MusicAlbum> albums) {
        albums.forEach(this::addAlbum);
    }

    /**
     * Removes stored album
     * @param album - music album
     */
    public void removeAlbum(MusicAlbum album) {
        this.albumMap.remove(album.getDirPath().toAbsolutePath());
        album.tracksPaths.forEach(p -> this.trackMap.remove(p));
    }

    /**
     * Removes all stored user collections
     */
    public void clearUserCollections() {
        userCollectionList.clear();
    }

    /**
     * Stores the user collection
     * @param collection - user collection
     */
    public void addUserCollection(MusicCollection collection) {
        this.userCollectionList.add(collection);
    }

    /**
     * Stores multiple user collections
     * @param collections - user collections
     */
    public void addUserCollections(List<MusicCollection> collections) {
        this.userCollectionList.addAll(collections);
    }

    /**
     * Removes stored user collection
     * @param collection - user collection
     */
    public void removeUserCollection(MusicCollection collection) {
        this.userCollectionList.remove(collection);
    }


    // ================== READ ONLY ACCESSORS ==================

    /**
     * Returns the map of all available music tracks
     * @return ObservableMap<Path, MusicTrack> - track map
     */
    public ObservableMap<Path, MusicTrack> getTrackMap() {
        return trackMap;
    }

    /**
     * Returns the map of all available music albums
     * @return ObservableMap<Path, MusicAlbum> - album map
     */
    public ObservableMap<Path, MusicAlbum> getAlbumMap() {
        return albumMap;
    }

    /**
     * Returns the list of all available user collections
     * @return ObservableList<MusicCollection> - user collection list
     */
    public ObservableList<MusicCollection> getUserCollectionList() {
        return userCollectionList;
    }
    


    // =========================== IO ===========================

    /**
     * Takes all the stored albums and user collections and saves their information into a JSON file.
     * 
     * @param filePath - path to the output file
     * @throws IOException
     */
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

    /**
     * Brings up a save to file prompt that saves the database configuration
     * @param window - owner window of the dialog prompt
     */
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

    /**
     * Takes a configuration json file and parses it to yield music album and user collections data
     * 
     * @param filePath path to the config file
     * @throws Exception
     */
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

    /**
     * Brings up a load from file prompt that loads database data from a configuration file
     * @param window - owner window of the dialog prompt
     */
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

    /**
     * Scans the file system in a given directory to find music albums
     * @param dirPath - path to the directory
     */
    public void loadFromFileSystem(Path dirPath) {
        var albums = MusicAlbum.fromDirectoryRecurse(dirPath);
        addAlbums(albums);
    }

    /**
     * Brings up a choose directory prompt and scans that directory for music albums
     * @param window - owner window of the dialog prompt
     */
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
