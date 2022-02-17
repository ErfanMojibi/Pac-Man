package model;

public class Cell {
    private char cell;
    private boolean hasPoint = false;
    private boolean hasEnergyBomb = false;
    private boolean isWall = false;
    private int row;
    private int column;

    public Cell(char cell) {
        this.cell = cell;
    }

    public Cell(char cell, boolean isWall, int row, int column) {
        this.cell = cell;
        this.isWall = isWall;
        this.row = row;
        this.column = column;
    }

    public char getInCell() {
        return cell;
    }

    public void setHasEnergyBomb(boolean hasEnergyBomb) {
        this.hasEnergyBomb = hasEnergyBomb;
    }

    public void setHasPoint(boolean hasPoint) {
        this.hasPoint = hasPoint;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean hasBomb() {
        return hasEnergyBomb;
    }

    public boolean hasPoint() {
        return hasPoint;
    }
}
