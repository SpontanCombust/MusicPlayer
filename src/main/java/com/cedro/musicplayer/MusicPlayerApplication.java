package com.cedro.musicplayer;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MusicPlayerApplication extends Application {
    public static final String VERSION = "1.0";

    @Override
    public void start(Stage stage) throws IOException {
        RootController root = new RootController();
        Scene scene = new Scene(root);
        stage.setTitle("Music Player");
        stage.getIcons().add(MusicAlbum.DEFAULT_COVER_IMAGE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}