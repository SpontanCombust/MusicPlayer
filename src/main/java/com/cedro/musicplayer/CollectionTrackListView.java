package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;

/**
 * Class representing the ListView control displayed when opening albums and user collections.
 */
public class CollectionTrackListView extends TrackListView {
    /**
     * Music collection, which tracks are to be displayed in the track list view
     */
    private MusicCollection collection;

    /**
     * Constructor
     * 
     * @param collection - collection which tracks will be displayed in the track list view
     * @throws IOException
     */
    public CollectionTrackListView(MusicCollection collection) throws IOException {
        super();
        this.collection = collection;
    }

    @Override
    public List<MusicTrack> getTracks() {
        return collection.getTracks();
    }

    
    /** 
     * Get the collection which tracks are displayed in the track list view
     * 
     * @return MusicCollection - music collection
     */
    public MusicCollection getCollection() {
        return collection;
    }
}
