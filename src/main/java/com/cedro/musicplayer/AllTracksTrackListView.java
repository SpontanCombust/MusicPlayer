package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class AllTracksTrackListView extends TrackListView {

    public AllTracksTrackListView() throws IOException {
        super();

        this.tracks = Jukebox.getInstance().getMusicDatabase().getTrackMap().values().stream().collect(Collectors.toList());
        populateListItems();

        this.listView.setContextMenu(getContextMenu());
    }

    private ContextMenu getContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem menuItemAddToPlaylist = new MenuItem();
        menuItemAddToPlaylist.setOnAction(e -> {
            List<MusicTrack> selectedTracks = getSelectedTracks();
            Jukebox.getInstance().getPlaylist().addAll(selectedTracks);
        });
        menuItemAddToPlaylist.setText("Add to playlist");

        menu.getItems().add(menuItemAddToPlaylist);

        return menu;
    }
}
