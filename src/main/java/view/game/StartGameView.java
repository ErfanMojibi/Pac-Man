package view.game;

import controller.GameController;
import controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Cell;
import model.Game;
import model.Map;

import java.io.IOException;
import java.util.Objects;

public class StartGameView {
    public GridPane showMapPane;
    public AnchorPane anchorPane;
    private int index = 0;

    @FXML
    public void initialize() {
        showMapPane.getChildren().clear();
        showMapPane.setAlignment(Pos.CENTER);
        int id = MainMenuController.getInstance().getLoggedInUser().getSettings().getMapsId().get(0);
        Cell[][] map1 = Objects.requireNonNull(Map.getMapByID(id)).getArray();
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
                if (map[j][i].isWall()) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(15);
                    pane.setPrefHeight(15);
                    pane.setStyle("-fx-background-color: #ffd31d;-fx-border-color: #ffbf00; -fx-border-radius: 2;");
                    showMapPane.add(pane, i, j);
                } else {
                    Pane pane = new Pane();
                    pane.setPrefWidth(15);
                    pane.setPrefHeight(15);
                    pane.setStyle("-fx-background-color: black;");
                    showMapPane.add(pane, i, j);
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
        showMapPane.getChildren().clear();
        showMapPane.setAlignment(Pos.CENTER);
        int id = MainMenuController.getInstance().getLoggedInUser().getSettings().getMapsId().get(index);
        Cell[][] map = Objects.requireNonNull(Map.getMapByID(id)).getArray();
        drawMapOnBoard(map);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.show();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
        Scene scene = new Scene(url);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void SelectMapAndStartGame(ActionEvent actionEvent) throws IOException {
        int id = MainMenuController.getInstance().getLoggedInUser().getSettings().getMapsId().get(index);
        Game game = new Game(Map.getMapByID(id), MainMenuController.getInstance().getLoggedInUser().getUsername(), (int) MainMenuController.getInstance().getLoggedInUser().getSettings().getPacManHealth());
        GameController.getInstance().setGame(game);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));

        Scene scene = new Scene(loader.load());
        GameView controller = loader.getController();
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();

            if (code.equals(KeyCode.W) || code == KeyCode.UP) {
                if (controller.isPaused()) {
                    controller.continueGame();
                }
                controller.moveUp();
            } else if (code.equals(KeyCode.D) || code == (KeyCode.RIGHT)) {
                if (controller.isPaused()) {
                    controller.continueGame();
                }
                controller.moveRight();
            } else if (code.equals(KeyCode.S) || code == (KeyCode.DOWN)) {
                if (controller.isPaused()) {
                    controller.continueGame();
                }
                controller.moveDown();
            } else if (code.equals(KeyCode.A) || code == (KeyCode.LEFT)) {
                if (controller.isPaused()) {
                    controller.continueGame();
                }
                controller.moveLeft();
            } else if (code.equals(KeyCode.ESCAPE)) {
                if (!controller.isPaused())
                    controller.pauseGame();
                else
                    controller.continueGame();
            }
        });
        scene.getRoot().requestFocus();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}