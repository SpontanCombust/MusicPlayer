package com.cedro.musicplayer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class TrackListController  {
    @FXML
    private VBox rootPane;
    @FXML
    private ListView<String> musicListView;


    private final List<String> audioExtensions;

    public TrackListController() throws IOException {
        audioExtensions = Arrays.asList(
            "aif", "aiff", 
            "mp3",
            "mp4", "m4a", "m4v",
            "wav");

        
    }

    @FXML
    protected void onLoadMusicButtonClick() {
        var directoryChooser = new DirectoryChooser();
        
        File selectedDirectory = directoryChooser.showDialog(rootPane.getScene().getWindow());
        if(selectedDirectory != null) {
            this.musicListView.getItems().clear();
            this.fillMusicListView(selectedDirectory);
        }
    }

    private void fillMusicListView(File musicDir) {
        if(musicDir.isDirectory()) {
            var musicFiles = musicDir.listFiles();
            for(File file: musicFiles) {
                if(file.isDirectory()) {
                    this.fillMusicListView(file);
                } else if(this.isMusicFile(file)){
                    //!TEMPORARY for now will only set the list item to music path, so we can use this path later
                    musicListView.getItems().add(file.getAbsolutePath());
                }
            }
        } 
    }

    private boolean isMusicFile(File file) {
        int dotIndex = file.getName().lastIndexOf(".");
        if( dotIndex != -1 ) {
            String extension = file.getName().substring(dotIndex + 1);
            return audioExtensions.stream().anyMatch(e -> e.equals(extension));
        }

        return false;
    }
}
