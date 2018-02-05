import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class LMC {

	private Memory memory = new Memory(100);

	private String[] input;

	private int instructionRegister = 0;
	private int addressRegister = 0;

	private int accumulator = 0;

	public LMC(Path path) throws IOException {
		input = Files.readAllLines(path).stream().map(i -> i.trim()).toArray(String[]::new);
	}

	public void run() {
		populateRam();
		Scanner input = new Scanner(System.in);

		for (int i = 0; i < memory.getSize(); i++) {
			int instruction = memory.getInstructionAt(i);
			int value = memory.getValueAt(i);

			switch (instruction) {
			case 0: System.exit(0); break;
			case 1: accumulator += memory.getAsDat(value); break;
			case 2: accumulator -= memory.getAsDat(value); break;
			case 3: memory.setAsDat(value, accumulator); break;
			case 5: memory.getAsDat(value); break;
			case 6: i = value; break;
			case 7: if (accumulator == 0) i = value; break;
			case 8: if (accumulator > 0) i = value; break;
			case 9:
				if (value == 1) {
					accumulator = Integer.parseInt(input.nextLine());
				} else if (value == 2) {
					System.out.println(accumulator);
				}
				break;
			}
		}
	}

	public void populateRam() {
		for (int i = 0; i < input.length; i++) {
			String[] current = input[i].split("\\s+");
			if (current.length > 2) {
				System.out.println("Invalid stuff");
			} else {
				String currentString = current[0];

				if (currentString.equalsIgnoreCase("hlt")) {
					memory.setInstructionOf(i, 0);
				} else if (currentString.equalsIgnoreCase("add")) {
					memory.setInstructionOf(i, 1);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				} else if (currentString.equalsIgnoreCase("sub")) {
					memory.setInstructionOf(i, 2);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				}else if (currentString.equalsIgnoreCase("brz")) {
					memory.setInstructionOf(i, 7);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				} else if (currentString.equalsIgnoreCase("sta")) {
					memory.setInstructionOf(i, 3);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				} else if (currentString.equalsIgnoreCase("lda")) {
					memory.setInstructionOf(i, 5);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				} else if (currentString.equalsIgnoreCase("bra")) {
					memory.setInstructionOf(i, 6);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				} else if (currentString.equalsIgnoreCase("brz")){
					memory.setInstructionOf(i, 7);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				} else if (currentString.equalsIgnoreCase("brp")) {
					memory.setInstructionOf(i, 8);
					memory.setValueOf(i, Integer.parseInt(current[1]));
				} else if (currentString.equalsIgnoreCase("inp")) {
					memory.setInstructionOf(i, 9);
					memory.setValueOf(i, 01);
				} else if (currentString.equalsIgnoreCase("out")) {
					memory.setInstructionOf(i, 9);
					memory.setValueOf(i, 02);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-f")) {
				new LMC(Paths.get(args[i + 1])).run();
			}
		}
	}
}
