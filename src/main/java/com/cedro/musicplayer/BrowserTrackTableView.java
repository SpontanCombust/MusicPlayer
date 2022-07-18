package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BrowserTrackTableView extends TrackTableView {

    private MusicTrackFilter filter = null;

    public BrowserTrackTableView() throws IOException {
        super();
    }

    public void setFilter(MusicTrackFilter filter) {
        this.filter = filter;
    }

    @Override
    public List<MusicTrack> fetchTracks() {
        var tracks = Jukebox.getInstance()
        .getMusicDatabase()
        .getTrackMap()
        .values().stream()
        .collect(Collectors.toList());

        if (filter != null) {
            tracks = filter.filter(tracks);
        }

        return tracks;
    }
    
}
