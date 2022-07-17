package com.cedro.musicplayer;

import java.io.File;
import java.nio.file.Paths;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MusicDatabaseTest {

    MusicDatabase db = new MusicDatabase();

    //TODO tests for MusicTrack

    @Test
    void testAddUserCollection() {
        final String collectionName = "Beat Doctor";
        final String collectionCoverImagePath = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor.jpg"; 
        final String collectionTrack1Path = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Bad Man (edit).mp3";
        final String collectionTrack2Path = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Too Retro.mp3";

        MusicCollection collection = new MusicCollection();
        collection.name = collectionName;
        collection.coverImagePath = Paths.get(collectionCoverImagePath);
        collection.tracksPaths.add(Paths.get(collectionTrack1Path));
        collection.tracksPaths.add(Paths.get(collectionTrack2Path));

        db.addUserCollection(collection);

        Assertions.assertTrue(db.getUserCollectionList().stream().anyMatch(c -> c.equals(collection)));
    }

    @Test
    void testAddUserCollections() {
        final String collection1Name = "Beat Doctor";
        final String collection1CoverImagePath = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor.jpg"; 
        final String collection1Track1Path = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Bad Man (edit).mp3";
        final String collection1Track2Path = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Too Retro.mp3";

        MusicCollection collection1 = new MusicCollection();
        collection1.name = collection1Name;
        collection1.coverImagePath = Paths.get(collection1CoverImagePath);
        collection1.tracksPaths.add(Paths.get(collection1Track1Path));
        collection1.tracksPaths.add(Paths.get(collection1Track2Path));


        final String collection2Name = "glasscarpenter";
        final String collection2CoverImagePath = "./src/test/resources/com/cedro/musicplayer/glasscarpenter/cover.jpeg"; 
        final String collection2Track1Path = "./src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3";

        MusicCollection collection2 = new MusicCollection();
        collection2.name = collection2Name;
        collection2.coverImagePath = Paths.get(collection2CoverImagePath);
        collection2.tracksPaths.add(Paths.get(collection2Track1Path));
        

        db.addUserCollections(Lists.list(collection1, collection2));

        Assertions.assertTrue(db.getUserCollectionList().stream().anyMatch(c -> c.equals(collection1)));
        Assertions.assertTrue(db.getUserCollectionList().stream().anyMatch(c -> c.equals(collection2)));
    }

    @Test
    void testClearUserCollections() {
        testAddUserCollections();

        db.clearUserCollections();

        Assertions.assertTrue(db.getUserCollectionList().isEmpty());
    }

    @Test
    void testRemoveUserCollection() {
        final String collectionName = "Beat Doctor";
        final String collectionCoverImagePath = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor.jpg"; 
        final String collectionTrack1Path = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Bad Man (edit).mp3";
        final String collectionTrack2Path = "./src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Too Retro.mp3";

        MusicCollection collection = new MusicCollection();
        collection.name = collectionName;
        collection.coverImagePath = Paths.get(collectionCoverImagePath);
        collection.tracksPaths.add(Paths.get(collectionTrack1Path));
        collection.tracksPaths.add(Paths.get(collectionTrack2Path));

        db.addUserCollection(collection);

        db.removeUserCollection(collection);
        Assertions.assertTrue(db.getUserCollectionList().stream().noneMatch(c -> c.equals(collection)));
    }

    @Test
    void testLoadFromConfigurationFile() {
        Assertions.assertDoesNotThrow(() -> db.loadFromConfigurationFile(Paths.get("src/test/resources/com/cedro/musicplayer/music.mpconfig")));

        //FIXME
        // String s1 = db.getAlbumMap().toString();
        // String s2 = Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor").toAbsolutePath().toString();

        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor").toAbsolutePath()));
        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/glasscarpenter").toAbsolutePath()));
        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/Wankers United").toAbsolutePath()));

        Assertions.assertTrue(db.getUserCollectionList().stream().anyMatch(c -> c.name.equals("united beat")));

        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Bad Man (edit).mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Too Retro.mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Wankers United/Wankers United - Calva Song.mp3").toAbsolutePath()));
    }

    @Test
    void testLoadFromFileSystem() {
        Assertions.assertDoesNotThrow(() -> db.loadFromFileSystem(Paths.get("src/test/resources/com/cedro/musicplayer")));

        //FIXME
        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor").toAbsolutePath()));
        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/glasscarpenter").toAbsolutePath()));
        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/Wankers United").toAbsolutePath()));

        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Bad Man (edit).mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Too Retro.mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Wankers United/Wankers United - Calva Song.mp3").toAbsolutePath()));
    }

    @Test
    void testSaveToConfigurationFile() {
        Assertions.assertDoesNotThrow(() -> db.loadFromConfigurationFile(Paths.get("src/test/resources/com/cedro/musicplayer/music.mpconfig")));
        Assertions.assertDoesNotThrow(() -> db.saveToConfigurationFile(Paths.get("src/test/resources/com/cedro/musicplayer/test.mpconfig")));

        //FIXME
        // db.clearAlbums();
        db.clearUserCollections();

        Assertions.assertDoesNotThrow(() -> db.loadFromConfigurationFile(Paths.get("src/test/resources/com/cedro/musicplayer/test.mpconfig")));

        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor").toAbsolutePath()));
        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/glasscarpenter").toAbsolutePath()));
        // Assertions.assertTrue(db.getAlbumMap().keySet().contains(Paths.get("src/test/resources/com/cedro/musicplayer/Wankers United").toAbsolutePath()));

        Assertions.assertTrue(db.getUserCollectionList().stream().anyMatch(c -> c.name.equals("united beat")));

        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Bad Man (edit).mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor/Beat Doctor - Too Retro.mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3").toAbsolutePath()));
        Assertions.assertTrue(db.getTrackMap().containsKey(Paths.get("src/test/resources/com/cedro/musicplayer/Wankers United/Wankers United - Calva Song.mp3").toAbsolutePath()));

        new File(Paths.get("src/test/resources/com/cedro/musicplayer/test.mpconfig").toAbsolutePath().toUri()).delete();
    }
}
