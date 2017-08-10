import java.io.IOException;
import java.util.*;

public class Game {

    Board board;

    public Game(int length, int width, int numOfMines) {

        help();

        // First click isn't guaranteed to bo safe
        // TODO: create the board when the user makes his first choice.
        board = new Board(length, width, numOfMines);

        start();
    }

    public void start() {
        while (true) {
            userInput();
        }
    }

    public static void main(String[] args) {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException e) {
        } catch (InterruptedException exception) {
        }

        Scanner scan = new Scanner(System.in);
        try {
            System.out.print("Length: ");
            int length = scan.nextInt();

            System.out.print("Width: ");
            int width = scan.nextInt();

            System.out.print("Number Of Mines: ");

            int numOfMines = scan.nextInt();

            if (numOfMines > (length * width)) {
                System.out.println("Invalid  Input. (Number of Mines > (Length*Width))");
                main(new String[0]);
            }

            System.out.print("\033[H\033[2J");
            System.out.flush();

            new Game(length, width, numOfMines);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, Please enter a number.");
            main(new String[0]);
        }

    }


    public void help() {
        System.out.println();
        System.out.println("Commands:");
        System.out.println("           Help: Opens the help menu.");
        System.out.println("           Check: Specify the Cell you want to check.");
        System.out.println("           Flag: Specify the Cell you want to flag.");
        System.out.println("		   Restart: Start a new Game.");
        System.out.println("		   Quit/Exist: Quits the current Game.");
        System.out.println();
    }

    void userInput() {
        Scanner scan = new Scanner(System.in);

        System.out.print("$ ");
        String userInput = scan.nextLine().trim().toLowerCase();

        switch (userInput) {
            case "help":

                help();
                break;

            case "check":
                if (board != null) {
                    System.out.print("Row: ");
                    int row = scan.nextInt() - 1;

                    System.out.print("Column: ");
                    int column = scan.nextInt() - 1;

                    if (row > board.getLength() || column > board.getWidth()) {
                        System.out.println("Invalid Input.");
                        userInput();
                    }

                    check(row, column);
                    if (board != null) {
                        board.printBoard();
                    }
                    break;
                } else {
                    System.out.println("Please start a new Game.");
                    userInput();
                }
            case "restart":

                try {
                    System.out.print("Length: ");
                    int length = scan.nextInt() - 1;

                    System.out.print("Width: ");
                    int width = scan.nextInt() - 1;

                    System.out.print("Number Of Mines: ");

                    int numOfMines = scan.nextInt();

                    if (numOfMines > (length * width)) {
                        System.out.println("Invalid  Input. (Number of Mines > (Length*Width))");
                        userInput();
                    }

                    restart(length, width, numOfMines);

                } catch (InputMismatchException e) {
                    System.out.println("Invalid input, Please enter a number.");
                    userInput();
                }

                break;

            case "flag":
                if (board != null) {
                    System.out.print("Row: ");
                    int row = scan.nextInt() - 1;

                    System.out.print("Column: ");
                    int column = scan.nextInt() - 1;

                    if (row > board.getLength() || column > board.getWidth()) {
                        System.out.println("Invalid Input.");
                        userInput();
                    }

                    flag(row, column);
                    board.printBoard();
                    break;
                } else {
                    System.out.println("Please start a new Game.");
                    userInput();
                }
            case "quit":
                scan.close();
                System.exit(0);
            case "exist":
                scan.close();
                System.exit(0);
            case "":
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    void check(int row, int column) {

        if (row > board.getLength() || column > board.getWidth()) {
            System.out.println("Invalid Input.");
            return;
        }

        Cell cell = board.getBoard()[row][column];

        if (cell.isMine()) {
            cell.show();
            board.printSolvedBoard();
            System.out.println("You Lose.");
            board = null;
        } else if (cell.isShown()) {
            System.out.println("Already revealed.");
        } else if (cell.hasValue()) {
            cell.show();
            if (checkWinConditions()) {
                System.out.println("You Win!");
                board.printSolvedBoard();
                board = null;
            }
        } else if (!cell.hasValue()) {
            reveal(cell, new ArrayList<>(), new ArrayList<>());
            if (checkWinConditions()) {
                System.out.println("You Win!");
                board.printSolvedBoard();
                board = null;
            }
        }
    }


    public void reveal(Cell cell, ArrayList<Cell> queue, ArrayList<Cell> processed) {

        cell.show();

        if (queue.isEmpty()) {
            ArrayList<Cell> surroundingCells = cell.getSurroundingCells();
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
        }
    }

    // sets isFlagged to true/false depending on its state

    void flag(int row, int column) {

        if (row > board.getLength() || column > board.getWidth()) {
            System.out.println("Invalid Input.");
            return;
        }

        Cell cell = board.getBoard()[row][column];
        cell.setFlagged(!cell.isFlagged());

    }

    boolean checkWinConditions() {

        boolean won = true;
        Cell[][] board2 = board.getBoard();

        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board2[i][j].isMine()) {
                    continue;
                } else if (board2[i][j].isShown()) {
                    continue;
                } else if (!board2[i][j].isShown()) {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }

    // creates a new board

    void restart(int length, int width, int numOfMines) {

        System.out.print("\033[H\033[2J");
        System.out.flush();

        board = new Board(length, width, numOfMines);
    }
}
