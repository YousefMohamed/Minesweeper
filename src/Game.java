import java.util.*;

public class Game {

    Board board;

    public Game(int length, int width, int numOfMines) {

        board = new Board(length, width, numOfMines);

        help();

        // First click isn't guaranteed to bo safe
        // TODO: generate the board when the user makes his first choice ( to make sure it's guaranteed to be safe )

        System.out.println("Generating Board");

        start();

    }

    public void start() {
        while (true) {
            userInput();
        }
    }

    public static void main(String[] args) {

        // currently doesn't check if  numOfMines > (length*width)

        Scanner scan = new Scanner(System.in);
        System.out.print("Length: ");
        int length = scan.nextInt();

        System.out.print("Width: ");
        int width = scan.nextInt();

        System.out.print("Number Of Mines: ");
        int numOfMines = scan.nextInt();

        new Game(length, width, numOfMines);
    }


    public void help() {
        System.out.println();
        System.out.println("Commands:");
        System.out.println("           \"help\" opens the help menu");
        System.out.println("           \"choose\" specify which tile you want to check");
        System.out.println("           \"flag\" specify which tile you want to flag");
        System.out.println("		   \"restart\" start a new game");
        System.out.println("		   \"quit\" to quit the game");
        System.out.println();
    }

    void userInput() {
        Scanner scan = new Scanner(System.in);

        System.out.print("$ ");
        String userInput;

        userInput = scan.nextLine();
        userInput = userInput.trim().toLowerCase();

        int row;
        int column;

        switch (userInput) {
            case "help":

                help();
                break;

            case "choose":

                System.out.print("Row: ");
                row = scan.nextInt() - 1;

                System.out.print("Column: ");
                column = scan.nextInt() - 1;

                choose(row, column);
                board.printBoard();
                break;

            case "restart":

                // currently doesn't check if  numOfMines > (length*width)
                System.out.print("Length: ");
                int length = scan.nextInt();

                System.out.print("Width: ");
                int width = scan.nextInt();

                System.out.print("Number Of Mines: ");
                int numOfMines = scan.nextInt();

                restart(length, width, numOfMines);
                break;

            case "flag":

                System.out.print("Row: ");
                row = scan.nextInt() - 1;

                System.out.print("Column: ");
                column = scan.nextInt() - 1;

                flag(row, column);
                board.printBoard();
                break;

            case "quit":

                scan.close();
                System.exit(0);

            case "":
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    void choose(int row, int column) {
        Cell cell = board.getBoard()[row][column];
        if (cell.isMine()) {
            cell.show();
            System.out.println("Lose");
        } else if (cell.hasValue()) {
            cell.show();
        } else if (!cell.hasValue()) {
            reveal(cell, new ArrayList<>(), new ArrayList<>(), 0);
        }
    }

    public void reveal(Cell cell, ArrayList<Cell> queue, ArrayList<Cell> processed, Integer i) {

        // debugging(number of recursions)
        i++;

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
            System.out.println(i);
            if (queue.isEmpty()) {
                return;
            } else {
                reveal(queue.get(0), queue, processed, i);
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

            System.out.println(i);

            processed.add(cell);
            queue.remove(cell);

            if (queue.isEmpty()) {
                return;
            } else {
                reveal(queue.get(0), queue, processed, i);
                return;
            }
        }

        // Debugging
/*     // Unreachable Statements
        board.printBoard();
        System.out.println(i);
*/
    }

    // sets isFlagged to true/false depending on its state

    void flag(int row, int column) {

        Cell cell = board.getBoard()[row][column];
        cell.setFlagged(!cell.isFlagged());

    }


    // creates a new board

    void restart(int length, int width, int numOfMines) {
        System.out.println("Generating new Board");
        board = new Board(length, width, numOfMines);
    }
}
