package com.cedro.musicplayer;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;

public class FilterTitledPane extends TitledPane {
    private MusicTrackFilter filter = new MusicTrackFilter();

    @FXML
    private ClearableTextField titleTextField;
    @FXML
    private ClearableTextField artistTextField;
    @FXML
    private ClearableTextField albumTextField;
    @FXML
    private ClearableTextField yearTextField;
    @FXML
    private ClearableTextField genreTextField;

    @FXML
    private Button applyFilterButton;


    public FilterTitledPane() throws IOException {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("filter-titled-pane.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();  
    }

    @FXML
    void initialize() {
        this.filter.titleFilterProperty.bind(this.titleTextField.textField.textProperty());
        this.filter.artistFilterProperty.bind(this.artistTextField.textField.textProperty());
        this.filter.albumFilterProperty.bind(this.albumTextField.textField.textProperty());
        this.filter.yearFilterProperty.bind(this.yearTextField.textField.textProperty());
        this.filter.genreFilterProperty.bind(this.genreTextField.textField.textProperty());
    }

    public MusicTrackFilter getFilter() {
        return filter;
    }
    
    public Button getApplyFilterButton() {
        return applyFilterButton;
    }
}
