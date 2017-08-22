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
            cell = new BoardReader("oscillators.txt").createArray();
        } catch (IOException c) {
            System.out.println("File Not Found!");
            cell = new Cell[10][10];
        }

        System.out.println();
    
        Board board = new Board(cell);
        
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(board, 100, 200, TimeUnit.MILLISECONDS);
        
    }
}
