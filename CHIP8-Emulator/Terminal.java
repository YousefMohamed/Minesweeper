import java.io.BufferedReader;
import java.lang.InterruptedException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Process;

public final class Terminal {
	public static void restore() throws IOException, InterruptedException {
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
