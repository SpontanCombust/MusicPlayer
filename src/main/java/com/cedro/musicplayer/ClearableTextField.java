package com.cedro.musicplayer;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ClearableTextField extends AnchorPane {
    @FXML
    public TextField textField;

    public ClearableTextField() throws IOException {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clearable-text-field.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();  
    }

    @FXML
    private void onClear(ActionEvent event) {
        textField.setText("");
    }
}
