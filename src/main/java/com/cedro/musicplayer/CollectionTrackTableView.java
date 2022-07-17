package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;

/**
 * Class representing the TableView control displayed when opening any collection
 */
public class CollectionTrackTableView extends TrackTableView {
    /**
     * Music collection, which tracks are to be displayed in the table view
     */
    private MusicCollection collection;

    /**
     * Constructor
     * 
     * @param collection - collection which tracks will be displayed in the table view
     * @throws IOException
     */
    public CollectionTrackTableView(MusicCollection collection) throws IOException {
        super();
        this.collection = collection;
    }

    @Override
    public List<MusicTrack> fetchTracks() {
        return collection.getTracks();
    }

    
    /** 
     * Get the collection which tracks are displayed in the table view
     * 
     * @return MusicCollection - music collection
     */
    public MusicCollection getCollection() {
        return collection;
    }
}
