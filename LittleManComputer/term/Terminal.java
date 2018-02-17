package term;

import java.io.BufferedReader;
import java.util.HashSet;
import java.lang.InterruptedException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Process;

// The ANSI Escape Codes are taken from wikipedia, some terminals may not support some of them (This is why you use terminfo).
//
// I could get the escape codes with tput (Or even better, use tput directly, but I think that will be slow.),
// when the class is loaded, but I'm too lazy for that, and most of these will work anyway.
//
// For any change to happen, you HAVE to call flush(), println(), or add a "\n" in you input, because they (afaik) flush System.out buffer (Yes, I'm using the output stream as a buffer, instead of making my own).

public final class Terminal {

	private Terminal(){}

	// Stands for Control Sequence Introducer, read the wikipedia page.
	// I could've just used "\e", but eh.
	private static final String CSI = "\033[";

	// Current state.
	private static TermColor currentFGColor = TermColor.WHITE;
	private static TermColor currentBGColor = TermColor.BLACK;
	private static HashSet<TermStyle> currentStyles = new HashSet<>();

	public static void setCursorPosition(int x, int y){
		System.out.print(CSI + x + ";" + y + "H");
	}

	public static void clear(){
		System.out.print(CSI + "2J");
		setCursorPosition(0,0);
	}

	public static void setStyle(TermStyle style){
		currentStyles.add(style);
		System.out.print(CSI + style.enable + "m");
	}

	public static void removeStyle(TermStyle style){
		currentStyles.remove(style);
		System.out.print(CSI + style.disable + "m");
	}

	public static void hideCursor(){
		System.out.print(CSI + "?25l");
	}

	public static void showCursor(){
		System.out.print(CSI + "?25h");
	}

	public static void print(String text, TermColor foreground, TermColor background, TermStyle... styles){
		setProperties(foreground, background, styles);
		System.out.print(text);
		resetProperties();
	}

	public static void println(String text, TermColor foreground, TermColor background, TermStyle... styles){
		print(text, foreground, background, styles);
		System.out.println();
	}

	public static void flush(){
		System.out.flush();
	}

	public static void reset(){
		currentBGColor = TermColor.BLACK;
		currentFGColor = TermColor.WHITE;
		currentStyles.clear();
		System.out.print(CSI + "0m");
		System.out.flush();
	}

	private static void setProperties(TermColor foreground, TermColor background, TermStyle... styles) {
		StringBuilder command = new StringBuilder().append(CSI).append(foreground.fgValue + ";").append(background.bgValue + ";");
		for(TermStyle style: styles){
			command.append(style.enable).append(";");
		}
		command.append("m");
		System.out.print(command.toString());
	}

	private static void resetProperties(){
		setProperties(currentFGColor, currentBGColor, currentStyles.toArray(new TermStyle[currentStyles.size()]));
	}

	public static void restore() throws IOException, InterruptedException {
		reset();
		stty("sane");
	}

	// https://www.darkcoding.net/software/non-blocking-console-io-is-not-possible/
	// Currently Linux only
	public static void setTermToCharBreak() throws IOException, InterruptedException {
		// set the console to be character-buffered instead of line-buffered
		stty("-icanon min 1");

		// disable character echoing
		stty("-echo");
	}

	private static boolean stty(String args) throws IOException, InterruptedException {
		String cmd = "stty " + args + " < /dev/tty";

		return exec(
			new String[] {
				"sh",
				"-c",
				cmd
			});
	}

	private static boolean exec(String[] args)throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec(args);
		return process.waitFor() == 0 ? true : false;
	}
}
