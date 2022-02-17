package controller;

import model.User;
import view.messages.SignUpAndLoginMessage;

public class LoginController {
    private static LoginController instance = null;

    private LoginController() {

    }

    public static LoginController getInstance() {
        if (instance == null)
            instance = new LoginController();
        return instance;
    }

    public SignUpAndLoginMessage loginUser(String username, String password) {
        if (username.equals("") || password.equals(""))
            return SignUpAndLoginMessage.INVALID_INPUT;
        User user = User.getUserByUsername(username);
        if (user == null)
            return SignUpAndLoginMessage.NO_USER_FOUND;
        if (!user.isTruePassword(password))
            return SignUpAndLoginMessage.WRONG_PASSWORD;
        MainMenuController.getInstance().setLoggedInUser(user);
        return SignUpAndLoginMessage.SUCCESSFUL_LOGIN;
    }

    public void loginAsGuest() {
        User guest = new User("", "");
        MainMenuController.getInstance().setLoggedInUser(guest);
    }
}