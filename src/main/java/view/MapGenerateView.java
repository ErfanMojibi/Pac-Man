package view;

import controller.GenerateMapController;
import controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Cell;
import model.Map;
import model.User;

import java.io.IOException;
import java.util.Objects;

public class MapGenerateView {
    public GridPane mapPane;
    public AnchorPane anchorPane;

    @FXML
    public void initialize() {
        initializeEmptyMapView();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/SettingsMenu.fxml")));
        Scene scene = new Scene(url);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void generateMap(ActionEvent actionEvent) {
        mapPane.getChildren().clear();
        mapPane.setAlignment(Pos.CENTER);
        Cell[][] map = GenerateMapController.getInstance().generateMap();
        for (int i = 0; i <= 30; i++) {
            for (int j = 0; j <= 30; j++) {
                if (!map[j][i].isWall()) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(12);
                    pane.setPrefHeight(12);
                    pane.setStyle("-fx-background-color: black;");
                    mapPane.add(pane, i, j);
                } else {
                    Pane pane = new Pane();
                    pane.setPrefWidth(12);
                    pane.setPrefHeight(12);
                    pane.setStyle("-fx-background-color: #ffd31d;-fx-border-color: #ffbf00;-fx-border-radius: 2;");
                    mapPane.add(pane, i, j);
                }
            }
        }

        Button button = new Button();
        button.setLayoutX(337);
        button.setLayoutY(505);

        button.setText("SaveMap");
        button.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/GenerateMap.css")).toExternalForm());
        button.getStyleClass().add("button");
        button.setPrefHeight(26.0);
        anchorPane.getChildren().add(button);
        button.setOnAction(actionEvent1 -> {
            Map myMap = new Map(map);
            Map.jsonMaps(myMap);
            MainMenuController.getInstance().getLoggedInUser().getSettings().addMap(myMap.getId());
            User.jsonUsers(MainMenuController.getInstance().getLoggedInUser());
            initializeEmptyMapView();
            anchorPane.getChildren().remove(button);
        });
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.show();
    }

    private void initializeEmptyMapView() {
        mapPane.getChildren().clear();
        mapPane.setGridLinesVisible(false);
        mapPane.setHgap(0);
        mapPane.setVgap(0);
        mapPane.setAlignment(Pos.CENTER);
        for (int i = 0; i <= 30; i++) {
            for (int j = 0; j <= 30; j++) {
                Rectangle rectangle = new Rectangle(12, 12);
                rectangle.setStyle("-fx-fill: white;");
                mapPane.add(rectangle, i, j);
            }
        }
    }

    public void openAllMapsPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/AllMapsPage.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
