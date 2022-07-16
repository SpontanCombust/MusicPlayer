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

/**
 * ContextMenu class for the track list view.
 */
public class MusicItemListingContextMenu extends ContextMenu {
    /**
     * Menu with items that add tracks to collections
     */
    @FXML
    private Menu menuAddToUserCollection;
    /**
     * Menu item that removes tracks from the track list
     */
    @FXML
    private MenuItem menuItemRemoveTracks;

    /**
     * MusicItemListing that the context menu is set for
     */
    private MusicItemListing parentMusicItemListing;


    /**
     * Constructor
     * @param musicItemListing - parent TrackListView
     * @throws IOException
     */
    public MusicItemListingContextMenu(MusicItemListing musicItemListing) throws IOException {
        this.parentMusicItemListing = musicItemListing;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("music-item-listing-context-menu-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    /**
     * Initializes the context menu
     */
    @FXML
    void initialize() {
        populateAddToUserCollectionMenu();
        removeRemoveMenuItemIfNecessary();
    }

    /**
     * Handler for the menu item that adds tracks to the playlist
     * @param event - menu item choice event
     */
    @FXML
    void onAddToPlaylist(ActionEvent event) {
        Jukebox.getInstance().getPlaylist().addAll(this.parentMusicItemListing.getSelectedTracks());
    }

    /**
     * Handler for the menu item that creates a new collection and adds selected tracks to it
     * @param event - menu item choice event
     */
    @FXML
    void onAddToNewCollection(ActionEvent event) {
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle(Localization.getString("track_list_context_menu_menu_add_to_collection_dialog_title"));
        tid.setHeaderText(Localization.getString("track_list_context_menu_menu_add_to_collection_dialog_header"));

        Optional<String> result = tid.showAndWait();
        if(result.isPresent()) {
            MusicCollection newCollection = new MusicCollection();
            newCollection.setName(result.get());
            newCollection.addTracks(this.parentMusicItemListing.getSelectedTracks());
            Jukebox.getInstance().getMusicDatabase().addUserCollection(newCollection);
            populateAddToUserCollectionMenu();
        }
    }

    /**
     * Handler for the menu item that removes tracks from a track list
     * @param event - menu item choice event
     */
    @FXML
    void onRemoveTracks(ActionEvent event) {
        if(this.parentMusicItemListing instanceof CollectionTrackListView) {
            var collectionTLV = (CollectionTrackListView) this.parentMusicItemListing;
            collectionTLV.getCollection().removeTracks(collectionTLV.getSelectedTracks());
            collectionTLV.populateItems();
        } else if(this.parentMusicItemListing instanceof PlaylistTrackListView) {
            var playlistTLV = (PlaylistTrackListView) this.parentMusicItemListing;
            Jukebox.getInstance().getPlaylist().removeAll(playlistTLV.getSelectedTracks());
            playlistTLV.populateItems();
        }
    }

    @FXML
    void onShowTrackInfo(ActionEvent event) throws IOException {
        TrackInfoDialog dialog = new TrackInfoDialog(this.parentMusicItemListing.getSelectedTracks().get(0));
        dialog.show();
    }


    /**
     * Populates the menu with the user collections to which tracks can be added
     */
    private void populateAddToUserCollectionMenu() {
        List<MenuItem> items = 
        Jukebox.getInstance()
        .getMusicDatabase()
        .getUserCollectionList().stream()
        .map(collection -> {
            MenuItem collectionItem = new MenuItem();
            collectionItem.setText(collection.getName());
            collectionItem.setOnAction(e -> {
                collection.addTracks(this.parentMusicItemListing.fetchTracks());
            });

            return collectionItem;

        }).collect(Collectors.toList());

        // inserting to the beginning so the "new collection" item is at the bottom
        menuAddToUserCollection.getItems().remove(0, menuAddToUserCollection.getItems().size() - 1);
        menuAddToUserCollection.getItems().addAll(0, items);
    }

    /**
     * Removes the "remove" menu item if the track list is not a playlist or album
     */
    private void removeRemoveMenuItemIfNecessary() {
        if(this.parentMusicItemListing instanceof PlaylistTrackListView) {
            return;
        }
        
        if(this.parentMusicItemListing instanceof CollectionTrackListView) {
            var collectionTLV = (CollectionTrackListView) this.parentMusicItemListing;
            if(!(collectionTLV.getCollection() instanceof MusicAlbum)) {
                return;
            }
        }

        this.getItems().remove(menuItemRemoveTracks);
    }
}
