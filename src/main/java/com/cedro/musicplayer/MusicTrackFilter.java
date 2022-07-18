package com.cedro.musicplayer;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;

public class MusicTrackFilter {
    public SimpleStringProperty titleFilterProperty = new SimpleStringProperty();
    public SimpleStringProperty artistFilterProperty = new SimpleStringProperty();
    public SimpleStringProperty albumFilterProperty = new SimpleStringProperty();
    public SimpleStringProperty yearFilterProperty = new SimpleStringProperty();
    public SimpleStringProperty genreFilterProperty = new SimpleStringProperty();

    public List<MusicTrack> filter(List<MusicTrack> musicTracks) {
        return musicTracks.stream()
        .filter( t -> 
            (this.titleFilterProperty.get() == "" || t.getTitle().toLowerCase().contains(this.titleFilterProperty.get().toLowerCase()))
            &&
            (this.artistFilterProperty.get() == "" || t.getArtist().toLowerCase().contains(this.artistFilterProperty.get().toLowerCase()))
            &&
            (this.albumFilterProperty.get() == "" || t.getAlbum().toLowerCase().contains(this.albumFilterProperty.get().toLowerCase()))
            &&
            (this.yearFilterProperty.get() == "" || t.getYear().toLowerCase().contains(this.yearFilterProperty.get().toLowerCase()))
            &&
            (this.genreFilterProperty.get() == "" || t.getGenre().toLowerCase().contains(this.genreFilterProperty.get().toLowerCase()))
            
        ).collect(Collectors.toList());
    }
}
