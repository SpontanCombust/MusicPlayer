package com.cedro.musicplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class MusicTrackTest {

    @Start
    public void start(Stage stage) {
        
    }

    @Test
    void testFromFile() {
        MusicTrack t1 = MusicTrack.fromFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3"));
        MusicTrack t2 = MusicTrack.fromFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/cover.jpeg"));
        MusicTrack t3 = MusicTrack.fromFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/nonexistent.mp3"));
        
        assertNotNull(t1);
        assertNull(t2);
        assertNull(t3);
    }

    @Test
    void testGetFilePath() {
        MusicTrack t1 = MusicTrack.fromFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3"));

        assertNotNull(t1);
        assertEquals(
            Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3").toAbsolutePath(),
            t1.getFilePath()
        );    
    }

    @Test
    void testGetName() {
        MusicTrack t1 = MusicTrack.fromFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3"));

        assertNotNull(t1);
        assertEquals("glasscarpenter - Funeral", t1.getFileName());
    }

    @Test
    void testIsAudioFile() {
        assertTrue(MusicTrack.isAudioFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3")));
        assertFalse(MusicTrack.isAudioFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/cover.jpeg")));
    }

    @Test
    void testLoadMedia() {
        MusicTrack t1 = MusicTrack.fromFile(Path.of("./src/test/resources/com/cedro/musicplayer/glasscarpenter/glasscarpenter - Funeral.mp3"));
        assertNotNull(t1.loadMedia());
    }
}
