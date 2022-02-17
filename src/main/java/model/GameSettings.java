package model;

import controller.MainMenuController;

import java.util.ArrayList;

public class GameSettings {
    private int pacManHealth;
    private ArrayList<Integer> mapsId;

    {
        mapsId = new ArrayList<>();
        mapsId.add(1);
        mapsId.add(2);
        mapsId.add(3);
    }

    public GameSettings() {
        this.pacManHealth = 5;
    }

    public double getPacManHealth() {
        return pacManHealth;
    }

    public void setPacManHealth(int pacManHealth) {
        this.pacManHealth = pacManHealth;
        User.jsonUsers(MainMenuController.getInstance().getLoggedInUser());
    }

    public ArrayList<Integer> getMapsId() {
        return mapsId;
    }

    public void addMap(int id) {
        mapsId.add(id);
    }
}
