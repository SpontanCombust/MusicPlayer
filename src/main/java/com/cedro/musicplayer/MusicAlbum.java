package com.cedro.musicplayer;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MusicAlbum extends MusicCollection {
    public static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
        "bmp", "gif", "jpg", "jpeg", "png"
    );


    protected Path dirPath;


    public static MusicAlbum fromDirectory(Path directoryPath) {
        List<File> files = Arrays.asList(directoryPath.toFile().listFiles());

        if(files.stream().anyMatch(f -> MusicTrack.isAudioFile(f.toPath()))) {
            MusicAlbum album = new MusicAlbum();
            album.dirPath = directoryPath;
            album.name = directoryPath.getFileName().toString();

            for(File f: files) {
                if(f.isFile()) {
                    MusicTrack track = MusicTrack.fromFile(f.toPath());
                    if(track != null) {
                        track.setParentAlbum(album);
                        album.tracks.add(track);
                        continue;
                    }
    
                    if(album.coverImagePath != null && isImageFile(f.toPath())) {
                        album.coverImagePath = f.toPath();
                    }
                }
            }

            return album;
        }

        return null;
    }

    public static List<MusicAlbum> fromDirectoryRecurse(Path rootDirectory) {
        List<MusicAlbum> albums = new ArrayList<>();

        File fileDir = rootDirectory.toFile();
        if(fileDir.isDirectory()) {
            for(File f: fileDir.listFiles()) {
                if(f.isDirectory() && !f.isHidden()) {
                    MusicAlbum album = fromDirectory(f.toPath());
                    if(album != null) {
                        albums.add(album);
                    }

                    for(File ff: f.listFiles()) {
                        if(ff.isDirectory()) {
                            var moreAlbums = fromDirectoryRecurse(ff.toPath());
                            albums.addAll(moreAlbums);
                        }
                    }
                }
            }
        }

        return albums;
    }

    private static boolean isImageFile(Path filePath) {
        String fileName = filePath.getFileName().toString();

        return IMAGE_EXTENSIONS.stream().anyMatch(ext -> fileName.endsWith("." + ext));
    }

    public Path getDirPath() {
        return dirPath;
    }
}
