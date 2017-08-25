package com.yousef.Minesweeper;

import java.util.Random;
import java.util.Objects;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Board {

    private final int numOfMines;
    private final Cell[][] board;
    private boolean gameEnded;

    private Board(int length, int width, int numOfMines) {

        gameEnded = false;
        this.numOfMines = numOfMines;

        board = new Cell[length][width];

        System.out.println("Creating Board.");

        setMines();
        setCellsValues();
    }

    public static Board createBoard(int length, int width, int numOfMines) {

        if (length > 1 && width > 1 && numOfMines >= 0 && numOfMines >= (length * width)) {
            throw new IllegalArgumentException("Length * Width <= Number Of Mines");
        }

        return new Board(length, width, numOfMines);
    }

    private void setMines() {

        int currentNumOfMines = 0;
        Random random = new Random();

        while (currentNumOfMines < numOfMines) {

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {

                    double probability = random.nextDouble();

                    if (board[i][j] == null) {
                        if (probability > 0.99999 && currentNumOfMines < numOfMines) {
                            board[i][j] = new Cell(i, j, true, this);
                            currentNumOfMines++;
                        } else {
                            board[i][j] = new Cell(i, j, false, this);
                        }
                    } else if (board[i][j].isMine()) {

                    } else if (probability > 0.99999 && currentNumOfMines < numOfMines) {
                        board[i][j] = new Cell(i, j, true, this);
                        currentNumOfMines++;
                    }
                }
            }
        }
    }

    // Calls Cell.setValue() method on every Cell in the board

    private void setCellsValues() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j].setValue();
            }
        }
    }

    public String toString() {

        StringBuilder currentBoardRepresentation = new StringBuilder().append("\n");

        if (gameEnded) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    currentBoardRepresentation.append(" " + board[i][j].getValue());
                }
                currentBoardRepresentation.append("\n");
            }
        } else {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    currentBoardRepresentation.append(" " + board[i][j].getSymbol());
                }
                currentBoardRepresentation.append("\n");
            }
        }

        return currentBoardRepresentation.toString();
    }

    private void show(Cell originalCell, Queue<Cell> queue, ArrayList<Cell> processed) {

        originalCell.show();
        queue.add(originalCell);
        processed.add(originalCell);

        while (!queue.isEmpty()) {

            Cell currentCell = queue.remove();
            currentCell.show();

            for (Cell cell : currentCell.getSurroundingCells()) {
                if (Integer.parseInt(cell.getValue()) == 0 && !cell.isMine() && !cell.isShown() && !cell.isFlagged()
                        && !processed.contains(cell)) {
                    queue.add(cell);
                    processed.add(cell);
                } else if (Integer.parseInt(cell.getValue()) > 0) {
                    cell.show();
                }
            }

        }
    }

    public void check(int row, int column) {

        if (gameEnded) {
            return;
        }

        if (row >= board.length || column >= board[0].length || row < 0 || column < 0) {
            throw new IllegalArgumentException("Specified row/column is bigger than the length/width of the board.");
        }

        Cell cell = board[row][column];

        if (cell.isMine()) {

            cell.show();

            gameEnded = true;
            System.out.println("You Lose.");
            return;

        } else if (cell.isShown()) {
            System.out.println("Already Shown.");
        } else if (Integer.parseInt(cell.getValue()) > 0) {
            cell.show();
        } else if (Integer.parseInt(cell.getValue()) == 0) {
            show(cell, new ArrayDeque<Cell>(), new ArrayList<>());
        }

        checkWinConditions();
        if (gameEnded) {
            System.out.println("You Win!");
        }

    }

    // sets isFlagged to true/false depending on its state

    public void flag(int row, int column) {

        if (gameEnded) {
            return;
        }

        if (row >= board.length || column >= board[0].length || row < 0 || column < 0) {
            throw new IllegalArgumentException("Specified row/column is bigger than the length/width of the board.");
        }

        Cell cell = board[row][column];
        cell.setFlagged(!cell.isFlagged());
    }

    @Override
    public int hashCode() {
        return Objects.hash(board.length, board[0].length, numOfMines, board);
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

        Board boardObj = (Board) obj;

        if (boardObj.getWidth() != board[0].length) {
            return false;
        }
        if (boardObj.getLength() != board.length) {
            return false;
        }
        if (boardObj.getNumOfMines() != numOfMines) {
            return false;
        }

        for (int i = 0; i < boardObj.getLength(); i++) {
            for (int j = 0; j < boardObj.getWidth(); j++) {
                if (!boardObj.getCellAt(i, j).equals(board[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getWidth() {
        return board[0].length;
    }

    public int getLength() {
        return board.length;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    public Cell getCellAt(int row, int column) throws IndexOutOfBoundsException {
        return board[row][column];
    }

    private void checkWinConditions() {

        if (gameEnded) {
            return;
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].isMine()) {
                    continue;
                } else if (board[i][j].isShown()) {
                    continue;
                } else if (!board[i][j].isShown()) {
                    gameEnded = false;
                    return;
                }
            }
        }

        gameEnded = true;
    }

    public boolean gameEnded() {
        return gameEnded;
    }
}
