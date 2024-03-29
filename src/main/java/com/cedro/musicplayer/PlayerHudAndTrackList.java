package com.cedro.musicplayer;

import java.io.IOException;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Class for a component that holds playlist ListView and player HUD.
 */
public class PlayerHudAndTrackList extends VBox {

    /**
     * ListView for the playlist.
     */
    @FXML
    private PlaylistTrackListView playlistTrackListPane;

    @FXML
    private Label playlistHelpLabel;

    @FXML
    private PlayerHud playerHudPane;

    /**
     * Constructor
     * 
     * @throws IOException
     */
    public PlayerHudAndTrackList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerhud-and-tracklist-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load(); 
    }

    /**
     * Initializes components and sets up listeners for them.
     */
    @FXML
    public void initialize() {
        this.playlistTrackListPane.populateItems();
        this.playlistTrackListPane.setupContextMenu();

        playlistHelpLabel.setVisible(Jukebox.getInstance().getPlaylist().size() == 0);
        
        Jukebox.getInstance().getPlaylist().addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                playlistHelpLabel.setVisible(Jukebox.getInstance().getPlaylist().size() == 0);
                playlistTrackListPane.populateItems();
            }  
        });

        playerHudPane.requestFocus();
    }
}
