package model;

public class Game {
    private Map map;
    private String username;
    private int score;
    private int pacmanHealth;

    public Game(Map map, String username, int pacmanHealth) {
        this.map = map;
        this.username = username;
        this.pacmanHealth = pacmanHealth;
    }

    public Map getMap() {
        return map;
    }

    public void addScoreOfPoint() {
        score += 5;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    public int getPacmanHealth() {
        return pacmanHealth;
    }

    public void decreasePacManHealth() {
        pacmanHealth -= 1;
    }
}
