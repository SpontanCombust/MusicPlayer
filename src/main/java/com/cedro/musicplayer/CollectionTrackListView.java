package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;

public class CollectionTrackListView extends LibraryTrackListView {
    private MusicCollection collection;

    public CollectionTrackListView(MusicCollection collection) throws IOException {
        super();
        this.collection = collection;
    }

    @Override
    public List<MusicTrack> getTracks() {
        return collection.getTracks();
    }
}
