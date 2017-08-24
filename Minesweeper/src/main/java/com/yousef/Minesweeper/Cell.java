package com.yousef.Minesweeper;

import java.util.ArrayList;
import java.util.Objects;

public class Cell {

    // the Cells Position

    private final int xCor;
    private final int yCor;

    // the Cells Properties
    private final boolean isMine;
    private boolean isFlagged;
    private boolean isShown;

    private String symbol;
    private String value;

    // the Cell's surroundings
    private Board myBoard;
    private ArrayList<Cell> surroundingCells;

    public Cell(int x, int y, boolean isMine, Board board) {

        this.isMine = isMine;
        this.isFlagged = false;
        this.isShown = false;

        this.myBoard = board;
        surroundingCells = new ArrayList<>();

        this.xCor = x;
        this.yCor = y;
        symbol = "*";

        if (this.isMine) {
            value = "#";
        }
    }

    public boolean isMine() {
        return isMine;
    }

    public int getxCor() {
        return xCor;
    }

    public int getyCor() {
        return yCor;
    }

    public String getSymbol() {
        return symbol;
    }

    public void show() {
        symbol = value;
        isShown = true;
    }

    public boolean isShown() {
        return isShown;
    }

    public ArrayList<Cell> getSurroundingCells() {

        if (surroundingCells.isEmpty() || surroundingCells.contains(null)) {
            setSurroundingCells();
        }

        return surroundingCells;
    }

    private void setSurroundingCells() {
        for (int i = xCor - 1; i <= xCor + 1; i++) {
            for (int j = yCor - 1; j <= yCor + 1; j++) {

                if (i == xCor && j == yCor) {

                } else {
                    if (i >= myBoard.getLength() || j >= myBoard.getWidth() || i < 0 || j < 0) {
                        continue;
                    } else {
                        surroundingCells.add(myBoard.getCellAt(i, j));
                    }
                }
            }
        }
    }

    public String getValue() {
        return value;
    }

    // Gets the surrounding Cells from getSurroundingCells(), then checks the number of mines in the returned ArrayList.

    public void setValue() {

        if (isMine) {
            return;
        }

        if (value == null) {

            int surroundingMines = 0;

            for (Cell cell : getSurroundingCells()) {
                if (cell.isMine()) {
                    surroundingMines++;
                }
            }
            value = Integer.toString(surroundingMines);
        }
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {

        if (isShown) {
            return;
        }

        isFlagged = flagged;

        if (isFlagged) {
            symbol = "F";
        } else if (isFlagged == false) {
            symbol = "*";
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(yCor, xCor, isMine, value);
    }

    @Override
    public boolean equals(Object obj) {
        // if obj is the same object as this, no need to process more
        if (obj == this) {
            return true;
        }

        // if obj is null, then it can't equal this
        // if obj is a different class than this, then they can't be equal
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Cell cell = (Cell) obj;
        return cell.getyCor() == yCor && cell.getxCor() == xCor && cell.isMine() == isMine
                && cell.getValue().equals(value);
    }

    @Override
    public String toString() {
        return "X: " + xCor + " Y: " + yCor + " Value:" + value;
    }

}
