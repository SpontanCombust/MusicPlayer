package com.cedro.musicplayer;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * Class representing a collection tile with a cover and a name displayed in "Albums" and "User collections" sections
 */
public class LibraryCollectionTile extends VBox {

    /**
     * Collection this tile refers to
     */
    private MusicCollection collection;
    /**
     * Stack pane in which the tile resides
     */
    private StackPane parentStackPane;
    /**
     * Cover image of the collection
     */
    private ImageView coverImageView;
    /**
     * Name label of the colletion
     */
    private Label collectionNameLabel;


    private ContextMenu contextMenu = null;

    /**
     * Constructor
     * 
     * @param collection - collection this tile should refer to
     * @param parentStackPane - stack pane in which the tile resides
     */
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

        try {
            contextMenu = new LibraryCollectionTileContextMenu(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getChildren().addAll(hbox, this.collectionNameLabel);
        this.setOnContextMenuRequested(event -> {
            if(!contextMenu.isShowing()) {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            }

            event.consume();
        });

        this.collection = collection;
        this.parentStackPane = parentStackPane;
        this.setOnMouseClicked(e -> this.onMouseClicked(e));
    }

    /**
     * Event handler on when mouse is clicked on the tile
     * 
     * @param mouseEvent - mouse event
     */
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

                CollectionTrackTableView collectionTableView = new CollectionTrackTableView(this.collection);
                collectionTableView.setPrefWidth(this.parentStackPane.getPrefWidth());
                collectionTableView.setupContextMenu();
                collectionTableView.populateItems();

                vb.getChildren().addAll(backButton, collectionTableView);
                VBox.setVgrow(collectionTableView, Priority.ALWAYS);

                this.parentStackPane.getChildren().add(vb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns collection this tile refers to
     * 
     * @return MusicCollection - referenced colletion 
     */
    public MusicCollection getCollection() {
        return this.collection;
    }

    /**
     * Refresh the label and cover image
     */
    public void reloadView() {
        this.collectionNameLabel.setText(this.collection.getName());
        this.coverImageView.setImage(this.collection.getCoverImage());
    }
}
