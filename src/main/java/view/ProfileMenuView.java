package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.MainMenuController;
import controller.ProfileMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import view.messages.ProfileMenuMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class ProfileMenuView {
    public Label score;
    public Label username;

    @FXML
    public void initialize() {
        username.setText("Username : " + MainMenuController.getInstance().getLoggedInUser().getUsername());
        score.setText("Score : " + MainMenuController.getInstance().getLoggedInUser().getScore());
    }

    public void changeUsername(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Username");
        dialog.setHeaderText("Enter new Username");
        dialog.setContentText("Username : ");
        Optional<String> input = dialog.showAndWait();
        if (input.isPresent()) {
            ProfileMenuMessage message = ProfileMenuController.getInstance().changeUsername(dialog.getEditor().getText());
            Alert alert = new Alert(message.getAlertType());
            alert.setContentText(message.getLabel());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
            dialogPane.getStyleClass().add("DialogPane");
            alert.showAndWait();
            username.setText("Username : " + MainMenuController.getInstance().getLoggedInUser().getUsername());
        }
    }

    public void changePassword(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter new Password");
        dialog.setContentText("Password : ");

        Optional<String> input = dialog.showAndWait();
        if (input.isPresent()) {
            ProfileMenuMessage message = ProfileMenuController.getInstance().changePassword(dialog.getEditor().getText());
            Alert alert = new Alert(message.getAlertType());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
            dialogPane.getStyleClass().add("DialogPane");
            alert.setContentText(message.getLabel());

            alert.showAndWait();
        }
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        ProfileMenuController.getInstance().logout();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/FirstPage.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAccount(ActionEvent actionEvent) throws IOException {
        String userDirectoryPath = System.getProperty("user.dir") +
                File.separator + "database" + File.separator + "users";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
        dialogPane.getStyleClass().add("DialogPane");
        alert.setHeaderText("Do you want to delete your account? ");
        alert.setTitle("Delete Confirmation");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            String username = MainMenuController.getInstance().getLoggedInUser().getUsername();
            User.deleteUser(MainMenuController.getInstance().getLoggedInUser());
            File file = new File(Paths.get("database" + File.separator + "users" + File.separator + username + ".json").toAbsolutePath().toString());
            if (file.delete()) {
                System.out.println("done");
            } else System.out.println("failed");
            logout(actionEvent);
        }
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
        Scene scene = new Scene(url);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}