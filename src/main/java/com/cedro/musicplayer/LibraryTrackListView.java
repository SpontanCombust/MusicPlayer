package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class LibraryTrackListView extends TrackListView {

    public LibraryTrackListView() throws IOException {
        super();
    }

    @Override
    public void initialize() {
        super.initialize();
        this.listView.setContextMenu(getContextMenu());
    }

    private ContextMenu getContextMenu() {
        List<MusicTrack> selectedTracks = getSelectedTracks();
        ContextMenu menu = new ContextMenu();


        MenuItem menuItemAddToPlaylist = new MenuItem();
        menuItemAddToPlaylist.setOnAction(e -> {
            Jukebox.getInstance().getPlaylist().addAll(selectedTracks);
        });
        menuItemAddToPlaylist.setText("Add to playlist");

        menu.getItems().add(menuItemAddToPlaylist);


        Menu menuAddToUserCollection = new Menu();
        menuAddToUserCollection.setText("Add to collection");

        Jukebox.getInstance().getMusicDatabase().getUserCollectionList().forEach(collection -> {
            MenuItem collectionItem = new MenuItem();
            collectionItem.setText(collection.getName());
            collectionItem.setOnAction(e -> {
                collection.addTracks(selectedTracks);
            });
            menuAddToUserCollection.getItems().add(collectionItem);
        });

        MenuItem newCollectionItem = new MenuItem();
        newCollectionItem.setText("New collection...");

        TextInputDialog tid = new TextInputDialog();
        tid.setTitle("New collection");
        tid.setHeaderText("Enter new collection name");
        newCollectionItem.setOnAction(e -> {
            Optional<String> result = tid.showAndWait();
            if(result.isPresent()) {
                MusicCollection newCollection = new MusicCollection();
                newCollection.setName(result.get());
                newCollection.addTracks(selectedTracks);
                Jukebox.getInstance().getMusicDatabase().addUserCollection(newCollection);
            }
        });

        menuAddToUserCollection.getItems().add(newCollectionItem);


        menu.getItems().add(menuAddToUserCollection);

        return menu;
    }
}
