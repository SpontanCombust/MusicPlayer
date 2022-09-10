package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TrackInfoDialog extends Stage {
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelArtist;
    @FXML
    private Label labelAlbum;
    @FXML
    private Label labelYear;
    @FXML
    private Label labelGenre;

    public TrackInfoDialog(MusicTrack track) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-info-dialog.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        labelTitle.setText(track.getTitle());
        labelArtist.setText(track.getArtist());
        labelAlbum.setText(track.getAlbum());
        labelYear.setText(track.getYear());
        labelGenre.setText(track.getGenre());
    }
}
