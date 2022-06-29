package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing the ListView control for the "All tracks" page in the library
 */
public class AllTracksTrackListView extends TrackListView {
    /**
     * Constructor
     */
    public AllTracksTrackListView() throws IOException {
        super();
    }

    @Override
    public List<MusicTrack> getTracks() {
        return Jukebox.getInstance().getMusicDatabase().getTrackMap().values().stream().collect(Collectors.toList());
    }
}
