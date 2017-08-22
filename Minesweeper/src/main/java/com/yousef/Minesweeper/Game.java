package com.yousef.Minesweeper;

import java.io.IOException;
import java.util.*;

public class Game {

    Board board;
    boolean playing;

    public Game(int length, int width, int numOfMines) throws IllegalArgumentException {

        help();
        playing = true;

        // First click isn't guaranteed to bo safe
        // TODO: create the board when the user makes his first choice.
        board = Board.createBoard(length, width, numOfMines);

        start();
    }

    public void start() {

        while (playing) {
            userInput();
        }

        System.exit(0);
    }

    public void help() {
        System.out.println();
        System.out.println("Command Format(s):  ");
        System.out.println("                  <Command>, <Row>, <Column>");
        System.out.println("Example:          Check, 10, 10");
        System.out.println("Commands:");
        System.out.println("           Help: Opens the help menu.");
        System.out.println("           Check: Specify the Cell you want to check.");
        System.out.println("           Flag: Specify the Cell you want to flag.");
        System.out.println("           Restart: Start a new Game.");
        System.out.println("           Quit/Exist: Quits the current Game.");
        System.out.println();
    }

    void userInput() {

        System.out.print("$ ");
        Scanner scan = new Scanner(System.in);
        String userInput = scan.nextLine().trim().toLowerCase();

        int row;
        int column;

        switch (userInput) {
        case "help":

            help();
            break;

        case "check":
            try {
                System.out.print("Row: ");
                row = scan.nextInt() - 1;

                System.out.print("Column: ");
                column = scan.nextInt() - 1;

                board.check(row, column);
                board.printBoard();

            } catch (InputMismatchException e) {
                System.out.println("Invalid input, Please enter a number.");
            }
            break;

        case "restart":

            try {
                System.out.print("Length: ");
                row = scan.nextInt() - 1;

                System.out.print("Width: ");
                column = scan.nextInt() - 1;

                System.out.print("Number Of Mines: ");

                int numOfMines = scan.nextInt();

                restart(row, column, numOfMines);

            } catch (InputMismatchException e) {
                System.out.println("Invalid input, Please enter a number.");
            }

            break;

        case "flag":

            try {
                System.out.print("Row: ");
                row = scan.nextInt() - 1;

                System.out.print("Column: ");
                column = scan.nextInt() - 1;

                board.flag(row, column);
                board.printBoard();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, Please enter a number.");
            }
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

    // creates a new board

    void restart(int length, int width, int numOfMines) {

        System.out.print("\033[H\033[2J");
        System.out.flush();

        try {
            new Game(length, width, numOfMines);
        } catch (IllegalArgumentException c) {
            System.out.println("Invaild Input!");
        }
    }

    public static void main(String[] args) {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException e) {
        } catch (InterruptedException exception) {
        }

        Scanner scan = new Scanner(System.in);

        int length = 0;
        int width = 0;
        int numOfMines = 0;

        try {
            System.out.print("Length: ");
            length = scan.nextInt();

            System.out.print("Width: ");
            width = scan.nextInt();

            System.out.print("Number Of Mines: ");
            numOfMines = scan.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input, Please enter a number.");
            main(new String[0]);
        }

        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            new Game(length, width, numOfMines);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Input!");
            main(new String[0]);
        }

    }
}
