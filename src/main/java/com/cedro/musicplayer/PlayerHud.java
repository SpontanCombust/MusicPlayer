package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Class representing the main player view with the track timeline, play/pause button etc.
 */
public class PlayerHud extends VBox {
    private final double KEY_PRESS_TIME_SKIP_MILLIS = 5000;
    private final float KEY_PRESS_VOLUME_CHANGE = 0.05f;

    /**
     * ImageView for the album cover image
     */
    @FXML
    private ImageView albumCoverImageView;
    /**
     * Label for the track title
     */
    @FXML
    private Label musicTitleText;
    /**
     * Label for passed track time
     */
    @FXML
    private Label musicTimeText;
    /**
     * Slider representing the time progress of the song
     */
    @FXML
    private Slider musicTimelineSlider;
    /**
     * Button for playing/pausing the song
     */
    @FXML
    private Button playPauseButton;
    /**
     * Slider for setting music volume
     */
    @FXML
    private Slider volumeSlider;
    /**
     * Button for enabling/disabling autoplay
     */
    @FXML
    private ToggleButton autoplayButton;
    /**
     * Button for enabling/disabling looping the playlist
     */
    @FXML
    private ToggleButton loopButton;


    /**
     * Whether the user is currently dragging the timelime slider
     */
    private boolean isDraggingMusicTimeline;
    /**
     * Whether playing should be resumed when user lets go of the timeline slider
     */
    private boolean shouldResumeOnMusicTimelineDragFinished;


    /**
     * Constructor
     *
     * @throws IOException
     */
    public PlayerHud() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("player-hud-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load(); 
    }

    /**
     * Initializes the player hud
     */
    @FXML
    public void initialize() {
        reconfigure();
    }

    @FXML
    protected void onKeyPress(KeyEvent event) {
        switch(event.getCode()) {
            case SPACE:
                if(Jukebox.getInstance().isPlaying()) {
                    Jukebox.getInstance().pause();
                } else {
                    Jukebox.getInstance().play();
                }
                break;
            case LEFT:
                Jukebox.getInstance().seek(new Duration(this.musicTimelineSlider.getValue() - KEY_PRESS_TIME_SKIP_MILLIS));
                break;
            case RIGHT:
                Jukebox.getInstance().seek(new Duration(this.musicTimelineSlider.getValue() + KEY_PRESS_TIME_SKIP_MILLIS));
                break;
            case UP:
                this.volumeSlider.setValue(this.volumeSlider.getValue() + KEY_PRESS_VOLUME_CHANGE);
                break;
            case DOWN:
                this.volumeSlider.setValue(this.volumeSlider.getValue() - KEY_PRESS_VOLUME_CHANGE);
                break;
            default: 
                break;
        }
    }

    /**
     * Event handler for when previous track is requested
     */
    @FXML
    protected void onPreviousTrackClick() {
        Jukebox.getInstance().selectPreviousTrack();
    }

    /**
     * Event handler for when next track is requested
     */
    @FXML
    protected void onNextTrackClick() {
        Jukebox.getInstance().selectNextTrack();
    }

    /**
     * Event handler for when play/pause button is clicked
     */
    @FXML
    protected void onPlayPauseClick() {
        if(Jukebox.getInstance().isPlaying()) {
            Jukebox.getInstance().pause();
        } else {
            Jukebox.getInstance().play();
        }
    }

    /** 
     * Event handler for when the timeline slider is dragged
     */
    @FXML
    protected void onMusicSliderCursorDragged() {
        this.isDraggingMusicTimeline = true;

        if(Jukebox.getInstance().isPlaying()) {
            this.shouldResumeOnMusicTimelineDragFinished = true;
            Jukebox.getInstance().pause();
        }

        Jukebox.getInstance().seek(new Duration(this.musicTimelineSlider.getValue()));
    }

