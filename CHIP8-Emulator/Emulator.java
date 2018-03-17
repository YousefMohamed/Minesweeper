import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.nio.file.Files;
import java.util.HashMap;
import java.nio.file.Path;

public class Emulator {

	private class OpcodeInfo {
		int[] stuff;
		public OpcodeInfo(int opcode, int opcode2) {
			stuff = new int[]  {
				(opcode >> 4) & 0xF,
				opcode & 0xF,
				(opcode2 >> 4) & 0xF,
				opcode2 & 0xF
			};
		}

		public int get(int num) {
			return stuff[num];
		}

		public int getNNN() {
			return ((stuff[1] << 8) | (stuff[2] << 4) | stuff[3]);
		}

		public byte getN() {
			return (byte) stuff[3];
		}

		public byte getX() {
			return (byte) stuff[1];
		}

		public byte getY() {
			return (byte) stuff[2];
		}

		public byte getKK() {
			return (byte) ((stuff[2] << 4) | stuff[3]);
		}
	}

	private byte[] registers = new byte[0x10];
	private byte[] memory = new byte[0xFFF];

	private byte[][] display = new byte[64][31];

	private short[] stack = new short[0x10];
	private short I = 0;

	private byte stackPointer = 0;
	private short programCounter = 0x200;

	public Emulator(Path file, int delay, boolean debug) throws Exception {
		byte[] game = Files.readAllBytes(file);
		System.arraycopy(game, 0, memory, 0x200, game.length);

		Terminal.setTermToCharBreak();

		loadSprites();
		Terminal.restore();
	}

	private void loadSprites() {

	}

	public void run()  {
		for(; programCounter < memory.length - 1; programCounter += 2)  {
			OpcodeInfo info = new OpcodeInfo(memory[programCounter], memory[programCounter + 1]);
			switch(info.get(0)) {
				case 0x0: switchZero(info); break;
				case 0x1: jump(info); break;
				case 0x2: call(info); break;
				case 0x3: skipIfEqualToByte(info); break;
				case 0x4: skipIfNotEqualToByte(info); break;
				case 0x5: skipIfEqualToRegister(info);break;
				case 0x6: loadByte(info); break;
				case 0x7: addByte(info); break;
				case 0x8: switchEight(info); break;
				case 0x9: skipIfNotEqualToRegister(info); break;
				case 0xA: setINNN(info); break;
				case 0xB: jumpPlusRegZero(info); break;
				case 0xC: setRegisterRandom(info); break;
				case 0xD: break;
				case 0xE: break;
				case 0xF: switchF(info); break;
			}
		}
	}

	private void switchZero(OpcodeInfo info) {
		switch(info.get(3)) {
			case 0x0: clear(); break;
			case 0xE: Return(); break;
		}
	}

	private void switchEight(OpcodeInfo info) {
		switch(info.get(3)) {
			case 0x1: orRegister(info); break;
			case 0x2: andRegister(info); break;
			case 0x3: xorRegister(info); break;
			case 0x4: addRegisterCarry(info); break;
			case 0x5: sub(info); break;
			case 0x6: divbyTwo(info); break;
			case 0x7: subn(info); break;
			case 0xE: shl(info); break;
		}
	}

	private void switchF(OpcodeInfo info){

	}

	private void clear() {
		for(int i = 0; i < display.length; i++) {
			for(int j = 0; j < display[0].length; j++) {
				display[i][j] = 0;
			}
		}
	}

	private void Return() {
		programCounter = stack[stackPointer];
		stackPointer--;
	}

	private void jump(OpcodeInfo info) {
		programCounter = (short) info.getNNN();
	}

	private void call(OpcodeInfo info) {
		stackPointer++;
		stack[stackPointer] = programCounter;
		programCounter = (short) info.getNNN();
	}

	private void skipIfEqualToByte(OpcodeInfo info) {
		if(registers[info.getX()] == info.getKK()) {
			programCounter += 2;
		}
	}

