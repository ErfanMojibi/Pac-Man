package controller;

import com.google.gson.Gson;
import model.User;
import view.messages.SignUpAndLoginMessage;

import java.io.File;
import java.io.FileWriter;

public class SignUpController {
    private static SignUpController instance = null;

    private SignUpController() {

    }

    public static SignUpController getInstance() {
        if (instance == null)
            instance = new SignUpController();
        return instance;
    }

    public SignUpAndLoginMessage signUpUser(String username, String password) {
        if (username.equals("") || password.equals(""))
            return SignUpAndLoginMessage.INVALID_INPUT;
        if (User.getUserByUsername(username) != null)
            return SignUpAndLoginMessage.USER_EXISTS;
        User.jsonUsers(new User(username, password));
        return SignUpAndLoginMessage.SUCCESSFUL_SIGN_UP;
    }


}
