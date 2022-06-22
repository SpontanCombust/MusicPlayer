package com.cedro.musicplayer;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RootController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void initialize() throws IOException {
        openPlayer();
    }

    @FXML
    protected void onMenuLoadMusicFromFileSystem(ActionEvent event) throws IOException {
        Jukebox.getInstance().getMusicDatabase().requestLoadFromFileSystem(this.contentPane.getScene().getWindow());
    }

    @FXML
    protected void onMenuLoadMusicFromDatabase(ActionEvent event) throws IOException {
        Jukebox.getInstance().getMusicDatabase().requestLoadFromDatabaseFile(this.contentPane.getScene().getWindow());
    }

    @FXML
    protected void onMenuSaveMusicToDatabase(ActionEvent event) throws IOException {
        Jukebox.getInstance().getMusicDatabase().requestSaveToDatabaseFile(this.contentPane.getScene().getWindow());
    }

    @FXML
    protected void onMenuExit(ActionEvent event) throws IOException {
        Stage stage = (Stage)contentPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    protected void onMenuAbout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Music Player v1.0");
        alert.setContentText(
            "Simple music player application.\n\n" +
            "Copyright (c) 2022 Przemysław Cedro\n"
        );

        alert.show();
    }



    @FXML
    protected void onButtonOpenLibrary(ActionEvent event) {
        openLibrary();
    }

    @FXML
    protected void onButtonOpenPlayer(ActionEvent event) throws IOException {
        openPlayer();
    }



    private Node getContentNode() {
        Node content = null; 
        if(contentPane.getChildren().size() > 0) {
            content = contentPane.getChildren().get(0);
        }

        return content;
    }

    private void openLibrary() {
        Node content = getContentNode();
        if(content == null || content instanceof PlayerHudAndTrackList) {
            try {
                var library = new LibraryModelController();
                contentPane.getChildren().clear();
                contentPane.getChildren().add(library);
                AnchorPane.setTopAnchor(library, 0.0);
                AnchorPane.setBottomAnchor(library, 0.0);
                AnchorPane.setLeftAnchor(library, 0.0);
                AnchorPane.setRightAnchor(library, 0.0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openPlayer() {
        Node content = getContentNode();
        if(content == null || content instanceof LibraryModelController) {
            try {
                var player = new PlayerHudAndTrackList();
                contentPane.getChildren().clear();
                contentPane.getChildren().add(player);
                AnchorPane.setTopAnchor(player, 0.0);
                AnchorPane.setBottomAnchor(player, 0.0);
                AnchorPane.setLeftAnchor(player, 0.0);
                AnchorPane.setRightAnchor(player, 0.0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
