package com.yousef.GameOfLife;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BoardReader {

	BufferedReader reader;

	public BoardReader(String file) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(file));
	}

	public Cell[][] createArray() throws IOException {
		ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
		char currentChar = (char) reader.read();

		int f = 0;
		int g = 0;
		while (currentChar != 65535) {

			System.out.println("Reading: " + currentChar + (int) currentChar);

			if (cells.isEmpty()) {
				cells.add(new ArrayList<Cell>());
			}
			if (currentChar == '\n' || currentChar == '\r') {
				cells.add(new ArrayList<Cell>());
				f++;
				g = 0;
				System.out.println("New Line Found!");
			} else if (currentChar == '1') {
				cells.get(f).add(new Cell(true, f, g));
				g++;
			} else if (currentChar == '0') {
				cells.get(f).add(new Cell(false, f, g));
				g++;
			} else {

			}
			currentChar = (char) reader.read();
		}
		Cell[][] cellsArray = new Cell[cells.size()][cells.get(0).size()];
		for (int i = 0; i < cells.size(); i++) {
			cellsArray[i] = cells.get(i).toArray(new Cell[cells.get(i).size()]);
		}
		return cellsArray;
	}
}