    /**
     * Event handler for when the timeline slider is only clicked in some place
     */
    @FXML
    protected void onMusicSliderMousePressed() {
        Jukebox.getInstance().seek(new Duration(this.musicTimelineSlider.getValue()));
    }

    /**
     * Event handler for when the timeline slider is released
     */
    @FXML
    protected void onMusicSliderMouseReleased() {
        if(this.shouldResumeOnMusicTimelineDragFinished) {
            Jukebox.getInstance().play();
        }
        this.isDraggingMusicTimeline = false;
        this.shouldResumeOnMusicTimelineDragFinished = false;
    }




    /**
     * Reconfigures states of all child controls
     */
    public void reconfigure() {
        this.isDraggingMusicTimeline = false;
        this.shouldResumeOnMusicTimelineDragFinished = false;

        reconfigureCoverImage();
        reconfigureTitleText();
        reconfigureTimeText();
        reconfigureTimelineSlider();
        reconfigurePlayPauseButton();
        reconfigureVolumeSlider();
        reconfigureAutplayButton();
        reconfigureLoopButton();
    }

    /**
     * Reconfigures the cover image. Sets appropriate listeners.
     */
    protected void reconfigureCoverImage() {
        this.albumCoverImageView.imageProperty().bind(Jukebox.getInstance().currentTrackCoverImage);
    }

    /**
     * Reconfigures the title text. Sets appropriate listeners.
     */
    protected void reconfigureTitleText() {
        this.musicTitleText.textProperty().bind(Jukebox.getInstance().currentTrackName);
    }

    /**
     * Reconfigures the time text. Sets appropriate listeners.
     */
    protected void reconfigureTimeText() {
        setTimeText(new Duration(0.0));

        Jukebox.getInstance().currentTrackTime.addListener(
            (observable, oldVal, newVal) -> {
                setTimeText(newVal);
            }
        );
    }

    /**
     * Sets the time text
     *
     * @param time the time to set
     */
    private void setTimeText(Duration time) {
        this.musicTimeText.setText(
            this.formatTimeFromSeconds((int)time.toSeconds()) // elapsed
            + "/" + 
            this.formatTimeFromSeconds(Math.max((int)Jukebox.getInstance().getCurrentTrackDuration().toSeconds(), 1)) // total duration
        );  
    }

    /**
     * Formats the time from seconds to a string
     *
     * @param seconds the time in seconds
     * @return the formatted time string
     */
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

    /**
     * Reconfigures the timeline slider. Sets appropriate listeners.
     */
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

    /**
     * Reconfigures the play/pause button. Sets appropriate listeners.
     */
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
    
    /**
     * Gets the localised play/pause button text
     *
     * @param isPlaying whether the player is playing or not
     * @return the play/pause button text
     */
    private String getPlayPauseButtonText(boolean isPlaying) {
        if(isPlaying) {
            return "| |";
        } else {
            return "\u25b6";
        }
    }

    /**
     * Reconfigures the volume slider. Sets appropriate listeners.
     */
    protected void reconfigureVolumeSlider() {
        this.volumeSlider.setMin(0.0);
        this.volumeSlider.setMax(1.0);
        this.volumeSlider.setValue(Jukebox.getInstance().trackVolume.get());

        this.volumeSlider.valueProperty().addListener(
            (observable, oldVal, newVal) -> Jukebox.getInstance().setVolume(newVal.floatValue())
        );
    }

    /**
     * Reconfigures the autplay button. Sets appropriate listeners.
     */
    protected void reconfigureAutplayButton() {
        this.autoplayButton.setSelected(Jukebox.getInstance().playlistAutoplay.get());
        Jukebox.getInstance().playlistAutoplay.bind(this.autoplayButton.selectedProperty());
    }

    /**
     * Reconfigures the loop button. Sets appropriate listeners.
     */
    protected void reconfigureLoopButton() {
        this.loopButton.setSelected(Jukebox.getInstance().playlistLooping.get());
        Jukebox.getInstance().playlistLooping.bind(this.loopButton.selectedProperty());
    }
}
