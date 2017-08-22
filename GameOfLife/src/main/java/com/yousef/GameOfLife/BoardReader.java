package com.yousef.GameOfLife;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BoardReader {

	BufferedReader reader;

	public BoardReader(String file) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(file));
	}

	public Cell[][] createArray() throws IOException {

		ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
		char currentChar;

		int f = 0;
		int g = 0;
		while (reader.ready()) {

			currentChar = (char) reader.read();
			System.out.println("Reading: " + currentChar);

			if (cells.isEmpty()) {
				cells.add(new ArrayList<Cell>());
			}
			if (currentChar == '\n') {
				cells.add(new ArrayList<Cell>());
				f++;
				g = 0;
				System.out.println("New Line Found!");
			} else if(currentChar == '\r'){
				reader.mark(1);
				if(reader.read() == '\n'){
					cells.add(new ArrayList<Cell>());
					f++;
					g = 0;
					System.out.println("New Line Found!");
					reader.reset();
					reader.read();						
				}else{
					reader.reset();
					reader.read();
				}
			} else if (currentChar == '1') {
				cells.get(cells.size() - 1).add(new Cell(true, f, g));
				g++;
			} else if (currentChar == '0') {
				cells.get(cells.size() - 1).add(new Cell(false, f, g));
				g++;
			} else {
				continue;
			}
		}

		reader.close();

		Cell[][] cellsArray = new Cell[cells.size()][cells.get(0).size()];
		for (int i = 0; i < cells.size(); i++) {
			cellsArray[i] = cells.get(i).toArray(new Cell[cells.get(i).size()]);
		}
		return cellsArray;
	}
}