	private void skipIfEqualToRegister(OpcodeInfo info) {
		if(registers[info.getX()] == registers[info.getY()]) {
			programCounter += 2;
		}
	}

	private void skipIfNotEqualToByte(OpcodeInfo info) {
		if(registers[info.getX()] != info.getKK()) {
			programCounter += 2;
		}
	}

	private void loadByte(OpcodeInfo info) {
		registers[info.getX()] = info.getKK();
	}

	private void addByte(OpcodeInfo info) {
		registers[info.getX()] += info.getKK();
	}

	private void loadRegister(OpcodeInfo info) {
		registers[info.getX()] = registers[info.getY()];
	}

	private void orRegister(OpcodeInfo info) {
		registers[info.getX()] = (byte) (registers[info.getX()] | registers[info.getY()]);
	}

	private void andRegister(OpcodeInfo info) {
		registers[info.getX()] = (byte) (registers[info.getX()] & registers[info.getY()]);
	}

	private void xorRegister(OpcodeInfo info) {
		registers[info.getX()] = (byte) (registers[info.getX()] ^ registers[info.getY()]);
	}

	private void addRegisterCarry(OpcodeInfo info) {
		int result = ((int) registers[info.getX()]) + registers[info.getY()];
		registers[0xF] = (byte) ((result > 255) ? 0x0 : 0x1);
		registers[info.getX()] = (byte) result;
	}

	private void sub(OpcodeInfo info) {
		registers[0xF] = (byte) ((registers[info.getX()] > registers[info.getY()]) ? 0x1 : 0x0);
		registers[info.getX()] = (byte) (registers[info.getX()] - registers[info.getY()]);
	}

	private void divbyTwo(OpcodeInfo info) {
		registers[0xF] = (byte) (((registers[info.getX()] & 0x1) == 1) ? 0x1 : 0x0);
		registers[info.getX()] = (byte) (registers[info.getX()] / 2);
	}

	private void subn(OpcodeInfo info) {
		registers[0xF] = (byte) ((registers[info.getY()] > registers[info.getX()]) ? 1 : 0);
		registers[info.getX()] = (byte) (registers[info.getY()] - registers[info.getX()]);
	}

	private void shl(OpcodeInfo info) {
		registers[0xF] = (byte) ((registers[info.getX()] & -0x2) == -128 ? 1 : 0);
		registers[info.getX()] = (byte) (registers[info.getX()] * 2);
	}

	private void skipIfNotEqualToRegister(OpcodeInfo info) {
		if(registers[info.getX()] == registers[info.getY()]) {
			programCounter += 2;
		}
	}

	private void setINNN(OpcodeInfo info) {
		I = (short) info.getNNN();
	}

	private void jumpPlusRegZero(OpcodeInfo info) {
		programCounter = (short) (info.getNNN() + registers[0]);
	}

	private void setRegisterRandom(OpcodeInfo info) {
		registers[info.getX()] = (byte) (ThreadLocalRandom.current().nextInt(0, 256) & info.getKK());
	}

	private void drawSprite(OpcodeInfo info) {

		int x = info.getX() % display.length;
		int y = info.getY();
		int n = info.get(3);
		int[] old = new int[n.length];
		for(int i = 0; i < n; i++){
			old[i] = display[x][(y + i) % display[0].length];
			display[x][(y + i) % display[0].length] = display[x][(y + i) % display[0].length] & n[i];

		}

	}

	public static void main(String[] args) throws Exception {
		Path file = null;
		int delay = 0;
		boolean debug = false;

		for(int i = 0; i < args.length; i++)  {
			switch (args[i]) {
				case "-f": file = Paths.get(args[i+1]); break;
				case "-s": debug = true; break;
				case "-d": delay = Integer.parseInt(args[i+1]); break;
				default: break;
			}
		}
		new Emulator(file, delay, debug).run();
	}
}
