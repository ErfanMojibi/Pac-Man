package controller;

public class SettingsController {
    private static SettingsController instance = null;

    private SettingsController() {

    }

    public static SettingsController getInstance() {
        if (instance == null)
            instance = new SettingsController();
        return instance;
    }

}
