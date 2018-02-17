import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import UI.Components.Textbox;
import UI.Components.View;
import term.TermStyle;
import term.Terminal;

public class LMC {
	private final String[] input;
	Memory memory = new Memory(100);
	public LMC(Path path) throws IOException {
		// "Loop" and "loop" aren't different, because I'm lazy.
		input = Files.readAllLines(path).stream().map(i -> i.trim()).toArray(String[]::new);
	}

	public void run() throws IOException, InterruptedException {

		Terminal.setTermToCharBreak();
		Terminal.hideCursor();
		Terminal.clear();
		View view = new View();
		Textbox box = new Textbox(10, 10, "Hello");
		view.add(box);
		Terminal.setStyle(TermStyle.STRIKETHROUGH);

		assembleIntoRAM(memory, new HashMap<String, Integer>(), new HashMap<String, List<Integer>>(), input, 0, 0);

		Scanner scanner = new Scanner(System.in);
		int accumulator = 0;

		for (int i = 0; i < memory.getSize(); i++) {
			if(System.in.available() != 0){
				System.out.println((char) System.in.read());
			}
			box.setText("Hello" + i);
			view.draw();
			Terminal.flush();
			int instruction = memory.getInstructionAt(i);
			int value = memory.getValueAt(i);
			switch (instruction) {
			case 0:	System.exit(0); break;
			case 1: accumulator += memory.getAsDat(value); break;
			case 2: accumulator -= memory.getAsDat(value); break;
			case 3: memory.setAsDat(value, accumulator); break;
			case 5: accumulator = memory.getAsDat(value); break;
			case 6: i = value; break;
			case 7: if (accumulator == 0) i = value; break;
			case 8: if (accumulator >= 0) i = value; break;
			case 9:
				if (value == 1) {
					accumulator = Integer.parseInt(scanner.nextLine());
				} else if (value == 2) {
					//System.out.print(accumulator);
				} else if (value == 22){
					//System.out.print((char) accumulator);
				}
				break;
			default:
				//System.out.println("Unknown instruction at address: " + i);
				break;
			}
		}
		Terminal.showCursor();
		Terminal.restore();
	}


	public void assembleIntoRAM(Memory memory, HashMap<String, Integer> labels, HashMap<String, List<Integer>> waiting, String[] input2, int currentLine, int start) {
		String[] current = input2[currentLine].split("\\s+");
		boolean shouldSetValue = true;
		switch(current[start]){
		case "hlt":
			memory.setInstructionAt(currentLine, 0);
			shouldSetValue = false;
			break;
		case "add":
			memory.setInstructionAt(currentLine, 1);
			break;
		case "sub":
			memory.setInstructionAt(currentLine, 2);
			break;
		case "sta":
			memory.setInstructionAt(currentLine, 3);
			break;
		case "sto":
			memory.setInstructionAt(currentLine, 3);
			break;
		case "lda":
			memory.setInstructionAt(currentLine, 5);
			break;
		case "bra":
			memory.setInstructionAt(currentLine, 6);
			break;
		case "brz":
			memory.setInstructionAt(currentLine, 7);
			break;
		case "brp":
			memory.setInstructionAt(currentLine, 8);
		case "inp":
			memory.setInstructionAt(currentLine, 9);
			memory.setValueAt(currentLine, 1);
			shouldSetValue = false;
			break;
		case "out":
			memory.setInstructionAt(currentLine, 9);
			memory.setValueAt(currentLine, 2);
			shouldSetValue = false;
			break;
		case "otc":
			memory.setInstructionAt(currentLine, 9);
			memory.setValueAt(currentLine, 22);
			shouldSetValue = false;
			break;
		case "dat":
			// You cannot call other labels here, because I'm lazy.
			if((start + 1) >= current.length){
				memory.setAsDat(currentLine, 0);
			} else {
				memory.setAsDat(currentLine, Integer.parseInt(current[start + 1]));
			}
			shouldSetValue = false;
			break;
		default:
			labels.put(current[start], currentLine);
			if(start + 1 < current.length) assembleIntoRAM(memory, labels, waiting, input2, currentLine, start + 1);
			if(waiting.get(current[start]) != null){
				for(int currentRegister: waiting.get(current[start])){
					memory.setValueAt(currentRegister, labels.get(current[start]));
				}
			}
			shouldSetValue = false;
			break;
		}

		if(shouldSetValue){
			if(labels.get(current[start + 1]) == null){
				try {
					int value = Integer.parseInt(current[start + 1]);
					memory.setValueAt(currentLine, value);
				} catch (NumberFormatException e){
					List<Integer> list = waiting.get(current[start + 1]) == null ? new ArrayList<Integer>() : waiting.get(current[start + 1]);
					list.add(currentLine);
					waiting.put(current[start + 1], list);
				}
			} else {
				memory.setValueAt(currentLine, labels.get(current[start + 1]));
			}
		}

		if((currentLine + 1) < input2.length){
			assembleIntoRAM(memory, labels, waiting, input2, currentLine + 1, 0);
		}
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-f")) {
				new LMC(Paths.get(args[i + 1])).run();
			}
		}
	}
}

