module PacMan {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires gson;
    requires java.sql;
    requires jdk.localedata;
    opens view to javafx.fxml;
    exports view;
    exports view.messages;
    opens view.messages to javafx.fxml;
    exports view.game;
    opens view.game to javafx.fxml;
    opens model to gson;
}