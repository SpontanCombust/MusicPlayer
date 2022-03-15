package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class PlayerHud extends VBox {
    @FXML
    private Label musicTitleText;
    @FXML
    private Label musicTimeText;
    @FXML
    private Slider musicTimelineSlider;
    @FXML
    private Button playPauseButton;


    public PlayerHud() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("player-hud-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load(); 
    }

    @FXML
    public void initialize() {
        this.musicTitleText.textProperty().bind(Jukebox.getInstance().currentTrackName);
        this.musicTimeText.setText("00:00/00:00");
        this.musicTimelineSlider.setMin(0.0);
        this.musicTimelineSlider.setValue(0.0);
        this.playPauseButton.setText("Play");

        Jukebox.getInstance().currentTrack.addListener(
            (observable, oldVal, newVal) -> {
                this.musicTimelineSlider.setMax(Math.max(newVal.getDuration().toSeconds(), 1));
            }
        );
        Jukebox.getInstance().currentTrackTime.addListener(
            (observable, oldVal, newVal) -> {
                double durationSeconds = this.musicTimelineSlider.getMax();
                double elapsedSeconds = newVal.toSeconds();

                this.musicTimelineSlider.setValue(elapsedSeconds);
                this.musicTimeText.setText(this.formatTimeFromSeconds((int)elapsedSeconds) + "/" + this.formatTimeFromSeconds((int)durationSeconds));
            }  
        );
    }

    @FXML
    protected void onPreviousTrackClick() {
        Jukebox.getInstance().selectPreviousTrack();
    }

    @FXML
    protected void onNextTrackClick() {
        Jukebox.getInstance().selectNextTrack();
    }

    @FXML
    protected void onPlayPauseClick() {
        if(Jukebox.getInstance().isPlaying()) {
            Jukebox.getInstance().pause();
            this.playPauseButton.setText("Play");
        } else {
            Jukebox.getInstance().play();
            this.playPauseButton.setText("Pause");
        }
    }


    private String formatTimeFromSeconds(int seconds) {
        int minutes = seconds / 60;
        seconds %= 60;

        String minutesStr = Integer.toString(minutes);
        if(minutes < 10) {
            minutesStr = "0" + minutesStr;
        }

        String secondsStr = Integer.toString(seconds);
        if(seconds < 10) {
            secondsStr = "0" + secondsStr;
        }

        return minutesStr + ":" + secondsStr;
    }
}
