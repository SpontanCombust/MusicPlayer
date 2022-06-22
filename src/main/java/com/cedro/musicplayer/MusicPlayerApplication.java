package com.cedro.musicplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MusicPlayerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MusicPlayerApplication.class.getResource("root-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Music Player");
        stage.getIcons().add(MusicAlbum.DEFAULT_COVER_IMAGE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}