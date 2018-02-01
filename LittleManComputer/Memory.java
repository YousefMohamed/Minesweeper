public class Memory {

	Address[] memory;

	public Memory(int size){
		memory = new Address[size];
		for(int i = 0; i< memory.length; i++){
			memory[i] = new Address(0, 0);
		}
	}

	private class Address {
		int instruction;
		int value;

		public Address(int instruction, int value) {
			this.instruction = instruction;
			this.value = value;
		}
	}

	public int getInstructionAt(int index){
		return memory[index].instruction;
	}

	public int getValueAt(int index) {
		return memory[index].value;
	}

	public int getAsDat(int index) {
		return memory[index].value + memory[index].instruction * 100;
	}

	public void setAsDat(int index, int dat){
		memory[index].instruction = dat / 100;
		memory[index].value = (int) ((((double) dat / 100) - memory[index].instruction) * 100);
	}

	public int setInstructionOf(int index, int instruction){
		return memory[index].instruction = instruction;
	}

	public int setValueOf(int index, int value){
		return memory[index].value = value;
	}
	
	public int getSize(){
		return memory.length;
	}
}

