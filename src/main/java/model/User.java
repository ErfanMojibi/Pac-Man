package model;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;


public class User {
    private String username;
    private String password;
    private Score score;
    private static ArrayList<User> allUsers;
    private GameSettings settings;
    private static final String userDirectoryPath = System.getProperty("user.dir") +
            File.separator + "database" + File.separator + "users";

    static {
        allUsers = new ArrayList<>();
        readUsers();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        score = new Score();
        score.setPoint(0);
        settings = new GameSettings();
        allUsers.add(this);
    }

    public GameSettings getSettings() {
        return settings;
    }

    public String getUsername() {
        return username;
    }


    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.username.equals(username))
                return user;
        }
        return null;
    }

    public boolean isTruePassword(String password) {
        return this.password.equals(password);
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public static void deleteUser(User user) {
        allUsers.remove(user);
    }

    public int getScore() {
        return score.getPoint();
    }


    public static ArrayList<User> getSortedUsers() {
        Comparator<User> comparator = Comparator.comparingInt(User::getScore).reversed();
        allUsers.sort(comparator);
        ArrayList<User> users = new ArrayList<>(allUsers);
        users.removeIf(user -> user.getUsername().equals(""));
        return users;
    }

    public void setScore(int score) {
        this.score.setPoint(score);
    }

    public static void jsonUsers(User user) {
        try {
            FileWriter myWriter = new FileWriter(userDirectoryPath + File.separator + user.getUsername() + ".json");
            new Gson().toJson(user, myWriter);
            myWriter.flush();
            myWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readUsers() {
        File users = new File(userDirectoryPath);
        File[] userFiles = users.listFiles();
        if (userFiles == null) {
            return;
        }
        for (File file : userFiles) {
            if (!file.getName().matches(".*[.]json")) {
                continue;
            }
            try {
                FileReader fileReader = new FileReader(file);
                User user = new Gson().fromJson(fileReader, User.class);
                allUsers.add(user);
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}