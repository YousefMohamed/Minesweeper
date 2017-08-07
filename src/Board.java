import java.util.Random;
import java.util.Arrays;

public class Board {

    private final int width;
    private final int length;
    private final int numOfMines;

    private Cell[][] board;


    public Board(int length, int width, int numOfMines) {

        this.width = width;
        this.length = length;
        this.numOfMines = numOfMines;

        board = new Cell[length][width];
    }

    public void generate() {
        generateMines();
        generateNumbers();
        printSolvedBoard();
    }

    private void generateMines() {

        int currentNumOfMines = 0;
        Random random = new Random();


        while (currentNumOfMines < numOfMines) {

            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {

                    double probability = random.nextDouble();

                    if (board[i][j] == null) {
                        board[i][j] = new Cell(i, j, false, board);
                    } else if (board[i][j].isMine()) {

                    } else if (probability > 0.99999 && currentNumOfMines < numOfMines) {
                        board[i][j] = new Cell(i, j, true, board);
                        currentNumOfMines++;
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return width * length * numOfMines + 13;
    }

    @Override
    public boolean equals(Object obj) {
        Board boardObj = (Board) obj;
        return Arrays.deepEquals(boardObj.getBoard(), board);
    }

    public void printBoard() {

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j].isMine()) {

                }
                System.out.print(" " + board[i][j].getSymbol());
            }
            System.out.println();
        }
    }

    public void printSolvedBoard() {

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j].isMine()) {

                }
                System.out.print(" " + board[i][j].getValue());
            }
            System.out.println();
        }
    }

    private void generateNumbers() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j].isMine()) {

                } else {
                    board[i][j].setValue();
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    public Cell[][] getBoard() {
        return board;
    }
}