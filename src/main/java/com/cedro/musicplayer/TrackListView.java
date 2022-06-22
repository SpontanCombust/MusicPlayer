package com.cedro.musicplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class TrackListView extends AnchorPane {
    @FXML
    protected AnchorPane rootPane;
    @FXML
    protected ListView<String> listView;

    protected List<MusicTrack> tracks = new ArrayList<>();



    public TrackListView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-list-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();  
    }
 
    @FXML
    public void initialize() {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        populateListItems();
    }

    @FXML
    protected void onListItemSelected(MouseEvent e) {
        
    }

    @FXML
    protected void onContextMenuRequested(ContextMenuEvent e) {
        
    }



    public List<MusicTrack> getTracks() {
        return tracks;
    }

    public void populateListItems() {
        this.listView.getItems().clear();

        this.listView
        .getItems()
        .addAll(
            this.tracks
            .stream()
            .map(t -> t.getName())
            .collect(Collectors.toList()));
    }

    protected List<MusicTrack> getSelectedTracks() {
        return this.listView
        .getSelectionModel()
        .getSelectedIndices()
        .stream()
        .map(i -> this.tracks.get(i))
        .collect(Collectors.toList());
    }
}
