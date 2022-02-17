package view;

import controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuView {
    public Button soundButton;
    private final Image unMutedSound = new Image(Objects.requireNonNull(getClass().getResource("/pics/unMutedSound.png")).toString());
    private final ImageView unMutedSoundView = new ImageView(unMutedSound);
    private final Image mutedSound = new Image(Objects.requireNonNull(getClass().getResource("/pics/mutedSound.png")).toString());
    private final ImageView mutedSoundImageView = new ImageView(mutedSound);
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/music/baseMusic.mp3")).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        unMutedSoundView.setFitWidth(13);
        unMutedSoundView.setFitHeight(13);
        mutedSoundImageView.setFitWidth(13);
        mutedSoundImageView.setFitHeight(13);
        soundButton.setGraphic(unMutedSoundView);
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void startGame(ActionEvent actionEvent) throws IOException {
        mediaPlayer.pause();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/StartGameView.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void openProfileMenu(ActionEvent actionEvent) throws IOException {
        mediaPlayer.pause();
        if (MainMenuController.getInstance().getLoggedInUser().getUsername().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can'T Open");
            alert.setContentText("You must sign up to enter profile menu!");
            alert.show();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfileMenu.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openSettings(ActionEvent actionEvent) throws IOException {
        mediaPlayer.pause();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/SettingsMenu.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void backToSignUp(ActionEvent actionEvent) throws IOException {
        mediaPlayer.pause();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/FirstPage.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openScoreBoard(ActionEvent actionEvent) throws IOException {
        mediaPlayer.pause();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Scoreboard.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void playOrPauseMusic(ActionEvent actionEvent) {
        if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            soundButton.setGraphic(unMutedSoundView);
            mediaPlayer.play();
        } else {
            soundButton.setGraphic(mutedSoundImageView);
            mediaPlayer.pause();
        }
    }
}
