package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Cell;
import model.Game;
import model.Map;
import model.User;
import view.game.GameView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class GameController {
    private static GameController instance = null;
    private Game game;
    private Map map;
    private int pacmanCurrentRow;
    private int pacmanCurrentColumn;
    private int pacmanPreviousColumn;
    private int pacmanPreviousRow;
    private GameView view;
    private Direction currentDirection = null;
    private ArrayList<Integer> ghostsRows = new ArrayList<>();

    private ArrayList<Integer> ghostsColumns = new ArrayList<>();

    private ArrayList<Direction> ghostDirections = new ArrayList<>();

    private GameController() {

    }

    public static GameController getInstance() {
        if (instance == null)
            instance = new GameController();
        return instance;
    }

    private void putPointsOnMap() {
        for (Cell[] cells : game.getMap().getArray()) {
            for (Cell cell : cells) {
                if (!cell.isWall()) {
                    cell.setHasPoint(true);
                }
            }
        }
    }

    public void placeGhosts() {
        ghostsColumns.clear();
        ghostDirections.clear();
        ghostsRows.clear();
        ghostsColumns.add(1);
        ghostsColumns.add(1);
        ghostsColumns.add(29);
        ghostsColumns.add(29);
        ghostsRows.add(1);
        ghostsRows.add(29);
        ghostsRows.add(1);
        ghostsRows.add(29);
        ghostDirections.add(Direction.Down);
        ghostDirections.add(Direction.Up);
        ghostDirections.add(Direction.Down);
        ghostDirections.add(Direction.Up);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        placeGhosts();
        this.map = game.getMap();
        this.game = game;
        putPointsOnMap();

    }

    public int getPacmanCurrentColumn() {
        return pacmanCurrentColumn;
    }

    public void setPacmanCurrentColumn(int pacmanCurrentColumn) {
        this.pacmanCurrentColumn = pacmanCurrentColumn;
    }

    public int getPacmanCurrentRow() {
        return pacmanCurrentRow;
    }

    public void setPacmanCurrentRow(int pacmanCurrentRow) {
        this.pacmanCurrentRow = pacmanCurrentRow;

    }

    public MoveResult moveRight() {
        if (isGameFinished()) {
            return MoveResult.GAME_FINISHED;
        } else if (isBoardEmpty()) {
            resetGame();
            return MoveResult.BOARD_EMPTY;
        }
        if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn + 1].isWall()) {
            return MoveResult.IS_WALL;
        } else {
            pacmanPreviousColumn = pacmanCurrentColumn;
            pacmanPreviousRow = pacmanCurrentRow;
            currentDirection = Direction.Right;
            if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn + 1].hasPoint()) {
                pacmanCurrentColumn++;
                view.setCurrentImageView(Direction.Right);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                map.getArray()[pacmanCurrentRow][pacmanCurrentColumn].setHasPoint(false);
                game.addScoreOfPoint();
                return MoveResult.GET_POINT;
            } else if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn + 1].hasBomb()) {
                pacmanCurrentColumn++;
                view.setCurrentImageView(Direction.Right);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                return MoveResult.NOTHING;
            } else {
                pacmanCurrentColumn += 1;
                view.setCurrentImageView(Direction.Right);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                return MoveResult.NOTHING;
            }
        }
    }

    public MoveResult moveLeft() {
        if (isGameFinished()) {
            return MoveResult.GAME_FINISHED;
        } else if (isBoardEmpty()) {
            resetGame();
            //? TODO return;
            return MoveResult.BOARD_EMPTY;
        }
        if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn - 1].isWall()) {
            return MoveResult.IS_WALL;
        } else {
            pacmanPreviousColumn = pacmanCurrentColumn;
            pacmanPreviousRow = pacmanCurrentRow;
            currentDirection = Direction.Left;
            if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn - 1].hasPoint()) {
                pacmanCurrentColumn--;
                view.setCurrentImageView(Direction.Left);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                game.addScoreOfPoint();
                map.getArray()[pacmanCurrentRow][pacmanCurrentColumn].setHasPoint(false);
                return MoveResult.GET_POINT;
            } else if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn - 1].hasBomb()) {
                pacmanCurrentColumn -= 1;
                view.setCurrentImageView(Direction.Left);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                return MoveResult.NOTHING;//TODO
            } else {

                pacmanCurrentColumn--;
                view.setCurrentImageView(Direction.Left);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                return MoveResult.NOTHING;
            }
        }

    }

    public MoveResult moveUp() {
        if (isGameFinished()) {
            return MoveResult.GAME_FINISHED;
        } else if (isBoardEmpty()) {
            resetGame();
            return MoveResult.BOARD_EMPTY;
        }
        if (map.getArray()[pacmanCurrentRow - 1][pacmanCurrentColumn].isWall()) {
            return MoveResult.IS_WALL;
        } else {
            pacmanPreviousColumn = pacmanCurrentColumn;
            pacmanPreviousRow = pacmanCurrentRow;
            currentDirection = Direction.Up;
            if (map.getArray()[pacmanCurrentRow - 1][pacmanCurrentColumn].hasPoint()) {
                pacmanCurrentRow -= 1;
                view.setCurrentImageView(Direction.Up);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                game.addScoreOfPoint();
                map.getArray()[pacmanCurrentRow][pacmanCurrentColumn].setHasPoint(false);
                return MoveResult.GET_POINT;
            } else if (map.getArray()[pacmanCurrentRow - 1][pacmanCurrentColumn].hasBomb()) {
                pacmanCurrentRow--;
                view.setCurrentImageView(Direction.Up);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                return MoveResult.NOTHING;//TODO
            } else {
                pacmanCurrentRow--;
                view.setCurrentImageView(Direction.Up);
                return MoveResult.NOTHING;
            }
        }

    }

    public MoveResult moveDown() {
        if (isGameFinished()) {
            return MoveResult.GAME_FINISHED;
        } else if (isBoardEmpty()) {
            resetGame();
            return MoveResult.BOARD_EMPTY;
        }
        if (map.getArray()[pacmanCurrentRow + 1][pacmanCurrentColumn].isWall()) {
            return MoveResult.IS_WALL;
        } else {
            pacmanPreviousColumn = pacmanCurrentColumn;
            pacmanPreviousRow = pacmanCurrentRow;
            currentDirection = Direction.Down;
            if (map.getArray()[pacmanCurrentRow + 1][pacmanCurrentColumn].hasPoint()) {
                view.setCurrentImageView(Direction.Down);
                pacmanCurrentRow += 1;
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                game.addScoreOfPoint();
                map.getArray()[pacmanCurrentRow][pacmanCurrentColumn].setHasPoint(false);
                return MoveResult.GET_POINT;
            } else if (map.getArray()[pacmanCurrentRow + 1][pacmanCurrentColumn].hasBomb()) {
                pacmanCurrentRow++;
                view.setCurrentImageView(Direction.Down);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                return MoveResult.NOTHING;//TODO
            } else {
                pacmanCurrentRow++;
                view.setCurrentImageView(Direction.Down);
                if (isAccidentBetWeenPacmanAndGhost()) {
                    game.decreasePacManHealth();
                    return MoveResult.ACCIDENT;
                }
                return MoveResult.NOTHING;
            }
        }
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public boolean canUserChangeMove(Direction newDirection) {
        if (newDirection == currentDirection)
            return false;
        switch (newDirection) {
            case Right:
                if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn + 1].isWall())
                    return false;
                break;
            case Left:
                if (map.getArray()[pacmanCurrentRow][pacmanCurrentColumn - 1].isWall())
                    return false;
                break;
            case Down:
                if (map.getArray()[pacmanCurrentRow + 1][pacmanCurrentColumn].isWall())
                    return false;
                break;
            case Up:
                if (map.getArray()[pacmanCurrentRow - 1][pacmanCurrentColumn].isWall())
                    return false;
                break;
        }
        return true;
    }

    public void changeMove(Direction newDirection) {
        currentDirection = newDirection;
    }


    public int getScore() {
        return game.getScore();
    }

    public String getUsername() {
        return game.getUsername();
    }

    private boolean isBoardEmpty() {
        int counter = 0;
        for (int i = 0; i < map.getArray().length; i++) {
            for (int j = 0; j < map.getArray()[i].length; j++) {
                if (!map.getArray()[j][i].hasPoint() && !map.getArray()[j][i].hasBomb()) {
                } else if (!map.getArray()[j][i].isWall())
                    counter++;
            }
        }
        if (counter == 0)
            return true;
        else
            return false;
    }

    public boolean isGameFinished() {
        if (game.getPacmanHealth() == 0)
            return true;
        return false;
    }

    private void resetGame() {
        putPointsOnMap();
    }

    public MoveResult moveGhost(int index) {
        Random random = new Random();
        boolean flag = true;
        while (true) {
            int randomDir = random.nextInt(4);
            randomDir %= 4;
            int row = ghostsRows.get(index - 1);
            int column = ghostsColumns.get(index - 1);
            switch (randomDir) {
                case 0: // Right
                    if (!map.getArray()[row][column + 1].isWall()) {
                        ghostDirections.set(index - 1, Direction.Right);
                        ghostsColumns.set(index - 1, column + 1);
                        if (isAccidentBetweenGhostIAndPacman(index)) {
                            game.decreasePacManHealth();
                            flag = false;
                        }
                        if (flag)
                            return MoveResult.NOTHING;
                        else return MoveResult.ACCIDENT;
                    }
                    break;
                case 1://Up
                    if (!map.getArray()[row - 1][column].isWall()) {
                        ghostDirections.set(index - 1, Direction.Up);
                        ghostsRows.set(index - 1, row - 1);
                        if (isAccidentBetweenGhostIAndPacman(index)) {
                            game.decreasePacManHealth();
                            flag = false;
                        }
                        if (flag)
                            return MoveResult.NOTHING;
                        else return MoveResult.ACCIDENT;
                    }
                    break;
                case 2://Left
                    if (!map.getArray()[row][column - 1].isWall()) {
                        ghostDirections.set(index - 1, Direction.Left);
                        ghostsColumns.set(index - 1, column - 1);
                        if (isAccidentBetweenGhostIAndPacman(index)) {
                            game.decreasePacManHealth();
                            flag = false;
                        }
                        if (flag)
                            return MoveResult.NOTHING;
                        else return MoveResult.ACCIDENT;
                    }
                    break;
                case 3://Down
                    if (!map.getArray()[row + 1][column].isWall()) {
                        ghostDirections.set(index - 1, Direction.Down);
                        ghostsRows.set(index - 1, row + 1);
                        if (isAccidentBetweenGhostIAndPacman(index)) {
                            game.decreasePacManHealth();
                            flag = false;
                        }
                        if (flag)
                            return MoveResult.NOTHING;
                        else return MoveResult.ACCIDENT;
                    }
                    break;
            }
        }
    }

    public int getGhostRow(int index) {
        return ghostsRows.get(index - 1);
    }

    public int getGhostColumn(int index) {
        return ghostsColumns.get(index - 1);
    }

    private boolean isAccidentBetWeenPacmanAndGhost() {
        for (int i = 1; i <= 4; i++) {
            if (isAccidentBetweenGhostIAndPacman(i))
                return true;
        }
        return false;
    }

    private synchronized boolean isAccidentBetweenGhostIAndPacman(int index) {
        if (ghostsRows.get(index - 1) == pacmanCurrentRow) {
            if (ghostsColumns.get(index - 1) == pacmanCurrentColumn) {
                return true;
            }
        }
        return false;
    }

    public void checkScore(int score) {
        if (game.getUsername().equals(""))
            return;
        User user = User.getUserByUsername(game.getUsername());

        if (game.getScore() > user.getScore()) {
            user.setScore(score);
            User.jsonUsers(user);
        }
    }
}