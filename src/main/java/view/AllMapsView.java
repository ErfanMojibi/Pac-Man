package view;

import controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Cell;
import model.Map;

import java.io.IOException;
import java.util.Objects;

public class AllMapsView {
    public GridPane mapPane;
    public AnchorPane anchorPane;
    private int index = 0;

    @FXML
    public void initialize() {

        mapPane.getChildren().clear();
        mapPane.setGridLinesVisible(false);
        mapPane.setAlignment(Pos.CENTER);
        int id = MainMenuController.getInstance().getLoggedInUser().getSettings().getMapsId().get(0);
        Cell[][] map1 = Map.getMapByID(id).getArray();
        drawMapOnBoard(map1);
    }

    public void nextMap(ActionEvent actionEvent) {
        if (index + 1 < MainMenuController.getInstance().getLoggedInUser().getSettings().getMapsId().size()) {
            index++;
        } else return;
        changeMapAndShow(actionEvent);
    }

    private void drawMapOnBoard(Cell[][] map) {
        for (int i = 0; i <= 30; i++) {
            for (int j = 0; j <= 30; j++) {
                if (!map[j][i].isWall()) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(15);
                    pane.setPrefHeight(15);
                    pane.setStyle("-fx-background-color: black;");
                    mapPane.add(pane, i, j);
                } else {
                    Pane pane = new Pane();
                    pane.setPrefWidth(15);
                    pane.setPrefHeight(15);
                    pane.setStyle("-fx-background-color: #ffd31d;-fx-border-color: #ffbf00; -fx-border-radius: 2;");
                    mapPane.add(pane, i, j);
                }
            }
        }
    }

    public void previousMap(ActionEvent actionEvent) {
        if (index - 1 >= 0) {
            index--;
        } else return;
        changeMapAndShow(actionEvent);
    }

    private void changeMapAndShow(ActionEvent actionEvent) {
        mapPane.getChildren().clear();
        mapPane.setAlignment(Pos.CENTER);
        int id = MainMenuController.getInstance().getLoggedInUser().getSettings().getMapsId().get(index);
        Cell[][] map = Objects.requireNonNull(Map.getMapByID(id)).getArray();
        drawMapOnBoard(map);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.show();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MapGenerate.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }
}
