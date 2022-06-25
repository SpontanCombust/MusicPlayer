package com.cedro.musicplayer;

import java.io.File;
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
import javafx.stage.FileChooser;

public class LibraryCollectionTileContextMenu extends ContextMenu {
    
    @FXML
    private Menu menuAddToUserCollection;

    private LibraryCollectionTile parentTile;


    public LibraryCollectionTileContextMenu(LibraryCollectionTile tile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("library-collection-tile-context-menu-view.fxml"), ResourceBundle.getBundle("com.cedro.musicplayer.strings"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        this.parentTile = tile;
    }

    @FXML
    void initialize() {
        populateAddToUserCollectionMenu();
    }
        
    @FXML
    void onAddToPlaylist(ActionEvent event) {
        Jukebox.getInstance().getPlaylist().addAll(this.parentTile.getCollection().getTracks());
    }

    @FXML
    void onAddToNewCollection(ActionEvent event) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.cedro.musicplayer.strings");
        
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle(bundle.getString("library_collection_tile_context_menu_item_add_to_collection_dialog_title"));
        tid.setHeaderText(bundle.getString("library_collection_tile_context_menu_item_add_to_collection_dialog_header"));

        Optional<String> result = tid.showAndWait();
        if(result.isPresent()) {
            MusicCollection newCollection = new MusicCollection();
            newCollection.setName(result.get());
            newCollection.addTracks(this.parentTile.getCollection().getTracks());
            Jukebox.getInstance().getMusicDatabase().addUserCollection(newCollection);
            populateAddToUserCollectionMenu();
        }
    }

    @FXML
    void onChangeCoverImage(ActionEvent event) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.cedro.musicplayer.strings");

        FileChooser fc = new FileChooser();
        fc.setTitle(bundle.getString("library_collection_tile_change_cover_image_dialog_title"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(bundle.getString("library_collection_tile_change_cover_image_dialog_file_category"), MusicAlbum.IMAGE_EXTENSIONS.stream().map(ext -> "*." + ext).collect(Collectors.toList())));
        fc.setInitialDirectory(new File("."));

        File file = fc.showOpenDialog(parentTile.getScene().getWindow());
        if(file != null) {
            this.parentTile.getCollection().setCoverImagePath(file.toPath().toAbsolutePath());
            this.parentTile.reloadView();
        }
    }

    @FXML
    void onChangeName(ActionEvent event) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.cedro.musicplayer.strings");
        
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle(bundle.getString("library_collection_tile_context_menu_item_change_name_dialog_title"));
        tid.setHeaderText(bundle.getString("library_collection_tile_context_menu_item_change_name_dialog_header"));
        tid.setResult(this.parentTile.getCollection().getName());
        
        Optional<String> result = tid.showAndWait();
        if(result.isPresent()) {
            this.parentTile.getCollection().setName(result.get());
            this.parentTile.reloadView();
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
                collection.addTracks(this.parentTile.getCollection().getTracks());
            });

            return collectionItem;

        }).collect(Collectors.toList());

        // inserting to the beginning so the "new collection" item is at the bottom
        menuAddToUserCollection.getItems().remove(0, menuAddToUserCollection.getItems().size() - 1);
        menuAddToUserCollection.getItems().addAll(0, items);
    }
}
