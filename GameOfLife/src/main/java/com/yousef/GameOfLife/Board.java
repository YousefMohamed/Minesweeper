package com.yousef.GameOfLife;

import java.util.Random;
import java.util.ArrayList;

public class Board {

	Cell[][] board;
	Cell[][] swapBoard;

	public Board() {
		board = randomBoard();
	}

	public Board(Cell[][] board) {
		this.board = board;
	}

	private Cell[][] cloneBoard(Cell[][] originalBoard) {

		Cell[][] clonedBoard = new Cell[originalBoard.length][originalBoard[0].length];

		for (int i = 0; i < originalBoard.length; i++) {
			for (int j = 0; j < originalBoard[0].length; j++) {
				clonedBoard[i][j] = originalBoard[i][j].clone();
			}
		}
		return clonedBoard;
	}

	public Cell[][] randomBoard() {
		Cell[][] randomBoard = new Cell[20][20];
		int limit = 300;
		int numOfAlives = 0;
		Random random = new Random();
		while (numOfAlives < limit) {
			for (int i = 0; i < randomBoard.length; i++) {
				for (int j = 0; j < randomBoard[0].length; j++) {
					if (randomBoard[i][j] == null) {
						if (random.nextDouble() > 0.999 && numOfAlives < limit) {
							randomBoard[i][j] = new Cell(true, i, j);
							numOfAlives++;
						} else {
							randomBoard[i][j] = new Cell(false, i, j);
						}
					} else if (randomBoard[i][j].isAlive()) {
					} else {
						if (random.nextDouble() > 0.9 && numOfAlives < limit) {
							randomBoard[i][j] = new Cell(true, i, j);
							numOfAlives++;
						} else {
							randomBoard[i][j] = new Cell(false, i, j);
						}
					}
				}
			}
		}
		return randomBoard;

	}

	public void update() {

		System.out.print("\033[H\033[2J");
		System.out.flush();

		updateArrays();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				System.out.print(" " + board[i][j].getSymbol());
			}
			System.out.println();
		}
	}

	private void updateArrays() {
		swapBoard = cloneBoard(board);

		for (int i = 0; i < swapBoard.length; i++) {
			for (int j = 0; j < swapBoard[0].length; j++) {
				board[i][j].setAlive(getSurroundingCells(board[i][j]));
			}
		}
	}

	private ArrayList<Cell> getSurroundingCells(Cell cell) {
		ArrayList<Cell> surroundingCells = new ArrayList<>();

		for (int i = cell.getxCor() - 1; i <= cell.getxCor() + 1; i++) {
			for (int j = cell.getyCor() - 1; j <= cell.getyCor() + 1; j++) {
				if (i == cell.getxCor() && j == cell.getyCor()) {
					continue;
				}
				try {
					surroundingCells.add(swapBoard[i][j]);
				} catch (IndexOutOfBoundsException e) {

				}
			}
		}
		return surroundingCells;
	}
}