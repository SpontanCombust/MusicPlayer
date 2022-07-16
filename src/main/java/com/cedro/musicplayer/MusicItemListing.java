package com.cedro.musicplayer;

import java.util.List;

import javafx.scene.input.MouseEvent;

public interface MusicItemListing {
    public List<MusicTrack> fetchTracks();
    
    public void setupContextMenu();
    public void populateItems();
    
    public List<MusicTrack> getSelectedTracks();

    public void onItemSelected(MouseEvent e);
}
