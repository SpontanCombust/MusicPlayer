package com.cedro.musicplayer;

import java.io.IOException;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class TrackList extends VBox {
    @FXML
    private VBox rootPane;
    @FXML
    private ListView<String> musicListView;



    public TrackList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-list-view.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();  
    }

    @FXML
    public void initialize() {
        // when the track is changed automatically or with prev/next buttons
        Jukebox.getInstance().currentTrackIndex.addListener(
            (observable, oldVal, newVal) -> {
                onCurrentTrackIndexChanged(newVal.intValue());
            }
        );

        refreshTrackListView();
    }

    private void refreshTrackListView() {
        this.musicListView.getItems().clear();
        this.musicListView
        .getItems()
        .addAll(
            Jukebox.getInstance()
            .getPlaylist()
            .stream()
            .map(t -> t.getName())
            .collect(Collectors.toList()));
    }

    @FXML
    protected void onLoadMusicButtonClick() {
        Jukebox jb = Jukebox.getInstance();

        jb.getMusicDatabase().requestLoadFromFileSystem(this.getScene().getWindow());

        //FIXME until adding to a playlist is available this is the default
        jb.getPlaylist().addAll(
            jb.getMusicDatabase()
            .getTrackMap().values().stream()
            .collect(Collectors.toList()));
            
        refreshTrackListView();
    }

    @FXML
    protected void onSaveMusicDatabaseButtonClick() {
        Jukebox.getInstance().getMusicDatabase().requestSaveToDatabaseFile(this.getScene().getWindow());
    }

    @FXML
    protected void onLoadMusicDatabaseButtonClick() {
        Jukebox jb = Jukebox.getInstance();

        jb.getMusicDatabase().requestLoadFromDatabaseFile(this.getScene().getWindow());

        //FIXME until adding to a playlist is available this is the default
        jb.getPlaylist().addAll(
            jb.getMusicDatabase()
            .getTrackMap().values().stream()
            .collect(Collectors.toList()));

        refreshTrackListView();
    }

    @FXML
    protected void onTrackSelected(MouseEvent e) {
        if(e.getClickCount() >= 2) {
            int trackIdx = this.musicListView.getSelectionModel().getSelectedIndex();
            Jukebox.getInstance().selectTrack(trackIdx);
            Jukebox.getInstance().play();
        }
    }


    private void onCurrentTrackIndexChanged(int newIndex) {
        this.musicListView.getSelectionModel().select(newIndex);
    }
}
