package com.cedro.musicplayer;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class LibraryCollectionTile extends VBox {

    private MusicCollection collection;
    private StackPane parentStackPane;

    public LibraryCollectionTile(MusicCollection collection, StackPane parentStackPane) {
        this.setAlignment(Pos.CENTER);

        ImageView iv = new ImageView(collection.getCoverImage());
        iv.setFitWidth(200);
        iv.setPreserveRatio(true);
        
        Label l = new Label(collection.getName());

        this.getChildren().addAll(iv, l);

        this.collection = collection;
        this.parentStackPane = parentStackPane;
        this.setOnMouseClicked(e -> this.onMouseClicked(e));
    }

    void onMouseClicked(MouseEvent mouseEvent) {       
        if (mouseEvent.getClickCount() >= 2) {
            try {
                VBox vb = new VBox();
                vb.setAlignment(Pos.TOP_LEFT);
                vb.setStyle("-fx-background-color: #ffffff"); // a hack that covers what's under the vbox
    
                Button backButton = new Button("←");
                backButton.setPrefWidth(75);
                backButton.setTextAlignment(TextAlignment.CENTER);
                backButton.setAlignment(Pos.CENTER);
                backButton.setOnAction(e -> {
                    this.parentStackPane.getChildren().remove(vb);
                });

                CollectionTrackListView collectionTrackList = new CollectionTrackListView(this.collection);
                collectionTrackList.populateListItems();
                
                vb.getChildren().addAll(backButton, collectionTrackList);
                this.parentStackPane.getChildren().add(vb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
