package com.cedro.musicplayer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Class representing the TreeView control for the "All tracks" page in the library
 */
public class AllTracksTreeView extends AnchorPane implements MusicItemListing {

    class MusicTreeItem extends TreeItem<String> {
        private final MusicTrack track;

        public MusicTreeItem(String value, MusicTrack track) {
            super(value);
            this.track = track;
        }

        public MusicTrack getTrack() {
            return this.track;
        }
    }


    /**
     * Actual ListView displaying track names
     */
    @FXML
    protected TreeView<String> treeView;

    @FXML
    protected StackPane stackPane;
    
    @FXML
    protected Label noTracksHelpLabel;


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
        this.treeView.setShowRoot(false);
    }

    @Override
    public List<MusicTrack> fetchTracks() {
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
        this.treeView.getRoot().getChildren().clear();
        List<MusicTrack> tracks = this.fetchTracks();
        
        if(tracks.size() > 0) {
            for(MusicTrack track : tracks) {
                Path trackPath = track.getFilePath();
                insertTrackIntoTree(track, trackPath, treeView.getRoot());
            }
    
            flattenLinearTreeBranches(treeView.getRoot());
    
            expandTree(this.treeView.getRoot());        

            this.stackPane.getChildren().remove(noTracksHelpLabel);
        }
    }

    private void expandTree(TreeItem<String> item) {
        item.setExpanded(true);
        if(!item.isLeaf()) {
            item.getChildren().stream().forEach(ch -> expandTree(ch));
        }
    }

    private void insertTrackIntoTree(MusicTrack track, Path remainingPath, TreeItem<String> treeItem) {
        if(remainingPath.getNameCount() == 0) {
            return;
        } else if(remainingPath.getNameCount() == 1) {
            String trackName = remainingPath.toString();
            trackName = trackName.substring(0, trackName.lastIndexOf("."));
            treeItem.getChildren().add(new MusicTreeItem(trackName, track));
        } else {
            String currentPathName;
            if(remainingPath.isAbsolute()) {
                currentPathName = remainingPath.getRoot().toString();
                remainingPath = remainingPath.subpath(0, remainingPath.getNameCount());
            } else {
                currentPathName = remainingPath.getName(0).toString();
                remainingPath = remainingPath.subpath(1, remainingPath.getNameCount());
            }


            TreeItem<String> foundChild = treeItem.getChildren().stream()
            .filter(ch -> ch.getValue().equals(currentPathName))
            .findFirst()
            .orElse(null);

            if(foundChild == null) {
                foundChild = new TreeItem<>(currentPathName);
                treeItem.getChildren().add(foundChild);
            }

            insertTrackIntoTree(track, remainingPath, foundChild);
        }
    }

    private void flattenLinearTreeBranches(TreeItem<String> treeItem) {
        if(treeItem.getChildren().size() == 1 && treeItem != this.treeView.getRoot()) {
            TreeItem<String> onlyChild = treeItem.getChildren().get(0);
            List<TreeItem<String>> subchildren = onlyChild.getChildren();

            if(subchildren.isEmpty()) {
                return;
            }

            treeItem.setValue(Paths.get(treeItem.getValue(), onlyChild.getValue()).toString());
            
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
        .getSelectedItems()
        .stream()
        .map(i -> this.getTreeItemChildTracks(i))
        .flatMap(List::stream)
        .distinct()
        .collect(Collectors.toList());
    }

    private List<MusicTrack> getTreeItemChildTracks(TreeItem<String> item) {
        // if it's last in the hierarchy it's always MusicTreeItem
        if(item.isLeaf()) {
            return Arrays.asList(((MusicTreeItem)item).getTrack());
        } else {
            return item.getChildren().stream()
            .map(ch -> getTreeItemChildTracks(ch))
            .flatMap(List::stream)
            .collect(Collectors.toList());
        }
    }

    @Override
    public void onItemSelected(MouseEvent e) {
        
    }
}
