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

        this.musicTitleText.textProperty().bind(Jukebox.getInstance().currentTrackName);

        this.musicTimelineSlider.setMin(0.0);
        this.musicTimelineSlider.setValue(0.0);
        Jukebox.getInstance().currentTrack.addListener(
            (observable, oldVal, newVal) -> {
                this.musicTimelineSlider.setMax(newVal.getDuration().toSeconds());
            }
        );
        Jukebox.getInstance().currentTrackTime.addListener(
            (observable, oldVal, newVal) -> {
                this.musicTimelineSlider.setValue(newVal.toSeconds());
            }  
        );
    }

    
    @FXML
    protected void onPreviousTrackClick() {
        Jukebox.getInstance().previous();
    }

    @FXML
    protected void onNextTrackClick() {
        Jukebox.getInstance().next();
    }

    @FXML
    protected void onPlayPauseClick() {
        if(Jukebox.getInstance().isPlaying()) {
            Jukebox.getInstance().pause();
        } else {
            Jukebox.getInstance().play();
        }
    }
}
