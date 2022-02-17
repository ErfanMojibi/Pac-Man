package view;

import controller.LoginController;
import controller.SignUpController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.messages.SignUpAndLoginMessage;

import java.io.IOException;
import java.util.Objects;

public class SignUpAndLoginView extends Application {
    public AnchorPane pane;
    public TextField username;
    public PasswordField password;
    Parent parent;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FirstPage.fxml"));
        parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("PACMAN");
        stage.show();
    }

    public void SignUp(ActionEvent actionEvent) {
        SignUpController controller = SignUpController.getInstance();
        SignUpAndLoginMessage message = controller.signUpUser(username.getText(), password.getText());
        Alert alert = new Alert(message.getAlertType());
        alert.setContentText(message.getLabel());
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
        dialogPane.getStyleClass().add("DialogPane");
        alert.show();
        if (message.equals(SignUpAndLoginMessage.INVALID_INPUT)) {
            return;
        }
        username.clear();
        password.clear();
    }

    public void Login(ActionEvent actionEvent) throws IOException {
        LoginController controller = LoginController.getInstance();
        SignUpAndLoginMessage message = controller.loginUser(username.getText(), password.getText());
        if (message == SignUpAndLoginMessage.SUCCESSFUL_LOGIN) {
            Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
            Scene scene = new Scene(url);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(message.getAlertType());
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
            dialogPane.getStyleClass().add("DialogPane");
            alert.setContentText(message.getLabel());

            alert.show();
            if (message.equals(SignUpAndLoginMessage.INVALID_INPUT)) {
                return;
            }
            username.clear();
            password.clear();
        }
    }

    public void loginAsGuest(ActionEvent actionEvent) throws IOException {
        LoginController controller = LoginController.getInstance();
        controller.loginAsGuest();
        Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
        Scene scene = new Scene(url);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}