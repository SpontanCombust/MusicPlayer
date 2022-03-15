package com.cedro.musicplayer;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class TrackList extends VBox {
    @FXML
    private VBox rootPane;
    @FXML
    private ListView<String> musicListView;



    public TrackList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-list-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();  
    }

    @FXML
    public void initialize() {
        // when the track is changed automatically or with prev/next buttons
        Jukebox.getInstance().currentTrackIndex.addListener(
            (observable, oldVal, newVal) -> {
                this.musicListView.getSelectionModel().select(newVal.intValue());
            }
        );
    }

    @FXML
    protected void onLoadMusicButtonClick() {
        var directoryChooser = new DirectoryChooser();
        
        File selectedDirectory = directoryChooser.showDialog(rootPane.getScene().getWindow());
        if(selectedDirectory != null) {
            this.musicListView.getItems().clear();

            Playlist playlist = Jukebox.getInstance().getPlaylist();
            playlist.clearTracks();
            playlist.loadTracksFromDirectory(selectedDirectory, true);
            
            this.musicListView.getItems().addAll(playlist.getTrackNames());
        }
    }

    @FXML
    protected void onTrackSelected(MouseEvent e) {
        if(e.getClickCount() >= 2) {
            int trackIdx = this.musicListView.getSelectionModel().getSelectedIndex();
            Jukebox.getInstance().selectTrack(trackIdx);
            Jukebox.getInstance().play();
        }
    }
}
