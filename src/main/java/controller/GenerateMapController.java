package controller;

import model.Cell;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GenerateMapController {
    private static GenerateMapController instance = null;
    public char mazeMap[][];
    public char visitedPoints[][];

    private GenerateMapController() {

    }

    public static GenerateMapController getInstance() {
        if (instance == null)
            instance = new GenerateMapController();
        return instance;
    }

    public Cell[][] generateMap() {
        Scanner scanner = new Scanner(System.in);
        int rowsNumber, columnsNumber, numberOfMazes;
        rowsNumber = 15;
        columnsNumber = 15;
        numberOfMazes = 1;
        Random random = new Random();
        mazeMap = new char[2 * rowsNumber + 1][2 * columnsNumber + 1];
        visitedPoints = new char[2 * rowsNumber + 1][2 * columnsNumber + 1];
        clear(rowsNumber, columnsNumber);
        initializeMap(rowsNumber, columnsNumber);
        for (int i = 1; i <= numberOfMazes; i++) {
            makeRandomPath(1, 1, random);

        }
        for (int i = 0; i < 150; i++) {
            int columnRand = random.nextInt(30);
            int rowRand = random.nextInt(30);
            if (mazeMap[rowRand][columnRand] == '1')
                mazeMap[rowRand][columnRand] = '0';
        }
        for (int i = mazeMap.length - 1; i >= 0; i--) {
            for (int i1 = mazeMap[i].length - 1; i1 >= 0; i1--) {
                if (mazeMap[i][i1] == '*') {
                    mazeMap[i][i1] = '0';
                } else if (mazeMap[i][i1] == '2') {
                    mazeMap[i][i1] = '1';
                }
            }
        }
        Cell[][] theMap = new Cell[31][31];
        for (int i = 0; i <= 30; i++) {
            for (int j = 0; j <= 30; j++) {
                if (mazeMap[j][i] == '1') {
                    theMap[j][i] = new Cell(mazeMap[j][i], true, j, i);
                } else {
                    theMap[j][i] = new Cell(mazeMap[j][i], false, j, i);
                }
            }
        }
        return theMap;
    }

    private void clear(int rowsNumber, int columnsNumber) {
        for (int i = 0; i < 2 * rowsNumber + 1; i++)
            for (int j = 0; j < 2 * columnsNumber + 1; j++)
                visitedPoints[i][j] = '0';
    }

    private void initializeMap(int rowsNumber, int columnsNumber) {
        for (int i = 0; i < 2 * rowsNumber + 1; i++) {
            for (int j = 0; j < 2 * columnsNumber + 1; j++) {
                if (i == 0) mazeMap[i][j] = '2';
                else if (j == 0) mazeMap[i][j] = '2';
                else if (i == 2 * rowsNumber) mazeMap[i][j] = '2';
                else if (j == 2 * columnsNumber) mazeMap[i][j] = '2';
                else {
                    if (i % 2 == 1 && j % 2 == 1) mazeMap[i][j] = '*';
                    else mazeMap[i][j] = '1';
                }
            }
        }
        mazeMap[0][1] = '2';
        mazeMap[2 * rowsNumber][2 * columnsNumber - 1] = '2';
    }

    private void makeRandomPath(int row, int column, Random random) {
        visitedPoints[row][column] = 'x';
        char[] around = checkAround(row, column);
        outer:
        while (true) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0:
                    if (around[0] == '+') {
                        mazeMap[row][column + 1] = '0';
                        makeRandomPath(row, column + 2, random);
                    }
                    break;
                case 1:
                    //around = checkAround(row, column);
                    if (around[1] == '+') {
                        mazeMap[row - 1][column] = '0';
                        makeRandomPath(row - 2, column, random);
                    }
                    break;
                case 2:
                    //around = checkAround(row, column);
                    if (around[2] == '+') {
                        mazeMap[row][column - 1] = '0';
                        makeRandomPath(row, column - 2, random);
                    }
                    break;
                case 3:
                    //around = checkAround(row, column);
                    if (around[3] == '+') {
                        mazeMap[row + 1][column] = '0';
                        makeRandomPath(row + 2, column, random);
                    }
                    break;
                default:
                    break;
            }
            around = checkAround(row, column);
            for (char c : around)
                if (c == '+')
                    continue outer;
            break;
        }
    }

    private char[] checkAround(int row, int column) {
        char[] around = new char[4];
        Arrays.fill(around, '-');
        if (mazeMap[row][column + 1] == '1' && visitedPoints[row][column + 2] != 'x')
            around[0] = '+';
        if (mazeMap[row - 1][column] == '1' && visitedPoints[row - 2][column] != 'x')
            around[1] = '+';
        if (mazeMap[row][column - 1] == '1' && visitedPoints[row][column - 2] != 'x')
            around[2] = '+';
        if (mazeMap[row + 1][column] == '1' && visitedPoints[row + 2][column] != 'x')
            around[3] = '+';
        return around;
    }
}
