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
public abstract class TrackListView extends AnchorPane {
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
    @FXML
    protected void onListItemSelected(MouseEvent e) {
        
    }



    /**
     * Returns stored list of tracks
     * @return List<MusicTrack> - list of stored tracks
     */
    public List<MusicTrack> getTracks() {
        return null;
    }

    /**
     * Fills items of the list view with names of stored tracks
     */
    public void populateListItems() {
        this.listView.getItems().clear();

        this.listView
        .getItems()
        .addAll(
            this.getTracks()
            .stream()
            .map(t -> t.getName())
            .collect(Collectors.toList()));
    }

    /**
     * Sets a context menu for the list view
     */
    public void setupContextMenu() {
        try {
            ContextMenu cm = new TrackListContextMenu(this);
            this.listView.setContextMenu(cm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the tracks that are selected in the list view
     * @return List<MusicTrack> - list of selected tracks
     */
    public List<MusicTrack> getSelectedTracks() {
        return this.listView
        .getSelectionModel()
        .getSelectedIndices()
        .stream()
        .map(i -> this.getTracks().get(i))
        .collect(Collectors.toList());
    }
}
