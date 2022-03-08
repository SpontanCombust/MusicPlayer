module com.cedro.musicplayer {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.cedro.musicplayer to javafx.fxml;
    exports com.cedro.musicplayer;
}