package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BrowserTrackTableView extends TrackTableView {

    public BrowserTrackTableView() throws IOException {
        super();
    }

    @Override
    public List<MusicTrack> fetchTracks() {
        return Jukebox.getInstance()
        .getMusicDatabase()
        .getTrackMap()
        .values().stream()
        .collect(Collectors.toList());
    }
    
}
