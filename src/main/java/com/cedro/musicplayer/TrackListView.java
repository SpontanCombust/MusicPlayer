package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class TrackListView extends VBox {
    private TrackListController controller = new TrackListController();

    public TrackListView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-list-view.fxml"));
        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

    public TrackListController getController() {
        return controller;
    }
}
