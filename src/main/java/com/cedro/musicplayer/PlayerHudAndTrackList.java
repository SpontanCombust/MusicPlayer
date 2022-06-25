package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class PlayerHudAndTrackList extends VBox {

    @FXML
    private PlaylistTrackListView playlistTrackListPane;

    public PlayerHudAndTrackList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerhud-and-tracklist-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load(); 
    }

    @FXML
    public void initialize() {
        this.playlistTrackListPane.populateListItems();
    }
}
