package com.cedro.musicplayer;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RootController extends AnchorPane {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private ToggleButton buttonOpenPlayer;
    
    @FXML
    private ToggleButton buttonOpenLibrary;


    public RootController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("root-view.fxml"), ResourceBundle.getBundle("com.cedro.musicplayer.strings"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @FXML
    public void initialize() throws IOException {
        openPlayer();
        buttonOpenPlayer.setSelected(true);
        buttonOpenLibrary.setSelected(false);
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
        alert.setHeaderText("Music Player v" + MusicPlayerApplication.VERSION);
        alert.setContentText(
            "Simple music player application.\n\n" +
            "Copyright (c) 2022 Przemysław Cedro\n"
        );

        alert.show();
    }



    @FXML
    protected void onButtonOpenPlayer(ActionEvent event) throws IOException {
        this.openPlayer();
        buttonOpenPlayer.setSelected(true);
        buttonOpenLibrary.setSelected(false);
    }

    @FXML
    protected void onButtonOpenLibrary(ActionEvent event) {
        this.openLibrary();
        buttonOpenLibrary.setSelected(true);
        buttonOpenPlayer.setSelected(false);
    }



    private Node getContentNode() {
        Node content = null; 
        if(contentPane.getChildren().size() > 0) {
            content = contentPane.getChildren().get(0);
        }

        return content;
    }

    private boolean openLibrary() {
        Node content = getContentNode();
        if(content == null || content instanceof PlayerHudAndTrackList) {
            try {
                var library = new LibraryModelController();
                contentPane.getChildren().clear();
                contentPane.getChildren().add(library);
                contentPane.setTopAnchor(library, 0.0);
                contentPane.setBottomAnchor(library, 0.0);
                contentPane.setLeftAnchor(library, 0.0);
                contentPane.setRightAnchor(library, 0.0);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean openPlayer() {
        Node content = getContentNode();
        if(content == null || content instanceof LibraryModelController) {
            try {
                var player = new PlayerHudAndTrackList();
                contentPane.getChildren().clear();
                contentPane.getChildren().add(player);
                contentPane.setTopAnchor(player, 0.0);
                contentPane.setBottomAnchor(player, 0.0);
                contentPane.setLeftAnchor(player, 0.0);
                contentPane.setRightAnchor(player, 0.0);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
}
