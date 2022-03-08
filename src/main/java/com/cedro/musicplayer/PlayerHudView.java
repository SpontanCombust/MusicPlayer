package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class PlayerHudView extends VBox {
    private PlayerHudController controller = new PlayerHudController();

    public PlayerHudView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("player-hud-view.fxml"));
        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

    public PlayerHudController getController() {
        return controller;
    }
}
