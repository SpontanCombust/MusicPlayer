package com.cedro.musicplayer;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

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
        var directoryChooser = new DirectoryChooser();
        
        File selectedDirectory = directoryChooser.showDialog(rootPane.getScene().getWindow());
        if(selectedDirectory != null) {
            Jukebox jb = Jukebox.getInstance();
            var albums = MusicAlbum.fromDirectoryRecurse(selectedDirectory.toPath());
            jb.getMusicDatabase().clearAlbums();
            jb.getMusicDatabase().addAlbums(albums);

            // by default for now will load all music from albums into the playlist
            jb.getPlaylist()
            .addAll(
                albums.stream()
                .flatMap(a -> a.getTracks().stream())
                .collect(Collectors.toList()));
            
            refreshTrackListView();
        }
    }

    @FXML
    protected void onSaveMusicDatabaseButtonClick() {
        var fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Music Database", MusicDatabase.DB_FILE_EXTENSION));
        fileChooser.setInitialFileName("music" + MusicDatabase.DB_FILE_EXTENSION);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        if(selectedFile != null) {
            try {
                Jukebox.getInstance().getMusicDatabase().saveToFile(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onLoadMusicDatabaseButtonClick() {
        var fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Music Database", MusicDatabase.DB_FILE_EXTENSION));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if(selectedFile != null) {
            String err = Jukebox.getInstance().getMusicDatabase().loadFromFile(selectedFile.toPath());
            if(err != null) {
                System.out.println(err);
            }
            refreshTrackListView();
        }
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
