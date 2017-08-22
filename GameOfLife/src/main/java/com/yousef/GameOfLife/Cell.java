package com.yousef.GameOfLife;

import java.util.ArrayList;
import java.util.Objects;

public class Cell {

	private boolean currentState;
	private boolean previousState;
	
	private char symbol;

	private boolean hasChanged;

	private int xCor;
	private int yCor;

	public Cell(boolean currentState, int xCor, int yCor) {
		this.currentState = currentState;
		this.xCor = xCor;
		this.yCor = yCor;

		setSymbol();
	}

	public void setAlive(ArrayList<Cell> surroundingCells) {

		int aliveCells = 0;

		for (Cell cell : surroundingCells) {
			if (cell.hasChanged()) {
				if (cell.previousState()) {
					aliveCells++;
				}
			} else if (!cell.hasChanged()) {
				if (cell.currentState()) {
					aliveCells++;
				}
			}
		}

		previousState = currentState;

		if (currentState) {
			if (aliveCells == 3 || aliveCells == 2) {
				hasChanged = false;
			} else if (aliveCells > 3) {
				currentState = false;
				hasChanged = true;
			} else if (aliveCells < 2) {
				currentState = false;
				hasChanged = true;
			} else {
				hasChanged = false;
			}
		} else if (!currentState) {
			if (aliveCells == 3) {
				currentState = true;
				hasChanged = true;
			} else {
				hasChanged = false;
			}
		}

		if (hasChanged) {
			setSymbol();
		}
	}

	public void reset() {
		previousState = false;
		hasChanged = false;
	}

	private void setSymbol() {
		if (currentState) {
			symbol = '1';
		} else if (!currentState) {
			symbol = ' ';
		}
	}

	public boolean previousState() {
		return previousState;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	
	public boolean currentState() {
		return currentState;
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

		return currentState == cell.currentState() && xCor == cell.xCor && yCor == cell.yCor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol, currentState, xCor, yCor);
	}
}
