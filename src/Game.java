import java.util.*;

public class Game {

    Board board;

    public Game() {

        board = new Board(10, 10, 10);

        help();

        System.out.println("Generating Board");
        board.generate();

        start();

    }

    public void start() {
        while (true) {
            userInput();
        }
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

        switch (userInput) {
            case "help":

                help();
                break;

            case "choose":

                int row = scan.nextInt() - 1;
                int column = scan.nextInt() - 1;
                choose(row, column);
                break;

            case "restart":

                restart(scan.nextInt(), scan.nextInt(), scan.nextInt());
                break;

            case "flag":

                flag(10, 10);
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
        if (board.getBoard()[row][column].isMine()) {
            board.printBoard();
            System.out.println("Lose");
        } else if (board.getBoard()[row][column].hasValue()) {
            board.getBoard()[row][column].show();
            board.printBoard();
        } else if (!board.getBoard()[row][column].hasValue()) {
            show(row, column, new ArrayList<>());
        }
    }

    public void show(int row, int column, ArrayList<Cell> queue) {

        System.out.println(queue);

        if (queue.isEmpty()) {
            queue.addAll(board.getBoard()[row][column].getSurroundingTiles());
            show(queue.get(0).getxPos(), queue.get(0).getyPos(), queue);
        } else {
            Cell cell = queue.get(0);

            for (Cell cell2 : cell.getSurroundingTiles()) {
                if (queue.contains(cell2)) {

                } else if (cell2.isShown()) {

                } else if (cell2.hasValue()) {
                    cell2.show();
                } else if (cell2.isMine()) {

                } else {
                    queue.add(cell2);
                }
            }

            cell.show();
            queue.remove(cell);

            board.printBoard();
            System.out.println();

            try {
                show(queue.get(0).getxPos(), queue.get(0).getyPos(), queue);
            } catch (IndexOutOfBoundsException e) {
                board.printBoard();
                System.out.println();
                return;
            }
        }
    }


    void flag(int row, int column) {

        if (board.getBoard()[row][column].isFlagged()) {
            board.getBoard()[row][column].setFlagged(false);
            return;
        }

        board.getBoard()[row][column].setFlagged(true);

    }

    void restart(int length, int width, int numOfMines) {
        board = new Board(length, width, numOfMines);
        System.out.println("Generating new Board");
        board.generate();
    }
}