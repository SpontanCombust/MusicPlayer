package com.cedro.musicplayer;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class
 */
public class MusicPlayerApplication extends Application {
    /**
     * Version of the application 
     */
    public static final String VERSION = "2.0";

    /**
     * Method ran at the start of application
     */
    @Override
    public void start(Stage stage) throws IOException {
        RootController root = new RootController();
        Scene scene = new Scene(root);
        stage.setTitle("Music Player");
        stage.getIcons().add(MusicTrack.DEFAULT_COVER_IMAGE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}