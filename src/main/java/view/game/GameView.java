package view.game;

import controller.Direction;
import controller.GameController;
import controller.MainMenuController;
import controller.MoveResult;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cell;
import model.Game;
import model.Map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class GameView {
    public GridPane mapPane;
    public Label username;
    public Label scoreLabel;
    public Label pacmanLife;
    public Button soundButton;
    private Map map;
    public Group pacman;
    private Timeline pacmanMouthMoveTimeline;
    private Timeline pacmanMovementTimeline;
    private Timeline ghostsTimeLine;
    private boolean isEnergySituation;
    //------------------
    private final AudioClip death = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/death.wav")).toString());
    private final AudioClip eat = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/eat.wav")).toString());
    private boolean isMuted = false;

    private final Image unMutedSound = new Image(Objects.requireNonNull(getClass().getResource("/pics/unMutedSound.png")).toString());
    private final ImageView unMutedSoundView = new ImageView(unMutedSound);
    private final Image mutedSound = new Image(Objects.requireNonNull(getClass().getResource("/pics/mutedSound.png")).toString());
    private final ImageView mutedSoundImageView = new ImageView(mutedSound);
    //-----------------------------------------------------
    private final Image left = new Image(Objects.requireNonNull(getClass().getResource("/pics/pacman/Left.png")).toString());
    private final Image right = new Image(Objects.requireNonNull(getClass().getResource("/pics/pacman/Right.png")).toString());
    private final Image down = new Image(Objects.requireNonNull(getClass().getResource("/pics/pacman/Down.png")).toString());
    private final Image up = new Image(Objects.requireNonNull(getClass().getResource("/pics/pacman/Up.png")).toString());
    private final Image closed = new Image(Objects.requireNonNull(getClass().getResource("/pics/pacman/Closed.png")).toString());
    private final ImageView pacmanLeftView = new ImageView(left);
    private final ImageView pacmanRightView = new ImageView(right);
    private final ImageView pacmanDownView = new ImageView(down);
    private final ImageView pacmanUpView = new ImageView(up);
    private final ImageView pacmanClosedView = new ImageView(closed);
    private ImageView currentImageView;
    private int score = 0;
    private int life = 0;
    //--------------------------------------------------------
    private final Image ghost1 = new Image(Objects.requireNonNull(getClass().getResource("/pics/ghost1.png")).toString());
    private final Image ghost2 = new Image(Objects.requireNonNull(getClass().getResource("/pics/ghost2.png")).toString());
    private final Image ghost3 = new Image(Objects.requireNonNull(getClass().getResource("/pics/ghost3.png")).toString());
    private final Image ghost4 = new Image(Objects.requireNonNull(getClass().getResource("/pics/ghost4.png")).toString());
    private final ImageView ghost1View = new ImageView(ghost1);
    private final ImageView ghost2View = new ImageView(ghost2);
    private final ImageView ghost3View = new ImageView(ghost3);
    private final ImageView ghost4View = new ImageView(ghost4);
    private final ArrayList<Group> ghosts = new ArrayList<>();
    private boolean isPaused = false;
    private MediaPlayer mediaPlayer;

    public boolean isPaused() {
        return isPaused;
    }

    @FXML
    public void initialize() {
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/music/gameMusic.mp3")).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        ghosts.add(new Group(ghost1View));
        ghosts.add(new Group(ghost2View));
        ghosts.add(new Group(ghost3View));
        ghosts.add(new Group(ghost4View));
        //-----------------------------------------
        unMutedSoundView.setFitWidth(13);
        unMutedSoundView.setFitHeight(13);
        mutedSoundImageView.setFitWidth(13);
        mutedSoundImageView.setFitHeight(13);
        soundButton.setGraphic(unMutedSoundView);
        //-----------------------------------------
        map = GameController.getInstance().getGame().getMap();
        currentImageView = pacmanRightView;
        GameController.getInstance().setView(this);

        mapPane.getChildren().clear();
        mapPane.setGridLinesVisible(false);
        mapPane.setAlignment(Pos.CENTER);

        GameController.getInstance().setPacmanCurrentRow(14);
        GameController.getInstance().setPacmanCurrentColumn(15);

        drawMapOnBoard(map.getArray());
        pacmanMovementTimeline = new Timeline();
        ghostsTimeLine = new Timeline();
        username.setText("Username : " + GameController.getInstance().getUsername());
        pacmanMovementTimeline.setCycleCount(Animation.INDEFINITE);
        pacmanMovementTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), (ActionEvent) -> {
            Pane previousPane = getPaneInMap(mapPane, GameController.getInstance().getPacmanCurrentRow(), GameController.getInstance().getPacmanCurrentColumn());
            Direction direction = GameController.getInstance().getCurrentDirection();
            currentImageView.setFitWidth(15);
            currentImageView.setFitHeight(15);
            pacmanClosedView.setFitHeight(15);
            pacmanClosedView.setFitWidth(15);
            if (GameController.getInstance().getScore() != score) {
                score = GameController.getInstance().getScore();
                scoreLabel.setText("Score: " + score);
            }
            if (direction != null) {
                MoveResult result = null;
                switch (direction) {
                    case Right:
                        result = GameController.getInstance().moveRight();
                        checkMove(previousPane, result);
                        break;
                    case Left:
                        result = GameController.getInstance().moveLeft();
                        checkMove(previousPane, result);
                        break;
                    case Down:
                        result = GameController.getInstance().moveDown();
                        checkMove(previousPane, result);
                        break;
                    case Up:
                        result = GameController.getInstance().moveUp();
                        checkMove(previousPane, result);
                        break;
                }
                if (result == MoveResult.GAME_FINISHED) {
                    try {
                        finishGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (result == MoveResult.ACCIDENT) {
                    GameController.getInstance().setPacmanCurrentColumn(15);
                    GameController.getInstance().setPacmanCurrentRow(14);
                } else if (result == MoveResult.BOARD_EMPTY) {
                    pauseGame();
                    GameController.getInstance().setPacmanCurrentRow(14);
                    GameController.getInstance().setPacmanCurrentColumn(15);
                    GameController.getInstance().placeGhosts();
                    ghostsTimeLine.stop();
                    reFillBoard();
                    continueGame();
                }
            }
            if (GameController.getInstance().getGame().getPacmanHealth() != life) {
                life = GameController.getInstance().getGame().getPacmanHealth();
                pacmanLife.setText("PacManHealth : " + life);
            }
        }

        ));
        //--------

        Timeline wait = new Timeline();
        wait.getKeyFrames().add(new KeyFrame(Duration.seconds(5)));
        wait.setCycleCount(1);

        //--------
        pacmanMovementTimeline.play();
        //------------------------ ghosts :
        //ghostsTimeLine = new Timeline();

        ghostsTimeLine.setCycleCount(Animation.INDEFINITE);
        ghostsTimeLine.getKeyFrames().add(new KeyFrame(Duration.millis(411), (ActionEvent) -> {
            ghost1View.setFitHeight(15);
            ghost1View.setFitWidth(15);
            ghost2View.setFitHeight(15);
            ghost2View.setFitWidth(15);
            ghost3View.setFitHeight(15);
            ghost3View.setFitWidth(15);
            ghost4View.setFitHeight(15);
            ghost4View.setFitWidth(15);
            moveGhost1();
            moveGhost2();
            moveGhost3();
            moveGhost4();
        }));
        wait.play();
        wait.setOnFinished((ActionEvent) -> ghostsTimeLine.play());
    }

    private void reFillBoard() {
        for (int i = 0; i <= 30; i++) {
            for (int j = 0; j <= 30; j++) {
                if (!map.getArray()[j][i].isWall()) {
                    Pane pane = getPaneInMap(mapPane, j, i);
                    addPointToPane(Objects.requireNonNull(pane));
                }
            }
        }
    }

    private void addPointToPane(Pane pane) {
        Circle point = new Circle(2);
        point.setLayoutX(9);
        point.setLayoutY(7.5);
        point.setFill(Color.LIGHTGREEN);
        pane.getChildren().add(point);
        pane.setPrefWidth(15);
        pane.setPrefWidth(15);
        pane.setStyle("-fx-background-color: black;");
    }

    private void checkMove(Pane previousPane, MoveResult result) {
        Pane nextPane;
        if (result == MoveResult.GET_POINT) {
            nextPane = getPaneInMap(mapPane, GameController.getInstance().getPacmanCurrentRow(), GameController.getInstance().getPacmanCurrentColumn());
            Objects.requireNonNull(nextPane).getChildren().clear();
            nextPane.getChildren().add(this.pacman);
            if (!isMuted) {
                eat.setRate(1.5);
                eat.setVolume(1);
                eat.play();
            }
        } else if (result == MoveResult.ACCIDENT) {
            nextPane = getPaneInMap(mapPane, GameController.getInstance().getPacmanCurrentRow(), GameController.getInstance().getPacmanCurrentColumn());
            if (nextPane != previousPane) {
                previousPane.getChildren().remove(pacman);
                Objects.requireNonNull(nextPane).getChildren().add(this.pacman);
            }
            if (!isMuted) {
                death.setRate(1.1);
                death.play();
            }
            pauseGame();
            Timeline waitOfDeath = new Timeline();
            waitOfDeath.setCycleCount(1);
            waitOfDeath.getKeyFrames().add(new KeyFrame(Duration.seconds(1)));
            waitOfDeath.play();
            waitOfDeath.setOnFinished(actionEvent -> {
                GameController.getInstance().setPacmanCurrentColumn(15);
                GameController.getInstance().setPacmanCurrentRow(14);
                Pane pane = getPaneInMap(mapPane, 14, 15);
                pane.getChildren().add(pacman);
            });
        } else if (result == MoveResult.NOTHING) {
            previousPane.getChildren().remove(pacman);
            nextPane = getPaneInMap(mapPane, GameController.getInstance().getPacmanCurrentRow(), GameController.getInstance().getPacmanCurrentColumn());
            Objects.requireNonNull(nextPane).getChildren().add(this.pacman);
        }
    }


    public void drawMapOnBoard(Cell[][] map) {
        for (int i = 0; i <= 30; i++) {
            for (int j = 0; j <= 30; j++) {
                if (map[j][i].hasPoint()) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(15);
                    pane.setPrefHeight(15);
                    addPacmanToBoard(i, j, pane);
                    addPointToPane(pane);

                    if (i == 1 && j == 1) {//ghost 1 : 1,1
                        ghost1View.setFitHeight(15);
                        ghost1View.setFitWidth(15);
                        pane.getChildren().add(ghosts.get(0));
                    } else if (i == 1 && j == 29) {//ghost 2 : 1,29
                        ghost2View.setFitHeight(15);
                        ghost2View.setFitWidth(15);
                        pane.getChildren().add(ghosts.get(1));
                    } else if (i == 29 && j == 1) {//ghost 3 : 29,1
                        ghost3View.setFitHeight(15);
                        ghost3View.setFitWidth(15);
                        pane.getChildren().add(ghosts.get(2));
                    } else if (i == 29 && j == 29) {//ghost 4 : 29,29
                        ghost4View.setFitHeight(15);
                        ghost4View.setFitWidth(15);
                        pane.getChildren().add(ghosts.get(3));
                    }
                    mapPane.add(pane, i, j);
                } else if (map[j][i].isWall()) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(15);
                    pane.setPrefHeight(15);
                    pane.setStyle("-fx-background-color: #ffd31d;-fx-border-color: #ffbf00; -fx-border-radius: 2;");
                    mapPane.add(pane, i, j);
                    addPacmanToBoard(i, j, pane);
                } else if (map[j][i].hasBomb()) {
                    Pane pane = new Pane();
                    Circle point = new Circle(2);
                    point.setCenterX(pane.getLayoutX() + 9);
                    point.setCenterY(pane.getLayoutY() + 7.5);
                    point.setFill(Color.RED);
                    pane.setPrefWidth(15);
                    pane.setPrefHeight(15);
                    pane.getChildren().add(point);
                    pane.setStyle("-fx-background-color: black;");
                    mapPane.add(pane, i, j);
                }
            }
        }


    }


    private void addPacmanToBoard(int i, int j, Pane pane) {
        if (i == GameController.getInstance().getPacmanCurrentColumn() && j == GameController.getInstance().getPacmanCurrentRow()) {
            pacman = new Group(currentImageView);
            pacman.setId("pacman");
            System.out.println(mapPane.lookup("pacman"));
            pacman.setLayoutX(pane.getLayoutX() + 2);
            pacman.setLayoutY(pane.getLayoutY() + 2);
            pane.getChildren().add(pacman);
            pacmanMouthMoveTimeline = getTimeLine(pacman);
            pacmanMouthMoveTimeline.play();
        }
    }


    private Timeline getTimeLine(Group pacman) {
        currentImageView.setFitWidth(15);
        currentImageView.setFitHeight(15);
        pacmanLeftView.setFitWidth(15);
        pacmanRightView.setFitWidth(15);
        pacmanLeftView.setFitHeight(15);
        pacmanRightView.setFitHeight(15);
        pacmanDownView.setFitWidth(15);
        pacmanUpView.setFitWidth(15);
        pacmanDownView.setFitHeight(15);
        pacmanUpView.setFitHeight(15);
        pacmanClosedView.setFitHeight(15);
        pacmanClosedView.setFitWidth(15);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(150), (ActionEvent) -> {
            pacman.getChildren().setAll(currentImageView);
        }));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), (ActionEvent) -> {
            pacman.getChildren().setAll(pacmanClosedView);
        }));

        return timeline;
    }

    public void moveRight() {
        if (GameController.getInstance().canUserChangeMove(Direction.Right))
            GameController.getInstance().changeMove(Direction.Right);
    }

    public void moveUp() {
        if (GameController.getInstance().canUserChangeMove(Direction.Up))
            GameController.getInstance().changeMove(Direction.Up);

    }

    public void moveDown() {
        if (GameController.getInstance().canUserChangeMove(Direction.Down))
            GameController.getInstance().changeMove(Direction.Down);
    }

    public void moveLeft() {
        if (GameController.getInstance().canUserChangeMove(Direction.Left))
            GameController.getInstance().changeMove(Direction.Left);


    }

    public void pauseGame() {
        pacmanMovementTimeline.pause();
        ghostsTimeLine.pause();
        pacmanMouthMoveTimeline.pause();
        isPaused = true;
    }

    public void continueGame() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pacmanMovementTimeline.play();
                pacmanMouthMoveTimeline.play();
                ghostsTimeLine.play();
                isPaused = false;
            }
        });

    }

    public void setCurrentImageView(Direction direction) {
        if (direction == Direction.Right)
            this.currentImageView = pacmanRightView;
        else if (direction == Direction.Left)
            this.currentImageView = pacmanLeftView;
        else if (direction == Direction.Up)
            this.currentImageView = pacmanUpView;
        else if (direction == Direction.Down)
            this.currentImageView = pacmanDownView;
    }


    private Pane getPaneInMap(GridPane gridPane, int row, int column) {
        for (Node child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == column)
                return (Pane) child;
        }
        return null;
    }

    private void moveGhost1() {
        int row = GameController.getInstance().getGhostRow(1);
        int column = GameController.getInstance().getGhostColumn(1);
        Pane prevPane = getPaneInMap(mapPane, row, column);
        MoveResult result = GameController.getInstance().moveGhost(1);
        if (result == MoveResult.NOTHING) {
            changePositionGhost1(prevPane);
        } else if (result == MoveResult.ACCIDENT) {
            changePositionGhost1(prevPane);
            accidentChekedInGhost();
        }

    }

    private void accidentChekedInGhost() {
        if (!isMuted) {
            death.setRate(1.1);
            death.play();
        }
        pauseGame();
        Timeline waitOfDeath = new Timeline();
        waitOfDeath.getKeyFrames().add(new KeyFrame(Duration.seconds(1)));
        waitOfDeath.setCycleCount(1);
        waitOfDeath.play();
        waitOfDeath.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameController.getInstance().setPacmanCurrentColumn(15);
                GameController.getInstance().setPacmanCurrentRow(14);
                Pane pane = getPaneInMap(mapPane, 14, 15);
                Objects.requireNonNull(pane).getChildren().add(pacman);
            }
        });

        if (GameController.getInstance().isGameFinished()) {
            try {
                finishGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void changePositionGhost1(Pane prevPane) {
        int row;
        int column;
        row = GameController.getInstance().getGhostRow(1);
        column = GameController.getInstance().getGhostColumn(1);
        Pane currentPane = getPaneInMap(mapPane, row, column);
        if (prevPane != currentPane) {
            Objects.requireNonNull(prevPane).getChildren().remove(ghosts.get(0));
            if (!Objects.requireNonNull(currentPane).getChildren().contains(ghosts.get(0)))
                Objects.requireNonNull(currentPane).getChildren().add(ghosts.get(0));
        }
    }

    private void moveGhost2() {
        int row = GameController.getInstance().getGhostRow(2);
        int column = GameController.getInstance().getGhostColumn(2);
        Pane prevPane = getPaneInMap(mapPane, row, column);
        MoveResult result = GameController.getInstance().moveGhost(2);
        if (result == MoveResult.NOTHING) {
            changePositionGhost2(prevPane);
        } else if (result == MoveResult.ACCIDENT) {
            changePositionGhost2(prevPane);
            accidentChekedInGhost();
        }

    }

    private void changePositionGhost2(Pane prevPane) {
        int row;
        int column;
        row = GameController.getInstance().getGhostRow(2);
        column = GameController.getInstance().getGhostColumn(2);
        Pane currentPane = getPaneInMap(mapPane, row, column);
        if (prevPane != currentPane) {
            Objects.requireNonNull(prevPane).getChildren().remove(ghosts.get(1));
            if (!Objects.requireNonNull(currentPane).getChildren().contains(ghosts.get(1)))
                Objects.requireNonNull(currentPane).getChildren().add(ghosts.get(1));
        }
    }

    private void moveGhost3() {
        int row = GameController.getInstance().getGhostRow(3);
        int column = GameController.getInstance().getGhostRow(3);
        Pane prevPane = getPaneInMap(mapPane, row, column);
        MoveResult result = GameController.getInstance().moveGhost(3);
        if (result == MoveResult.NOTHING) {
            changePositionGhost3(prevPane);
        } else if (result == MoveResult.ACCIDENT) {
            changePositionGhost3(prevPane);
            accidentChekedInGhost();
        }

    }

    private void changePositionGhost3(Pane prevPane) {
        int row;
        int column;
        row = GameController.getInstance().getGhostRow(3);
        column = GameController.getInstance().getGhostColumn(3);
        Pane currentPane = getPaneInMap(mapPane, row, column);
        if (prevPane != currentPane) {
            Objects.requireNonNull(prevPane).getChildren().remove(ghosts.get(2));
            if (!Objects.requireNonNull(currentPane).getChildren().contains(ghosts.get(2)))
                Objects.requireNonNull(currentPane).getChildren().add(ghosts.get(2));
        }
    }

    private void moveGhost4() {
        int row = GameController.getInstance().getGhostRow(4);
        int column = GameController.getInstance().getGhostColumn(4);
        Pane prevPane = getPaneInMap(mapPane, row, column);

        MoveResult result = GameController.getInstance().moveGhost(4);
        if (result == MoveResult.NOTHING) {
            changePositionGhost4(prevPane);
        } else if (result == MoveResult.ACCIDENT) {
            changePositionGhost4(prevPane);
            accidentChekedInGhost();
        }

    }

    private void changePositionGhost4(Pane prevPane) {
        int row;
        int column;
        row = GameController.getInstance().getGhostRow(4);
        column = GameController.getInstance().getGhostColumn(4);
        Pane currentPane = getPaneInMap(mapPane, row, column);
        if (prevPane != currentPane) {
            Objects.requireNonNull(prevPane).getChildren().remove(ghosts.get(3));
            if (!currentPane.getChildren().contains(ghosts.get(3)))
                Objects.requireNonNull(currentPane).getChildren().add(ghosts.get(3));
        }
    }

    public void finishGame() throws IOException {
        pacmanMovementTimeline.pause();
        ghostsTimeLine.pause();
        pacmanMouthMoveTimeline.pause();
        mediaPlayer.pause();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mapPane.getChildren().remove(pacmanMovementTimeline);
        mapPane.getChildren().remove(ghostsTimeLine);
        mapPane.getChildren().remove(pacmanMouthMoveTimeline);
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/StartGameView.fxml")));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Game Finished! your score :" + score);
        GameController.getInstance().checkScore(score);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
        dialogPane.getStyleClass().add("DialogPane");
        alert.show();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) (mapPane).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void muteOrUnMute(ActionEvent actionEvent) {
        if (isMuted) {
            isMuted = false;
            soundButton.setGraphic(unMutedSoundView);
            mediaPlayer.play();
        } else {
            isMuted = true;
            mediaPlayer.pause();
            soundButton.setGraphic(mutedSoundImageView);
        }
    }

    public void exitGame(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Your score will be lost, are you sure you want to quit?");
        DialogPane dialogPane = alert.getDialogPane();
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
        dialogPane.getStyleClass().add("DialogPane");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mediaPlayer.pause();
            pacmanMovementTimeline.stop();
            pacmanMouthMoveTimeline.stop();
            ghostsTimeLine.stop();
            GameController.getInstance().setPacmanCurrentColumn(15);
            GameController.getInstance().setPacmanCurrentRow(14);
            GameController.getInstance().placeGhosts();
            Parent url = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(url);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
    }
}
