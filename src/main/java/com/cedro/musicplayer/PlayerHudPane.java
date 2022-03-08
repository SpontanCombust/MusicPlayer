package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

public class PlayerHudPane extends VBox {
    @FXML
    private Label musicTitleText;

    private MediaPlayer commonJukebox = null;

    public PlayerHudPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("player-hud-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    void setJukebox(MediaPlayer jukebox) {
        this.commonJukebox = jukebox;
    }

    @FXML
    protected void onPreviousTrackClick() {

    }

    @FXML
    protected void onNextTrackClick() {

    }

    @FXML
    protected void onPlayPauseClick() {
        
    }
}