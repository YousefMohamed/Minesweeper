package com.yousef.GameOfLife;

import java.util.ArrayList;
import java.util.Objects;

public class Cell implements Cloneable {
	private boolean isAlive;
	private char symbol;
	private int xCor;
	private int yCor;

	public Cell(boolean isAlive, int xCor, int yCor) {
		this.isAlive = isAlive;
		this.xCor = xCor;
		this.yCor = yCor;

		setSymbol();
	}

	public boolean isAlive() {
		return isAlive;
	}

	public int getxCor() {
		return xCor;
	}

	public int getyCor() {
		return yCor;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setAlive(ArrayList<Cell> surroundingCells) {

		int aliveCells = 0;
		for (Cell cell : surroundingCells) {
			if (cell.isAlive()) {
				aliveCells++;
			}
		}
		if (isAlive) {
			if (aliveCells == 3 || aliveCells == 2) {
			} else if (aliveCells > 3) {
				isAlive = false;
			} else if (aliveCells < 2) {
				isAlive = false;
			}
		} else if (!isAlive) {
			if (aliveCells == 3) {
				isAlive = true;
			}
		}
		setSymbol();
	}

	private void setSymbol() {
		if (isAlive) {
			symbol = '1';
		} else if (!isAlive) {
			symbol = ' ';
		}
	}

	@Override
	public String toString() {
		return Character.toString(symbol);
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

		return isAlive == cell.isAlive() && xCor == cell.xCor && yCor == cell.yCor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol, isAlive, xCor, yCor);
	}

	public Cell clone() {
		return new Cell(isAlive, xCor, yCor);
	}
}