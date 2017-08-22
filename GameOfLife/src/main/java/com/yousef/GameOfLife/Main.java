package com.yousef.GameOfLife;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException e) {
        } catch (InterruptedException exception) {
        }

        Cell[][] cell;
        try {
            cell = new BoardReader("C:/Users/moham/Desktop/file.txt").createArray();
        } catch (IOException c) {
            System.out.println("File Not Found!");
            cell = new Cell[10][10];
        }
        Board board = new Board(cell);

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        scheduler.scheduleAtFixedRate(board, 100, 200, TimeUnit.MILLISECONDS);

        /* while (true) {
            board.update();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
