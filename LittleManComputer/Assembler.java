import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Assembler {

	private Memory memory;
	private HashMap<String, Integer> labels;
	private HashMap<String, Integer> instructions;

	public Assembler(Memory memory){
		this.memory = memory;
		instructions = createInstructionSet();
		labels = new HashMap<>();
	}

	private HashMap<String, Integer> createInstructionSet(){
		HashMap<String, Integer> instructions = new HashMap<>();
		instructions.put("hlt", 0);
		instructions.put("add", 1);
		instructions.put("sub", 2);
		instructions.put("sta", 3);
		instructions.put("sto", 3);
		instructions.put("lda", 5);
		instructions.put("bra", 6);
		instructions.put("brz", 7);
		instructions.put("brp", 8);
		instructions.put("inp", 9);
		instructions.put("out", 9);
		instructions.put("otc", 9);
		instructions.put("dat", 4);
		return instructions;
	}

	private void parseLine(String line){

	}

	private void getLabels(List<String> lines){
		labels.clear();
	}

	public void assemble(String filename){
		List<String> lines = Files.readAllLines(Paths.get(filename));
		getLabels(lines);
		lines.forEach(this::parseLine);
	}
}
