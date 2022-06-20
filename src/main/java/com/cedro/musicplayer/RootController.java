package com.cedro.musicplayer;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class RootController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void initialize() throws IOException {
        openPlayer(null);
    }

    @FXML
    void openLibrary(ActionEvent event) throws IOException {
        Node content = null; 
        if(contentPane.getChildren().size() > 0) {
            content = contentPane.getChildren().get(0);
        }

        if(content == null || content instanceof PlayerHudAndTrackList) {
            var library = new LibraryModelController();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(library);
            AnchorPane.setTopAnchor(library, 0.0);
            AnchorPane.setBottomAnchor(library, 0.0);
            AnchorPane.setLeftAnchor(library, 0.0);
            AnchorPane.setRightAnchor(library, 0.0);
        }
    }

    @FXML
    void openPlayer(ActionEvent event) throws IOException {
        Node content = null;
        if(contentPane.getChildren().size() > 0) {
            content = contentPane.getChildren().get(0);
        }

        if(content == null || content instanceof LibraryModelController) {
            var player = new PlayerHudAndTrackList();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(player);
            AnchorPane.setTopAnchor(player, 0.0);
            AnchorPane.setBottomAnchor(player, 0.0);
            AnchorPane.setLeftAnchor(player, 0.0);
            AnchorPane.setRightAnchor(player, 0.0);
        }
    }

}
