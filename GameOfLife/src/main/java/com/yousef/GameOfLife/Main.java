package com.yousef.GameOfLife;

import java.lang.Thread;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException e) {
        } catch (InterruptedException exception) {
        }
        Cell[][] cell;
        try{
        cell = new BoardReader("C:/Users/moham/Desktop/file.txt").createArray();
        }catch(IOException c){
            System.out.println("File Not Found!");
            cell = new Cell[10][10];
        }
        Board board = new Board(cell);

        while (true) {
            board.update();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
