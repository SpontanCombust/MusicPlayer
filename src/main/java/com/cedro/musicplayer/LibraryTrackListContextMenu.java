package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class LibraryTrackListContextMenu extends ContextMenu {
    @FXML
    private Menu menuAddToUserCollection;

    private TrackListView parentTrackListView;


    public LibraryTrackListContextMenu(TrackListView trackListView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("library-track-list-context-menu-view.fxml"), ResourceBundle.getBundle("com.cedro.musicplayer.strings"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        this.parentTrackListView = trackListView;
    }

    @FXML
    void initialize() {
        List<MenuItem> items = 
        Jukebox.getInstance()
        .getMusicDatabase()
        .getUserCollectionList().stream()
        .map(collection -> {
            MenuItem collectionItem = new MenuItem();
            collectionItem.setText(collection.getName());
            collectionItem.setOnAction(e -> {
                collection.addTracks(this.parentTrackListView.getSelectedTracks());
            });

            return collectionItem;

        }).collect(Collectors.toList());

        // inserting to the beginning so the "new collection" item is at the bottom
        menuAddToUserCollection.getItems().addAll(0, items);
    }

    @FXML
    void onAddToPlaylist(ActionEvent event) {
        Jukebox.getInstance().getPlaylist().addAll(this.parentTrackListView.getSelectedTracks());
    }

    @FXML
    void onAddToNewCollection(ActionEvent event) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.cedro.musicplayer.strings");
        
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle(bundle.getString("library_track_list_context_menu_menu_add_to_collection_dialog_title"));
        tid.setHeaderText(bundle.getString("library_track_list_context_menu_menu_add_to_collection_dialog_header"));

        Optional<String> result = tid.showAndWait();
        if(result.isPresent()) {
            MusicCollection newCollection = new MusicCollection();
            newCollection.setName(result.get());
            newCollection.addTracks(this.parentTrackListView.getSelectedTracks());
            Jukebox.getInstance().getMusicDatabase().addUserCollection(newCollection);
        }
    }

}
