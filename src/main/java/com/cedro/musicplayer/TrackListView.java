package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Abstract class for the ListView that displays music tracks.
 */
public abstract class TrackListView extends AnchorPane implements MusicItemListing {
    /**
     * Actual ListView displaying track names
     */
    @FXML
    protected ListView<String> listView;



    /**
     * Constructor
     * @throws IOException
     */
    public TrackListView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-list-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();  
    }
 
    /**
     * Initializes the list view
     */
    @FXML
    public void initialize() {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Handler for when an item in the list view gets clicked on.
     * @param e - mouse event
     */
    @Override
    public void onItemSelected(MouseEvent e) {
        
    }



    /**
     * Returns stored list of tracks
     * @return List<MusicTrack> - list of stored tracks
     */
    @Override
    public List<MusicTrack> fetchTracks() {
        return null;
    }

    /**
     * Fills items of the list view with names of stored tracks
     */
    @Override
    public void populateItems() {
        this.listView.getItems().clear();

        this.listView
        .getItems()
        .addAll(
            this.fetchTracks()
            .stream()
            .map(t -> t.getArtist() + " - " + t.getTitle())
            .collect(Collectors.toList()));
    }

    /**
     * Sets a context menu for the list view
     */
    @Override
    public void setupContextMenu() {
        try {
            ContextMenu cm = new MusicItemListingContextMenu(this);
            this.listView.setContextMenu(cm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the tracks that are selected in the list view
     * @return List<MusicTrack> - list of selected tracks
     */
    @Override
    public List<MusicTrack> getSelectedTracks() {
        return this.listView
        .getSelectionModel()
        .getSelectedIndices()
        .stream()
        .map(i -> this.fetchTracks().get(i))
        .collect(Collectors.toList());
    }
}
