package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * A class representing the library page of the application.
 */
public class LibraryModelController extends AnchorPane {

    /**
     * ListView with all available songs.
     */
    @FXML
    private AllTracksTrackListView allTrackListView;

    /**
     * Stack pane for albumsFlowPane
     */
    @FXML
    private StackPane albumsStackPane;

    /**
     * Flow pane for album tiles.
     */
    @FXML
    private FlowPane albumsFlowPane;

    /**
     * Stack pane for customCollectionsFlowPane
     */
    @FXML
    private StackPane customCollectionsStackPane;

    /**
     * Flow pane for custom collection tiles.
     */
    @FXML
    private FlowPane customCollectionsFlowPane;



    /**
     * Constructor.
     * 
     * @throws IOException
     */
    public LibraryModelController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("library-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    /**
     * Initializes the controller class. This method is automatically called when loading from FXML.
     */
    @FXML
    public void initialize() {
        allTrackListView.setupContextMenu();
        
        allTrackListView.populateListItems();
        populateAlbums();
        populateUserCollections();

        Jukebox.getInstance().getMusicDatabase().getTrackMap().addListener(new MapChangeListener<>() {
            @Override
            public void onChanged(MapChangeListener.Change c) {
                allTrackListView.populateListItems();
            }  
        });
        Jukebox.getInstance().getMusicDatabase().getAlbumMap().addListener(new MapChangeListener<>() {
            @Override
            public void onChanged(MapChangeListener.Change c) {
                populateAlbums();
            }
        });
        Jukebox.getInstance().getMusicDatabase().getUserCollectionList().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                populateUserCollections();
            }
        });
    }

    /**
     * Fills albumsFlowPane with album tiles.
     */
    private void populateAlbums() {
        Jukebox jb = Jukebox.getInstance();

        List<VBox> albumVboxes =
        jb.getMusicDatabase()
        .getAlbumMap()
        .values()
        .stream()
        .map(a -> new LibraryCollectionTile(a, this.albumsStackPane))
        .collect(Collectors.toList());
        
        albumsFlowPane.getChildren().clear();
        albumsFlowPane.getChildren().addAll(albumVboxes);
    }

    /**
     * Fills customCollectionsFlowPane with custom collections tiles.
     */
    private void populateUserCollections() {
        Jukebox jb = Jukebox.getInstance();

        List<VBox> userCollectionVboxes = 
        jb.getMusicDatabase()
        .getUserCollectionList()
        .stream()
        .map(c -> new LibraryCollectionTile(c, this.customCollectionsStackPane))
        .collect(Collectors.toList());

        customCollectionsFlowPane.getChildren().clear();
        customCollectionsFlowPane.getChildren().addAll(userCollectionVboxes);
    }
}
