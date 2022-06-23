package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("library-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @FXML
    public void initialize() {
        // populateAllTracks();
        populateAlbums();
        populateUserCollections();
    }

    // void populateAllTracks() {
        
    // }

    void populateAlbums() {
        Jukebox jb = Jukebox.getInstance();

        List<VBox> albumVboxes =
        jb.getMusicDatabase()
        .getAlbumMap()
        .values()
        .stream()
        .map(a -> makeCollectionVbox(a))
        .collect(Collectors.toList());
        
        albumsFlowPane.getChildren().clear();
        albumsFlowPane.getChildren().addAll(albumVboxes);
    }

    VBox makeCollectionVbox(MusicCollection collection) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        ImageView iv = new ImageView(collection.getCoverImage());
        iv.setFitWidth(200);
        iv.setPreserveRatio(true);
        
        Label l = new Label(collection.getName());
        
        vbox.getChildren().addAll(iv, l);

        return vbox;
    }

    void populateUserCollections() {
        Jukebox jb = Jukebox.getInstance();

        List<VBox> userCollectionVboxes = 
        jb.getMusicDatabase()
        .getUserCollectionList()
        .stream()
        .map(c -> makeCollectionVbox(c))
        .collect(Collectors.toList());

        customCollectionsFlowPane.getChildren().clear();
        customCollectionsFlowPane.getChildren().addAll(userCollectionVboxes);
    }
}
