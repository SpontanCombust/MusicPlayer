package com.cedro.musicplayer;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public abstract class TrackTableView extends AnchorPane implements MusicItemListing {

    @FXML
    private TableView<MusicTrack> tableView;

    public TrackTableView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-table-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @FXML
    void initialize() {
        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    public void setupContextMenu() {
        try {
            ContextMenu cm = new MusicItemListingContextMenu(this);
            this.tableView.setContextMenu(cm);

            this.tableView.setOnContextMenuRequested(e -> {
                if(this.getSelectedTracks().size() == 0) {
                    cm.hide();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populateItems() {
        this.tableView.getItems().setAll(this.fetchTracks());
    }

    @Override
    public List<MusicTrack> getSelectedTracks() {
        return this.tableView
        .getSelectionModel()
        .getSelectedItems();
    }

    @Override
    public void onItemSelected(MouseEvent e) {
        
    }
    
}
