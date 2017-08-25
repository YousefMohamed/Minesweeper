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

        String path = new Main().getClass().getResource(args[0]).getPath();
        Board board;
        try {
            board = new Board(path);
        } catch (IOException e) {
            board = new Board();
        }

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(board, 100, 200, TimeUnit.MILLISECONDS);

    }
}
