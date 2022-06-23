package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AllTracksTrackListView extends LibraryTrackListView {
    public AllTracksTrackListView() throws IOException {
        super();
    }

    @Override
    public List<MusicTrack> getTracks() {
        return Jukebox.getInstance().getMusicDatabase().getTrackMap().values().stream().collect(Collectors.toList());
    }
}
