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

public class LibraryModelController extends AnchorPane {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private LibraryTrackListView allTrackListView;

    @FXML
    private StackPane albumsStackPane;

    @FXML
    private FlowPane albumsFlowPane;

    @FXML
    private StackPane customCollectionsStackPane;

    @FXML
    private FlowPane customCollectionsFlowPane;



    public LibraryModelController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("library-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @FXML
    public void initialize() {
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

    void populateAlbums() {
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

    void populateUserCollections() {
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
