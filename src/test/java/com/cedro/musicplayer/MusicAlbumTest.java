package com.cedro.musicplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public class MusicAlbumTest {
    @Test
    void testFromDirectory() {
        MusicAlbum album = MusicAlbum.fromDirectory(Paths.get("./src/test/resources/com/cedro/musicplayer/Beat Doctor"));
        Assertions.assertNotNull(album);
        Assertions.assertEquals(2, album.tracksPaths.size());

        album = MusicAlbum.fromDirectory(Paths.get("./src/test/resources/com/cedro/musicplayer"));
        Assertions.assertNull(album);
    }

    @Test
    void testFromDirectoryRecurse() {
        List<MusicAlbum> albums = MusicAlbum.fromDirectoryRecurse(Paths.get("./src/test/resources/com/cedro/musicplayer/"));
        Assertions.assertEquals(albums.size(), 3);
        
        int c = 0;
        for(MusicAlbum album : albums) {
            c += album.tracksPaths.size();
        }

        Assertions.assertEquals(c, 4);
    }

    @Test
    void testGetDirPath() {
        Path p = Paths.get("src/test/resources/com/cedro/musicplayer/Beat Doctor");
        MusicAlbum album = MusicAlbum.fromDirectory(p);
        assertEquals(p.toAbsolutePath(), album.getDirPath());
    }
}
