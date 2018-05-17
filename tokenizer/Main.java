import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		Tokenizer tokenizer = new Tokenizer(args[0], Arrays.copyOfRange(args, 1, args.length));
		while(tokenizer.hasNext()){
			System.out.println(tokenizer.next());
		}
	}
}
