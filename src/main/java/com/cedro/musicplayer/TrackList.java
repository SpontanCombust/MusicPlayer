package com.cedro.musicplayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.stage.DirectoryChooser;

public class TrackList extends VBox {
    @FXML
    private VBox rootPane;
    @FXML
    private ListView<String> musicListView;

    private Playlist playlist = new Playlist();

    public TrackList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-list-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @FXML
    protected void onLoadMusicButtonClick() {
        var directoryChooser = new DirectoryChooser();
        
        File selectedDirectory = directoryChooser.showDialog(rootPane.getScene().getWindow());
        if(selectedDirectory != null) {
            this.musicListView.getItems().clear();
            this.playlist.clearTracks();
            this.loadMusicFromDirectory(selectedDirectory);
        }
    }

    @FXML
    protected void onTrackSelected(MouseEvent e) {
        if(e.getClickCount() == 2) {
            // System.out.println(!musicListView.getSelectionModel().isEmpty());
        }
    }

    private void loadMusicFromDirectory(File musicDir) {
        if(musicDir.isDirectory()) {
            var paths = new ArrayList<String>();
            var tracks = new ArrayList<Media>();

            var musicFiles = musicDir.listFiles();
            for(File file: musicFiles) {
                if(file.isDirectory()) {
                    this.loadMusicFromDirectory(file);
                } else if(this.playlist.isAudioFile(file)){
                    var path = file.getAbsolutePath();
                    var mediaOpt = loadMedia(path);
                    if(mediaOpt.isPresent()) {
                        paths.add(path);
                        tracks.add(mediaOpt.get());
                    }
                }
            }

            this.musicListView.getItems().addAll(paths);
            this.playlist.addTracks(tracks);
        } 
    }

    private Optional<Media> loadMedia(String path) {
        Optional<Media> opt = Optional.empty();

        try {
            var media = new Media(Paths.get(path).toUri().toString());
            opt = Optional.of(media);
        } catch (MediaException e) {
            System.out.println(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return opt;
    }
}
