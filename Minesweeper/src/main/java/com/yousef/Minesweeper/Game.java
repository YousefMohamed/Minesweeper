package com.yousef.Minesweeper;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {

    Board board;
    boolean playing;
    String help = "Commands:\n" + "          Help: Opens the help menu.\n"
            + "          Check: Reveals the cell you specify.\n" + "          Flag: Flags the cell you specify.\n"
            + "          Restart: Starts a new Game.\n" + "          Quit/Exit: Exits the Game.\n\n"
            + "Command Format(s):\n" + "                 <Command> <Row> <Column>\n" + "                 <Command>\n\n"
            + "Examples:\n" + "          Check 10 10\n" + "          Restart\n\n";

    public Game() throws IllegalArgumentException {
        int[] parameters = init();

        System.out.print(help);

        // First click isn't guaranteed to bo safe
        // TODO: create the board when the user makes his first choice.
        board = Board.createBoard(parameters[0], parameters[1], parameters[2]);
        System.out.println(board);

        userInput();
    }

    private int[] init() {

        Scanner scan = new Scanner(System.in);

        int length = 0;
        int width = 0;
        int numOfMines = 0;

        while (!playing) {

            try {
                System.out.print("Length: ");
                length = scan.nextInt();

                System.out.print("Width: ");
                width = scan.nextInt();

                System.out.print("Number Of Mines: ");
                numOfMines = scan.nextInt();

                if ((length * width) <= numOfMines) {
                    System.out.println("Error: (Length * Width) <= Number Of Mines");
                    continue;
                }

                playing = true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                scan.nextLine();
                continue;
            }
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();

        return new int[] { length, width, numOfMines };
    }

    void userInput() {

        Scanner scan = new Scanner(System.in);

        while (playing) {

            if (board.gameEnded()) {
                System.out.println("New Game? Yes|Quit");

                System.out.print("$ ");

                String userInput = scan.nextLine().trim().toLowerCase();

                if (userInput.equals("yes") || userInput.equals("new game")) {
                    restart();
                } else if (userInput.equals("quit") || userInput.equals("exit")) {
                    playing = false;
                    continue;
                } else {
                }
            } else {

                System.out.print("$ ");

                String[] userInput = scan.nextLine().trim().toLowerCase().split("\\s+");

                int row = 0;
                int column = 0;
                String command = "Invalid input";

                if (userInput.length == 1) {
                    command = userInput[0];
                    switch (command) {

                    case "help":

                        System.out.print(help);
                        break;

                    case "restart":

                        restart();
                        break;

                    case "quit":

                        playing = false;
                        break;

                    case "exit":

                        playing = false;
                        break;

                    case "":
                        break;
                    default:
                        System.out.println("Invalid Input!");
                        break;
                    }
                } else if (userInput.length == 3) {

                    command = userInput[0];

                    try {
                        row = Integer.parseInt(userInput[1].trim()) - 1;
                        column = Integer.parseInt(userInput[2].trim()) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number.");
                        continue;
                    }

                    switch (command) {

                    case "check":

                        if (row >= board.getLength() || column >= board.getWidth() || row < 0 || column < 0) {
                            System.out.println(
                                    "Error: Specified row/column is bigger than the length/width of the board.");
                            break;
                        }

                        board.check(row, column);
                        System.out.println(board.toString());
                        break;

                    case "flag":

                        if (row >= board.getLength() || column >= board.getWidth() || row < 0 || column < 0) {
                            System.out.println(
                                    "Error: Specified row/column is bigger than the length/width of the board.");
                            break;
                        }

                        board.flag(row, column);
                        System.out.println(board.toString());
                        break;
                    default:
                        System.out.println("Invalid Input!");
                        break;
                    }

                } else {
                    System.out.println("Invalid Input!");
                    continue;
                }
            }
        }

        scan.close();
        System.exit(0);
    }

    void restart() {

        System.out.print("\033[H\033[2J");
        System.out.flush();

        new Game();
    }

    public static void main(String[] args) {

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException exception) {
        }

        new Game();
    }
}
