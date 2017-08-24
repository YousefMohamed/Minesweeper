package com.yousef.Minesweeper;

import java.io.IOException;
import java.util.*;

public class Game {

    Board board;
    boolean playing;
    Scanner scan;
    String help = "Commands:\n" 
                + "          Help: Opens the help menu.\n"
                + "          Check: Reveals the cell you specify.\n"
                + "          Flag: Flags the cell you specify.\n"
                + "          Restart: Starts a new Game.\n"
                + "          Quit/Exit: Exits the Game.\n"
                + "CommandFormat(s):\n"
                + "                 <Command> <Row> <Column>\n"
                + "                 <Command>\n"
                + "Examples:\n"
                + "          Check 10 10\n"
                + "          Restart\n";

    public Game() throws IllegalArgumentException {

        scan = new Scanner(System.in);
        int[] parameters = init();

        System.out.print(help);

        // First click isn't guaranteed to bo safe
        // TODO: create the board when the user makes his first choice.
        board = Board.createBoard(parameters[0], parameters[1], parameters[2]);
        System.out.println(board);
        start();
    }

    public void start() {

        while (playing) {
            userInput();
        }

        scan.close();
        System.exit(0);
    }

    private int[] init() {

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
                playing = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, Please enter a number.");
            }
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();

        return new int[] { length, width, numOfMines };
    }

    void userInput() {

        scan = new Scanner(System.in);

        if (board.hasLost() || board.hasWon()) {
            System.out.print("New Game? Yes|Quit");

            String input = scan.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("new game")) {
                restart();
            } else if (input.equals("quit") || input.equals("exit")) {
                playing = false;
                return;
            } else {
                return;
            }
        }

        System.out.print("$ ");
        String[] userInput = scan.nextLine().trim().toLowerCase().split(" ");

        if (userInput.length != 3) {
            if (userInput.length != 1) {
                return;
            }
        }

        String command = "Invalid Input!";
        int row = 0;
        int column = 0;

        if (userInput.length == 1) {
            command = userInput[0];
        } else if (userInput.length == 3) {
            command = userInput[0];
            try {
                row = Integer.parseInt(userInput[1].trim()) - 1;
                column = Integer.parseInt(userInput[2].trim()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input!");
                return;
            }
        }

        switch (command) {
        case "help":

            System.out.print(help);
            break;

        case "check":
            board.check(row, column);
            System.out.println(board.toString());

            break;

        case "flag":

            board.flag(row, column);
            System.out.println(board.toString());
            break;

        case "restart":

            restart();
            break;

        case "quit":

            playing = false;
            break;

        case "exist":

            playing = false;
            break;

        case "":
            break;
        default:
            System.out.println("Invalid Input!");
            break;
        }
    }

    void restart() {

        System.out.print("\033[H\033[2J");
        System.out.flush();

        try {
            new Game();
        } catch (IllegalArgumentException c) {
            System.out.println("Invaild Input!");
        }
    }

    public static void main(String[] args) {

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException exception) {
        }

        new Game();
    }
}
