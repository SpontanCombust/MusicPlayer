package com.cedro.musicplayer;

import java.io.IOException;

import javafx.scene.control.ContextMenu;

public abstract class LibraryTrackListView extends TrackListView {

    public LibraryTrackListView() throws IOException {
        super();
    }

    @Override
    public void initialize() {
        super.initialize();

        try {
            ContextMenu cm = new LibraryTrackListContextMenu(this);
            this.listView.setContextMenu(cm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
