package com.cedro.musicplayer;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PlayerHud extends VBox {
    @FXML
    private ImageView albumCoverImageView;
    @FXML
    private Label musicTitleText;
    @FXML
    private Label musicTimeText;
    @FXML
    private Slider musicTimelineSlider;
    @FXML
    private Button playPauseButton;
    @FXML
    private Slider volumeSlider;


    private boolean isDraggingMusicTimeline;
    private boolean shouldResumeOnMusicTimelineDragFinished;


    public PlayerHud() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("player-hud-view.fxml"), ResourceBundle.getBundle("com.cedro.musicplayer.strings"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load(); 
    }

    @FXML
    public void initialize() {
        reconfigure();
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
        } else {
            Jukebox.getInstance().play();
        }
    }

    @FXML
    protected void onMusicSliderCursorDragged() {
        this.isDraggingMusicTimeline = true;

        if(Jukebox.getInstance().isPlaying()) {
            this.shouldResumeOnMusicTimelineDragFinished = true;
            Jukebox.getInstance().pause();
        }

        Jukebox.getInstance().seek(new Duration(this.musicTimelineSlider.getValue()));
    }

    @FXML
    protected void onMusicSliderMousePressed() {
        Jukebox.getInstance().seek(new Duration(this.musicTimelineSlider.getValue()));
    }

    @FXML
    protected void onMusicSliderMouseReleased() {
        if(this.shouldResumeOnMusicTimelineDragFinished) {
            Jukebox.getInstance().play();
        }
        this.isDraggingMusicTimeline = false;
        this.shouldResumeOnMusicTimelineDragFinished = false;
    }




    public void reconfigure() {
        this.isDraggingMusicTimeline = false;
        this.shouldResumeOnMusicTimelineDragFinished = false;

        reconfigureCoverImage();
        reconfigureTitleText();
        reconfigureTimeText();
        reconfigureTimelineSlider();
        reconfigurePlayPauseButton();
        reconfigureVolumeSlider();
    }

    protected void reconfigureCoverImage() {
        this.albumCoverImageView.imageProperty().bind(Jukebox.getInstance().currentTrackCoverImage);
    }

    protected void reconfigureTitleText() {
        this.musicTitleText.textProperty().bind(Jukebox.getInstance().currentTrackName);
    }

    protected void reconfigureTimeText() {
        setTimeText(new Duration(0.0));

        Jukebox.getInstance().currentTrackTime.addListener(
            (observable, oldVal, newVal) -> {
                setTimeText(newVal);
            }
        );
    }

    private void setTimeText(Duration time) {
        this.musicTimeText.setText(
            this.formatTimeFromSeconds((int)time.toSeconds()) // elapsed
            + "/" + 
            this.formatTimeFromSeconds(Math.max((int)Jukebox.getInstance().getCurrentTrackDuration().toSeconds(), 1)) // total duration
        );  
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

    protected void reconfigureTimelineSlider() {
        this.musicTimelineSlider.setMin(0.f);
        this.musicTimelineSlider.setMax(Math.max(Jukebox.getInstance().getCurrentTrackDuration().toMillis(), 1));
        this.musicTimelineSlider.setValue(Jukebox.getInstance().getCurrentTrackTime().toMillis());

        Jukebox.getInstance().currentTrack.addListener(
            (observable, oldVal, newVal) -> {
                this.musicTimelineSlider.setMax(Math.max(newVal.getDuration().toMillis(), 1));
            }
        );

        Jukebox.getInstance().currentTrackTime.addListener(
            (observable, oldVal, newVal) -> {
                this.musicTimelineSlider.setValue(newVal.toMillis());
            }
        );
    }

    protected void reconfigurePlayPauseButton() {
        this.playPauseButton.setText(getPlayPauseButtonText(Jukebox.getInstance().currentTrackStatus.get() == MediaPlayer.Status.PLAYING));

        Jukebox.getInstance().currentTrackStatus.addListener(
            (observable, oldVal, newVal) -> {
                if(!isDraggingMusicTimeline) {
                    this.playPauseButton.setText(getPlayPauseButtonText(newVal == MediaPlayer.Status.PLAYING));
                }
            }
        );
    }
    
    private String getPlayPauseButtonText(boolean isPlaying) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.cedro.musicplayer.strings");
        if(isPlaying) {
            return bundle.getString("player_view_button_pause_track");
        } else {
            return bundle.getString("player_view_button_play_track");
        }
    }

    protected void reconfigureVolumeSlider() {
        this.volumeSlider.setMin(0.0);
        this.volumeSlider.setMax(1.0);
        this.volumeSlider.setValue(Jukebox.getInstance().currentTrackVolume.get());

        this.volumeSlider.valueProperty().addListener(
            (observable, oldVal, newVal) -> Jukebox.getInstance().setVolume(newVal.floatValue())
        );
    }
}
