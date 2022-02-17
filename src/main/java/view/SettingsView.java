package view;

import controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SettingsView {
    public ProgressBar pacmanHealth;
    public Label label;

    @FXML
    public void initialize() {
        pacmanHealth.setStyle("-fx-accent: lightgreen;");
        pacmanHealth.setProgress(MainMenuController.getInstance().getLoggedInUser().getSettings().getPacManHealth() / 5);
        label.setText("Pac-Man Health : " + MainMenuController.getInstance().getLoggedInUser().getSettings().getPacManHealth());
    }

    public void decreaseHealth(ActionEvent actionEvent) {
        int health = (int) (pacmanHealth.getProgress() * 5);
        if (health == 2)
            return;
        health -= 1;
        pacmanHealth.setProgress((double) health / 5);
        MainMenuController.getInstance().getLoggedInUser().getSettings().setPacManHealth(health);
        label.setText("Pac-Man Health : " + MainMenuController.getInstance().getLoggedInUser().getSettings().getPacManHealth());
    }

    public void increaseHealth(ActionEvent actionEvent) {
        int health = (int) (pacmanHealth.getProgress() * 5);
        if (health == 5)
            return;
        health += 1;
        pacmanHealth.setProgress((double) health / 5);
        MainMenuController.getInstance().getLoggedInUser().getSettings().setPacManHealth(health);
        label.setText("Pac-Man Health : " + MainMenuController.getInstance().getLoggedInUser().getSettings().getPacManHealth());
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
        Scene scene = new Scene(url);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openMapsMenu(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MapGenerate.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }
}
