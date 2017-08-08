import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Cell {

    // the Cells Position

    private final int xPos;
    private final int yPos;

    // the Cells Properties
    private final boolean isMine;
    private boolean isFlagged;
    private boolean isShown;

    private String symbol;
    private String value;

    // the Cell's surroundings

    private Cell[][] myBoard;
    private ArrayList<Cell> surroundingCells;

    public Cell(int x, int y, boolean isMine, Cell[][] board) {

        this.isMine = isMine;
        this.isFlagged = false;
        this.isShown = false;

        this.myBoard = board;
        surroundingCells = new ArrayList<>();

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

    public ArrayList<Cell> getSurroundingCells() {

        if (surroundingCells.isEmpty()) {
            setSurroundingCells();
        }

        return surroundingCells;
    }

    private void setSurroundingCells() {
        for (int i = xPos - 1; i <= xPos + 1; i++) {
            for (int j = yPos - 1; j <= yPos + 1; j++) {

                if (i == xPos && j == yPos) {

                } else {
                    try {
                        surroundingCells.add(myBoard[i][j]);
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


    // Gets the surrounding Cells from getSurroundingCells(), then checks the number of mines in the returned ArrayList.

    public void setValue() {

        if (isMine) {
            return;
        }

        // if the board contains null then the method will exist (to avoid errors)

        if (Arrays.asList(myBoard).contains(null)) {
            return;
        }

        int surroundingMines = 0;

        for (Cell cell : getSurroundingCells()) {
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

        if (isShown) {
            return;
        } else if (isFlagged) {
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
        return Objects.hash(yPos, xPos, isMine, value);
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
        return cell.getyPos() == yPos && cell.getxPos() == xPos && cell.isMine() == isMine && cell.getValue().equals(value);
    }

    @Override
    public String toString() {
        return "X: " + xPos + " Y: " + yPos + " Value:" + value;
    }

}
