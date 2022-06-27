package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class TrackListContextMenu extends ContextMenu {
    @FXML
    private Menu menuAddToUserCollection;
    @FXML
    private MenuItem menuItemRemoveTracks;

    private TrackListView parentTrackListView;


    public TrackListContextMenu(TrackListView trackListView) throws IOException {
        this.parentTrackListView = trackListView;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-list-context-menu-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @FXML
    void initialize() {
        populateAddToUserCollectionMenu();
        removeRemoveMenuItemIfNecessary();
    }

    @FXML
    void onAddToPlaylist(ActionEvent event) {
        Jukebox.getInstance().getPlaylist().addAll(this.parentTrackListView.getSelectedTracks());
    }

    @FXML
    void onAddToNewCollection(ActionEvent event) {
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle(Localization.getString("track_list_context_menu_menu_add_to_collection_dialog_title"));
        tid.setHeaderText(Localization.getString("track_list_context_menu_menu_add_to_collection_dialog_header"));

        Optional<String> result = tid.showAndWait();
        if(result.isPresent()) {
            MusicCollection newCollection = new MusicCollection();
            newCollection.setName(result.get());
            newCollection.addTracks(this.parentTrackListView.getSelectedTracks());
            Jukebox.getInstance().getMusicDatabase().addUserCollection(newCollection);
            populateAddToUserCollectionMenu();
        }
    }

    @FXML
    void onRemoveTracks(ActionEvent event) {
        if(this.parentTrackListView instanceof CollectionTrackListView) {
            var collectionTLV = (CollectionTrackListView) this.parentTrackListView;
            collectionTLV.getCollection().removeTracks(collectionTLV.getSelectedTracks());
            collectionTLV.populateListItems();
        } else if(this.parentTrackListView instanceof PlaylistTrackListView) {
            var playlistTLV = (PlaylistTrackListView) this.parentTrackListView;
            Jukebox.getInstance().getPlaylist().removeAll(playlistTLV.getSelectedTracks());
            playlistTLV.populateListItems();
        }
    }


    private void populateAddToUserCollectionMenu() {
        List<MenuItem> items = 
        Jukebox.getInstance()
        .getMusicDatabase()
        .getUserCollectionList().stream()
        .map(collection -> {
            MenuItem collectionItem = new MenuItem();
            collectionItem.setText(collection.getName());
            collectionItem.setOnAction(e -> {
                collection.addTracks(this.parentTrackListView.getTracks());
            });

            return collectionItem;

        }).collect(Collectors.toList());

        // inserting to the beginning so the "new collection" item is at the bottom
        menuAddToUserCollection.getItems().remove(0, menuAddToUserCollection.getItems().size() - 1);
        menuAddToUserCollection.getItems().addAll(0, items);
    }

    private void removeRemoveMenuItemIfNecessary() {
        if(this.parentTrackListView instanceof PlaylistTrackListView) {
            return;
        }
        
        if(this.parentTrackListView instanceof CollectionTrackListView) {
            var collectionTLV = (CollectionTrackListView) this.parentTrackListView;
            if(!(collectionTLV.getCollection() instanceof MusicAlbum)) {
                return;
            }
        }

        this.getItems().remove(menuItemRemoveTracks);
    }
}
