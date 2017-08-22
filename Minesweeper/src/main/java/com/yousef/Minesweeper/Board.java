package com.yousef.Minesweeper;

import java.util.Random;
import java.util.Objects;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Board {

    private final int numOfMines;
    private final Cell[][] board;
    private boolean hasWon;
    private boolean hasLost;

    private Board(int length, int width, int numOfMines) {

        this.numOfMines = numOfMines;
        board = new Cell[length][width];

        System.out.println("Creating Board.");

        setMines();
        setCellsValues();
        printBoard();
    }

    public static Board createBoard(int length, int width, int numOfMines) throws IllegalArgumentException {

        if (length > 1 && width > 1 && numOfMines >= 0 && numOfMines >= (length * width)) {
            throw new IllegalArgumentException();
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

    public void printBoard() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (hasWon || hasLost) {
                    System.out.print(" " + board[i][j].getValue());
                } else {
                    System.out.print(" " + board[i][j].getSymbol());
                }
            }
            System.out.println();
        }
    }

    private void reveal(Cell originalCell, Queue<Cell> queue, ArrayList<Cell> processed) {

        originalCell.show();
        queue.add(originalCell);
        processed.add(originalCell);

        while (!queue.isEmpty()) {

            Cell currentCell = queue.remove();
            currentCell.show();

            for (Cell cell : currentCell.getSurroundingCells()) {

                if (cell.isMine()) {

                } else if (cell.isShown()) {

                } else if (cell.isFlagged()) {

                } else if (processed.contains(cell)) {

                } else if (Integer.parseInt(cell.getValue()) == 0) {
                    queue.add(cell);
                    processed.add(cell);
                } else if (Integer.parseInt(cell.getValue()) > 0) {
                    cell.show();
                }
            }

        }
        /*
        if (queue.isEmpty()) {
            for (Cell cell2 : surroundingCells) {
                if (processed.contains(cell2)) {
        
                } else if (!cell2.hasValue() && !cell.isMine()) {
                    queue.add(cell2);
                } else if (cell2.hasValue()) {
                    cell2.show();
                }
            }
        
            if (queue.isEmpty()) {
                return;
            } else {
                reveal(queue.get(0), queue, processed);
                return;
            }
        
        } else {
            for (Cell cell2 : cell.getSurroundingCells()) {
                if (queue.contains(cell2)) {
        
                } else if (processed.contains(cell2)) {
        
                } else if (!cell2.hasValue() && !cell2.isMine()) {
                    queue.add(cell2);
                } else if (cell2.hasValue()) {
                    cell2.show();
                } else if (cell2.isShown()) {
        
                } else if (cell2.isMine()) {
        
                }
            }
        
            processed.add(cell);
            queue.remove(cell);
        
            if (queue.isEmpty()) {
                return;
            } else {
                reveal(queue.get(0), queue, processed);
                return;
            }
        }*/
    }

    public void check(int row, int column) {

        if (hasWon || hasLost) {
            return;
        }

        if (row > board.length || column > board[0].length) {
            System.out.println("Invalid Input.");
            return;
        }

        Cell cell = board[row][column];

        if (cell.isMine()) {

            cell.show();

            hasLost = true;
            System.out.println("You Lose.");

        } else if (cell.isShown()) {

            System.out.println("Already revealed.");

        } else if (Integer.parseInt(cell.getValue()) > 0) {
            cell.show();

            checkWinConditions();
            if (hasWon) {

                System.out.println("You Win!");
            }

        } else if (Integer.parseInt(cell.getValue()) == 0) {

            reveal(cell, new ArrayDeque<Cell>(), new ArrayList<>());

            checkWinConditions();
            if (hasWon) {
                System.out.println("You Win!");
            }
        }
    }

    // sets isFlagged to true/false depending on its state

    void flag(int row, int column) {

        if (hasLost || hasWon) {
            return;
        }

        if (row > board.length || column > board[0].length) {
            System.out.println("Invalid Input.");
            return;
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

    public Cell getCellAt(int row, int column) {
        return board[row][column];
    }

    private void checkWinConditions() {

        if (hasLost || hasWon) {
            return;
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].isMine()) {
                    continue;
                } else if (board[i][j].isShown()) {
                    continue;
                } else if (!board[i][j].isShown()) {
                    hasWon = false;
                    break;
                }
            }
        }

    }
}
