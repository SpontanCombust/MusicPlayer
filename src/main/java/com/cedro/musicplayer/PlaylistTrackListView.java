package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;

/**
 * ListView class for the paylist
 */
public class PlaylistTrackListView extends TrackListView {

    /**
     * Constructor
     * @throws IOException
     */
    public PlaylistTrackListView() throws IOException {
        super();
    }
    
    @Override
    public void initialize() {
        super.initialize();

        // when the track is changed automatically or with prev/next buttons
        Jukebox.getInstance().currentTrackIndex.addListener(
            (observable, oldVal, newVal) -> {
                onCurrentTrackIndexChanged(newVal.intValue());
            }
        );

        this.listView.setCellFactory(param -> new ListCell<String>() {
            {
                Jukebox.getInstance().currentTrackIndex.addListener((obs, ov, nv) -> {
                    if(getIndex() == nv.intValue() && Jukebox.getInstance().currentTrack != null) {
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-font-weight: normal;");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
        
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);

                    if(getIndex() == Jukebox.getInstance().currentTrackIndex.intValue() && Jukebox.getInstance().currentTrack != null) {
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-font-weight: normal;");
                    }
                }
            }
        });
    }

    @Override
    public List<MusicTrack> fetchTracks() {
        return Jukebox.getInstance().getPlaylist();
    }

    /**
     * Handler on when the current track index changes
     * @param newIndex - new track index in the playlist
     */
    private void onCurrentTrackIndexChanged(int newIndex) {
        this.listView.getSelectionModel().select(newIndex);
    }

    @Override
    public void onItemSelected(MouseEvent e) {
        if(e.getClickCount() >= 2) {
            int trackIdx = this.listView.getSelectionModel().getSelectedIndex();
            Jukebox.getInstance().selectTrack(trackIdx);
            Jukebox.getInstance().play();
        }
    }
}
