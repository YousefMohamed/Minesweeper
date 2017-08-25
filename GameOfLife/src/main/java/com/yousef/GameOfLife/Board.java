package com.yousef.GameOfLife;

import java.util.Random;
import java.io.IOException;
import java.net.URL;

public class Board implements Runnable {

	private Cell[][] board;

	public Board(Cell[][] board) {
		this.board = board;
	}

	public Board() {
		board = randomBoard();
	}

	public Board(String path) throws IOException {
		board = BoardReader.createArray(path, this);
	}

	public Board(URL path) throws IOException {
		board = BoardReader.createArray(path, this);
	}

	public Cell[][] randomBoard() {
		Cell[][] randomBoard = new Cell[60][60];

		int numOfAlives = 0;
		Random random = new Random();

		while (numOfAlives < 3000) {
			for (int i = 0; i < randomBoard.length; i++) {
				for (int j = 0; j < randomBoard[0].length; j++) {

					if (randomBoard[i][j] == null) {

						if (random.nextDouble() > 0.999 && numOfAlives < 3000) {
							randomBoard[i][j] = new Cell(true, i, j, this);
							numOfAlives++;
						} else {
							randomBoard[i][j] = new Cell(false, i, j, this);
						}

					} else if (randomBoard[i][j].currentState()) {
					} else {

						if (random.nextDouble() > 0.999 && numOfAlives < 3000) {
							randomBoard[i][j] = new Cell(true, i, j, this);
							numOfAlives++;
						} else {
							randomBoard[i][j] = new Cell(false, i, j, this);
						}

					}
				}
			}
		}

		return randomBoard;
	}

	private void cleanup() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j].reset();
			}
		}
	}

	private void updateArrays() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j].setAlive();
			}
		}
	}

	public void run() {
		System.out.print("\033[H\033[2J");
		System.out.flush();

		updateArrays();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				sb.append(" " + board[i][j].getSymbol());
			}
			sb.append("\n");
		}
		System.out.print(sb.toString());

		cleanup();
	}

	public int getLength() {
		return board.length;
	}

	public int getWidth() {
		return board[0].length;
	}

	public Cell getCellAt(int row, int column) {
		return board[row][column];
	}
}
