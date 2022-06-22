package com.cedro.musicplayer;

import java.io.IOException;

import javafx.scene.input.MouseEvent;

public class PlaylistTrackListView extends TrackListView {

    public PlaylistTrackListView() throws IOException {
        super();

        this.tracks = Jukebox.getInstance().getPlaylist();
        this.populateListItems();
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
    }

    private void onCurrentTrackIndexChanged(int newIndex) {
        this.listView.getSelectionModel().select(newIndex);
    }

    @Override
    protected void onListItemSelected(MouseEvent e) {
        if(e.getClickCount() >= 2) {
            int trackIdx = this.listView.getSelectionModel().getSelectedIndex();
            Jukebox.getInstance().selectTrack(trackIdx);
            Jukebox.getInstance().play();
        }
    }
}
