import java.io.BufferedReader;
import java.util.HashSet;
import java.lang.InterruptedException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Process;

// Taken from wikipedia, some terminals may not support some of these escape sequences. (This is why you use terminfo.)
public final class Console {

	private Console(){}

	// Stands for Control Sequence introducer, read the wikipedia page.
	private static final String CSI = "\033[";

	private static Color currentFGColor = Color.WHITE;
	private static Color currentBGColor = Color.BLACK;
	private static HashSet<Style> currentStyles = new HashSet<>();

	public static void setCursorPosition(int x, int y){
		System.out.print(CSI + x + ";" + y + "H");
		System.out.flush();
	}

	public static void clear(){
		// This isn't working correctly, my terminal probably doesn't support it.
		// System.out.print(CSI + "H");

		System.out.print(CSI + "2J");
		setCursorPosition(0,0);
	}

	public static void hideCursor(){
		System.out.print(CSI + "?25l");
		System.out.flush();
	}

	public static void showCursor(){
		System.out.print(CSI + "?25h");
		System.out.flush();
	}

	public static void print(String text, Color foreground, Color background, Style... styles){
		setTempProperties(foreground, background, styles);
		System.out.print(text);
		resetProperties();
	}

	public static void println(String text, Color foreground, Color background, Style... styles){
		print(text, foreground, background, styles);
		System.out.println();
	}

	public static void reset(){
		currentBGColor = Color.BLACK;
		currentFGColor = Color.WHITE;
		currentStyles.clear();
		System.out.print(CSI + "0m");
		System.out.flush();
	}

	private static void setTempProperties(Color foreground, Color background, Style... styles) {
		StringBuilder command = new StringBuilder().append(CSI).append(foreground.fgValue + ";").append(background.bgValue + ";");
		for(Style style: styles){
			command.append(style.value + ";");
		}
		command.append("m");
		System.out.print(command.toString());
	}

	private static void resetProperties(){
		StringBuilder command = new StringBuilder().append(CSI).append(currentFGColor.fgValue + ";").append(currentBGColor.bgValue + ";");
		for(Style style : currentStyles){
			command.append(style.value).append(";");
		}
		command.append("m");
		System.out.print(command.toString());
	}

	// https://www.darkcoding.net/software/non-blocking-console-io-is-not-possible/
	// Currently Linux only
	public static void setTerminalToCharBreak() throws IOException, InterruptedException {
		// set the console to be character-buffered instead of line-buffered
		stty("-icanon min 1");

		// disable character echoing
		stty("-echo");
	}

	private static boolean stty(String args) throws IOException, InterruptedException {
		String cmd = "stty " + args + " < /dev/tty";

		return exec(new String[] {
					"sh",
					"-c",
					cmd
			});
	}

	private static boolean exec(String[] args)throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec(args);
		return process.waitFor() == 0 ? true : false;
	}

	public static void restore() throws IOException, InterruptedException {
		reset();
		stty("sane");
	}
}
