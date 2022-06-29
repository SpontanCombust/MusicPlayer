package com.cedro.musicplayer;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller class for the root view
 */
public class RootController extends AnchorPane {

    /**
     * Pane that can hold either playlist with player hud or the library view
     */
    @FXML
    private AnchorPane contentPane;

    /**
     * Button that opens the player with playlist view
     */
    @FXML
    private ToggleButton buttonOpenPlayer;
    
    /**
     * Button that opens the library view
     */
    @FXML
    private ToggleButton buttonOpenLibrary;


    /**
     * Constructor
     * @throws IOException
     */
    public RootController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("root-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    /**
     * Initialize the controller. Opens the player hud view.
     *
     * @throws IOException
     */
    @FXML
    public void initialize() throws IOException {
        openPlayer();
        buttonOpenPlayer.setSelected(true);
        buttonOpenLibrary.setSelected(false);
    }

    /**
     * Handler for the menu item that loads music from directory
     * @param event - choice event
     * @throws IOException
     */
    @FXML
    protected void onMenuLoadMusicFromFileSystem(ActionEvent event) throws IOException {
        Jukebox.getInstance().getMusicDatabase().requestLoadFromFileSystem(this.contentPane.getScene().getWindow());
    }

    /**
     * Handler for the menu item that loads music from a configuration file
     * @param event - choice event
     * @throws IOException
     */
    @FXML
    protected void onMenuLoadMusicFromConfiguration(ActionEvent event) throws IOException {
        Jukebox.getInstance().getMusicDatabase().requestLoadFromConfigurationFile(this.contentPane.getScene().getWindow());
    }

    /**
     * Handler for the menu item that saves music configuration
     * @param event - choice event
     * @throws IOException
     */
    @FXML
    protected void onMenuSaveMusicToConfiguration(ActionEvent event) throws IOException {
        Jukebox.getInstance().getMusicDatabase().requestSaveToConfigurationFile(this.contentPane.getScene().getWindow());
    }

    /**
     * Handler for the menu item that exits the application
     * @param event - choice event
     * @throws IOException
     */
    @FXML
    protected void onMenuExit(ActionEvent event) throws IOException {
        Stage stage = (Stage)contentPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Handler for the menu item that displays About information
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onMenuAbout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Music Player v" + MusicPlayerApplication.VERSION);
        alert.setContentText(
            Localization.getString("root_view_menu_help_about_text") + "\n\n" +
            "Copyright (c) 2022 Przemysław Cedro\n"
        );

        alert.show();
    }



    /**
     * Handler for the button that opens the player with playlist view
     * @param event - action event
     * @throws IOException
     */
    @FXML
    protected void onButtonOpenPlayer(ActionEvent event) throws IOException {
        this.openPlayer();
        buttonOpenPlayer.setSelected(true);
        buttonOpenLibrary.setSelected(false);
    }

    /**
     * Handler for the button that opens the library view
     * @param event - action event
     * @throws IOException
     */
    @FXML
    protected void onButtonOpenLibrary(ActionEvent event) {
        this.openLibrary();
        buttonOpenLibrary.setSelected(true);
        buttonOpenPlayer.setSelected(false);
    }



    /**
     * Gets the node that is stored in the content pane or null if there is no node
     */
    private Node getContentNode() {
        Node content = null; 
        if(contentPane.getChildren().size() > 0) {
            content = contentPane.getChildren().get(0);
        }

        return content;
    }

    /**
     * Opens the library view
     */
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

    /**
     * Opens the player with playlist view
     */
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
