package com.cedro.musicplayer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Class representing the TreeView control for the "All tracks" page in the library
 */
public class AllTracksTreeView extends AnchorPane implements MusicItemListing {
    /**
     * Actual ListView displaying track names
     */
    @FXML
    protected TreeView<String> treeView;

    /**
     * Constructor
     */
    public AllTracksTreeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("track-tree-view.fxml"), Localization.BUNDLE);
        loader.setController(this);
        loader.setRoot(this);
        loader.load();  
    }

    @FXML
    protected void initialize() {
        this.treeView.setRoot(new TreeItem<String>("All tracks"));
    }

    @Override
    public List<MusicTrack> getTracks() {
        return Jukebox.getInstance().getMusicDatabase().getTrackMap().values().stream().collect(Collectors.toList());
    }

    @Override
    public void setupContextMenu() {
        try {
            ContextMenu cm = new MusicItemListingContextMenu(this);
            this.treeView.setContextMenu(cm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populateItems() {
        List<MusicTrack> tracks = this.getTracks();
        
        for(MusicTrack track : tracks) {
            Path trackPath = track.getFilePath();
            insertPathIntoTree(trackPath, treeView.getRoot());
        }

        flattenLinearTreeBranches(treeView.getRoot());
    }

    //FIXME incorrect placement in the tree
    private void insertPathIntoTree(Path path, TreeItem<String> treeItem) {
        if(path.getNameCount() == 0) {
            return;
        } else if(path.getNameCount() == 1) {
            treeItem.getChildren().add(new TreeItem<>(path.toString()));
        } else {
            TreeItem<String> foundChild = null;
            for(TreeItem<String> childItem : treeItem.getChildren()) {
                if(childItem.getValue() == path.getName(0).toString()) {
                    foundChild = childItem;
                }
            }

            if(foundChild != null) {
                insertPathIntoTree(path.subpath(1, -1), foundChild);
            } else {
                Iterator<Path> iter = path.iterator();
                while(iter.hasNext()) {
                    TreeItem<String> newTreeItem = new TreeItem<>(iter.next().toString());
                    treeItem.getChildren().add(newTreeItem);
                    treeItem = newTreeItem;
                }
            }
        }
    }

    private void flattenLinearTreeBranches(TreeItem<String> treeItem) {
        if(treeItem.getChildren().size() == 1 && treeItem != this.treeView.getRoot()) {
            TreeItem<String> onlyChild = treeItem.getChildren().get(0);
            List<TreeItem<String>> subchildren = onlyChild.getChildren();

            treeItem.setValue(treeItem.getValue() + "/" + onlyChild.getValue());
            treeItem.getChildren().remove(onlyChild);
            treeItem.getChildren().addAll(subchildren);

            flattenLinearTreeBranches(treeItem);
        } else {
            for(TreeItem<String> childItem : treeItem.getChildren()) {
                flattenLinearTreeBranches(childItem);
            }
        }
    }

    @Override
    public List<MusicTrack> getSelectedTracks() {
        return this.treeView
        .getSelectionModel()
        .getSelectedIndices()
        .stream()
        .map(i -> this.getTracks().get(i))
        .collect(Collectors.toList());
    }

    @Override
    public void onItemSelected(MouseEvent e) {
        
    }
}
