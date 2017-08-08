import java.util.Random;
import java.util.Arrays;

public class Board {

    private final int numOfMines;

    private Cell[][] board;


    public Board(int length, int width, int numOfMines) {
        this.numOfMines = numOfMines;

        board = new Cell[length][width];
        generate();
    }

    private void generate() {
        generateMines();
        generateNumbers();
        printSolvedBoard();
    }

    private void generateMines() {

        int currentNumOfMines = 0;
        Random random = new Random();

        while (currentNumOfMines < numOfMines) {

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {

                    double probability = random.nextDouble();

                    if (board[i][j] == null) {
                        board[i][j] = new Cell(i, j, false, board.clone());
                    } else if (board[i][j].isMine()) {

                    } else if (probability > 0.99999 && currentNumOfMines < numOfMines) {
                        board[i][j] = new Cell(i, j, true, board.clone());
                        currentNumOfMines++;
                    }
                }
            }
        }
    }

    // Calls Cell.setValue() method on every Cell in the board

    private void generateNumbers() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].isMine()) {

                } else {
                    board[i][j].setValue();
                }
            }
        }
    }
    public void printBoard() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].isMine()) {

                }
                System.out.print(" " + board[i][j].getSymbol());
            }
            System.out.println();
        }
    }

    public void printSolvedBoard() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].isMine()) {

                }
                System.out.print(" " + board[i][j].getValue());
            }
            System.out.println();
        }
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
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
        return Arrays.deepEquals(boardObj.getBoard(), board);
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

    public Cell[][] getBoard() {
        return board;
    }
}