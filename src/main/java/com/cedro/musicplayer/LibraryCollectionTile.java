package com.cedro.musicplayer;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class LibraryCollectionTile extends VBox {

    private MusicCollection collection;
    private StackPane parentStackPane;
    private ImageView coverImageView;
    private Label collectionNameLabel;

    public LibraryCollectionTile(MusicCollection collection, StackPane parentStackPane) {
        this.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        hbox.setPrefWidth(200);
        hbox.setPrefHeight(200);
        hbox.setAlignment(Pos.CENTER);

        this.coverImageView = new ImageView(collection.getCoverImage());
        this.coverImageView.setFitWidth(200);
        this.coverImageView.setFitHeight(200);
        this.coverImageView.setPreserveRatio(true);
        
        hbox.getChildren().add(this.coverImageView);

        this.collectionNameLabel = new Label(collection.getName());

        this.getChildren().addAll(hbox, this.collectionNameLabel);
        this.setOnContextMenuRequested(event -> {
            try {
                ContextMenu cm = new LibraryCollectionTileContextMenu(this);
                cm.show(this, event.getScreenX(), event.getScreenY());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
                collectionTrackList.setupContextMenu();
                
                vb.getChildren().addAll(backButton, collectionTrackList);
                this.parentStackPane.getChildren().add(vb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MusicCollection getCollection() {
        return this.collection;
    }

    public void reloadView() {
        this.collectionNameLabel.setText(this.collection.getName());
        this.coverImageView.setImage(this.collection.getCoverImage());
    }
}
