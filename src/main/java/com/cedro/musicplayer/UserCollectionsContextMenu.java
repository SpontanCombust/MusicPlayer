package com.cedro.musicplayer;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextInputDialog;

public class UserCollectionsContextMenu extends ContextMenu {
    public UserCollectionsContextMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-collections-context-menu-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @FXML
    private void onAddNewUserCollection(ActionEvent event) {
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle(Localization.getString("library_user_collections_context_menu_item_add_collection_dialog_title"));
        tid.setHeaderText(Localization.getString("library_user_collections_context_menu_item_add_collection_dialog_header"));

        Optional<String> result = tid.showAndWait();
        if(result.isPresent()) {
            MusicCollection newCollection = new MusicCollection();
            newCollection.setName(result.get());
            Jukebox.getInstance().getMusicDatabase().addUserCollection(newCollection);
        }
    }
}
