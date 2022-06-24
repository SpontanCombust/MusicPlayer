package com.cedro.musicplayer;

import java.io.IOException;

import javafx.scene.control.ContextMenu;

public class LibraryTrackListView extends TrackListView {

    public LibraryTrackListView() throws IOException {
        super();
    }

    @Override
    public void initialize() {
        super.initialize();

        try {
            ContextMenu cm = new LibraryTrackListContextMenu(() -> this.getSelectedTracks());
            this.listView.setContextMenu(cm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
