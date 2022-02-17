package view;

import model.User;

import java.util.ArrayList;

public class ScoreBoardData {

     int rank;
     String username;
     int score;
     static final ArrayList<ScoreBoardData> scoreBoardData;

    static {
        scoreBoardData = new ArrayList<>();
    }


    public ScoreBoardData(String username, int score, int rank) {
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    public static void getAndSetDataFromUser() {
        scoreBoardData.clear();
        ArrayList<User> users = User.getSortedUsers();
        int counter = 1;
        for (User user : users) {
            scoreBoardData.add(new ScoreBoardData(user.getUsername(), user.getScore(), counter));
            counter++;
        }
    }

    public static ArrayList<ScoreBoardData> getScoreBoardData() {
        return scoreBoardData;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

}
