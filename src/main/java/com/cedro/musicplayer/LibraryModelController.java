package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LibraryModelController extends AnchorPane {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ListView<String> allTrackListView;

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
        // albumsTilePane.setPrefTileWidth(albumsTilePane.getWidth() / albumsTilePane.getPrefColumns());
        // albumsTilePane.setPrefTileHeight(albumsTilePane.getHeight() / albumsTilePane.getPrefRows());
        // customCollectionsTilePane.setPrefTileWidth(customCollectionsTilePane.getWidth() / customCollectionsTilePane.getPrefColumns());
        // customCollectionsTilePane.setPrefTileHeight(customCollectionsTilePane.getHeight() / customCollectionsTilePane.getPrefRows());

        populateAllTracks();
        populateAlbums();
        populateCustomCollections();
    }

    void populateAllTracks() {
        Jukebox jb = Jukebox.getInstance();

        allTrackListView.getItems().clear();
        allTrackListView.getItems().addAll(jb.getPlaylist().stream().map(t -> t.getName()).collect(Collectors.toList()));
    }

    void populateAlbums() {
        Jukebox jb = Jukebox.getInstance();

        List<VBox> albumVboxes = jb.getAlbums().stream().map(a -> makeAlbumVbox(a)).collect(Collectors.toList());
        albumsFlowPane.getChildren().clear();
        albumsFlowPane.getChildren().addAll(albumVboxes);
    }

    VBox makeAlbumVbox(MusicAlbum album) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        ImageView iv = new ImageView(album.getCoverImage());
        iv.setFitWidth(200);
        iv.setPreserveRatio(true);
        
        Label l = new Label(album.getName());
        
        vbox.getChildren().addAll(iv, l);

        return vbox;
    }

    void populateCustomCollections() {
        Jukebox jb = Jukebox.getInstance();

        List<VBox> userCollectionVboxes = jb.getUserCollections().stream().map(c -> makeUserCollectionVbox(c)).collect(Collectors.toList());
        customCollectionsFlowPane.getChildren().clear();
        customCollectionsFlowPane.getChildren().addAll(userCollectionVboxes);
    }

    VBox makeUserCollectionVbox(MusicCollection collection) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        Label l = new Label(collection.getName());

        vbox.getChildren().addAll(l);

        return vbox;
    }

}
