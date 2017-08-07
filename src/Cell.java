import java.util.ArrayList;
import java.util.Arrays;

public class Cell {

    private final int xPos;
    private final int yPos;

    private final boolean isMine;
    private boolean isFlagged;
    private boolean isShown;

    private String symbol;
    private String value;

    private Cell[][] myBoard;
    private ArrayList<Cell> surroundingTiles;

    public Cell(int x, int y, boolean isMine, Cell[][] board) {

        this.isMine = isMine;
        this.isFlagged = false;
        this.isShown = false;

        this.myBoard = board;
        surroundingTiles = new ArrayList<>();

        this.xPos = x;
        this.yPos = y;
        symbol = "*";

        if (this.isMine) {
            value = "#";
        }
    }

    public boolean isMine() {
        return isMine;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
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

    public boolean hasValue() {

        if (isMine == true) return false;

        return Integer.parseInt(value) > 0;
    }

        /*    1 2 3 4
            1 * * * *
		    2 * * * *
	    	3 * * * *
		    4 * * * *

		Tile: 3,3

		1st position: i-1, j-1 (2,2), 2nd position: i-1, j (2,3), 3rd position: i-1, j+1 (2,4), 4th position: i, j-1 (3,2)
		5th position: i, j+1 (3,4), 6th position: i+1, j-1 (4,2), 7th position: i+1, j (4,3), 8th position: i+1, j+1 (4,4)  */

    public ArrayList<Cell> getSurroundingTiles() {

        if (surroundingTiles.isEmpty()) {
            setSurroundingTiles();
        }

        return surroundingTiles;
    }

    private void setSurroundingTiles() {
        for (int i = xPos - 1; i <= xPos + 1; i++) {
            for (int j = yPos - 1; j <= yPos + 1; j++) {

                if (i == xPos && j == yPos) {

                } else {
                    try {
                        surroundingTiles.add(myBoard[i][j]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue() {

        if (isMine) {
            return;
        }

        if (Arrays.asList(myBoard).contains(null)) {
            return;
        }

        int surroundingMines = 0;

        if (surroundingTiles.isEmpty()) {
            setSurroundingTiles();
        }

        for (Cell cell : surroundingTiles) {
            if (cell.isMine()) {
                surroundingMines++;
            }
        }

        value = Integer.toString(surroundingMines);
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
        if (flagged) {
            symbol = "F";
        } else {
            if (isMine) {
                symbol = "#";
            } else if (isShown) {
                symbol = value;
            } else {
                symbol = "*";
            }
        }
    }

    @Override
    public int hashCode() {
        return xPos * yPos * symbol.hashCode() * value.hashCode() * 29 + 6;
    }

    @Override
    public boolean equals(Object obj) {
        Cell cell = (Cell) obj;
        return cell.getyPos() == yPos && cell.getxPos() == xPos && cell.isMine() == isMine && cell.getValue().equals(value);
    }

    @Override
    public String toString() {
        return "X: " + xPos + " Y: " + yPos + " Value:" + value;
    }

}
