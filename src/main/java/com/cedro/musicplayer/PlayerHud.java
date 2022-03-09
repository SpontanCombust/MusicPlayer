package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class PlayerHud extends VBox {
    @FXML
    private Label musicTitleText;
    @FXML
    private Slider musicTimelineSlider;

    public PlayerHud() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("player-hud-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
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
