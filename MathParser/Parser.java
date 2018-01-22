import java.util.List;
import java.util.Arrays;
import java.util.Stack;
import java.util.Scanner;
import javafx.util.Pair;
import java.util.stream.Collectors;
import java.lang.StringBuilder;

public class Parser {

	public static void main(String[] args) {
		Parser parser = new Parser();
		Scanner scanner = new Scanner(System.in);

		boolean running = true;
		while (running) {
			System.out.print("Expression to evaluate: ");
			String expression = scanner.nextLine();
			if (expression.equalsIgnoreCase("quit")) {
				running = false;
			} else {
				System.out.println("Result: " + parser.eval(expression));
			}
		}
		scanner.close();
	}

	// Pretty sure this isn't the best way to do this.
	private String preprocess(String exp) {
		String cleaned = exp.replaceAll("[^\\d*-+÷×()]", "");
		StringBuilder builder = new StringBuilder(cleaned);
		for (int i = 0; i < builder.length(); i++) {
			char character = builder.charAt(i);
			if (character == '(') {
				if (i - 1 >= 0 && (Character.isDigit(builder.charAt(i - 1)) || builder.charAt(i - 1) == ')')) {
					builder.insert(i, '*');
				}
			} else if (character == ')') {
				if (i + 1 < builder.length() && (Character.isDigit(builder.charAt(i + 1)) || builder.charAt(i + 1) == '(')) {
					builder.insert(i + 1, '*');
				}
			}
		}
		return builder.toString();
	}

	// A stupid algorithm that I came up with.

	public double eval(String exp) {
		String preprocessed = preprocess(exp);
		return eval(preprocessed, 0, preprocessed.length());
	}

	private double eval(String exp, int start, int end) {

		Stack<Pair<Operator, Double>> operations = new Stack<>();
		Stack<Character> encounteredSymbols = new Stack<>();

		for (int i = start; i < end; i++) {
			char current = exp.charAt(i);

			if (current == '(') {
				int endIndex = findMatchingParenthesis(exp, i);
				if (endIndex == -1) endIndex = exp.length();
				operations.push(determineSignAndOp(encounteredSymbols, eval(exp, i + 1, endIndex)));
				i = endIndex;
			} else if (Character.isDigit(current)) {
				int endOfNum = getRestOfTheNumber(exp, i);
				operations.push(determineSignAndOp(encounteredSymbols, Double.parseDouble(exp.substring(i, endOfNum + 1))));
				i = endOfNum;
			} else {
				encounteredSymbols.push(current);
			}
		}

		return calculateAll(operations);
	}

	private double calculateAll(Stack <Pair<Operator, Double>> operations) {
		while (operations.size() > 1) {
			doOperations(operations, operations.pop());
		}
		return operations.pop().getValue();
	}

	private void doOperations(Stack<Pair<Operator, Double>> operations, Pair<Operator, Double> first) {
		Pair<Operator, Double> second = operations.pop();
		while (second.getKey().priority > first.getKey().priority) {
			doOperations(operations, second);
			second = operations.pop();
		}
		operations.push(new Pair<Operator, Double> (second.getKey(), first.getKey().function.apply(second.getValue(), first.getValue())));
	}

	private Pair<Operator, Double> determineSignAndOp(Stack<Character> encounteredSymbols, double lastNumber) {

		Character lastSymbol = encounteredSymbols.empty() ? ' ' : encounteredSymbols.pop();
		Operator op = Operator.ADD;
		double value = lastNumber;

		switch (lastSymbol) {
		case '-': value = lastNumber * -1; break;
		case '^': op = Operator.POW; break;
		case '÷':
		case '/': op = Operator.DIV; break;
		case '×':
		case '*': op = Operator.MULTIPLY; break;
		}

		return encounteredSymbols.empty() ? new Pair<Operator, Double>(op, value) : determineSignAndOp(encounteredSymbols, value);
	}


	private int findMatchingParenthesis(String exp, int index) {
		int num = 0;
		for (int i = index; i < exp.length(); i++) {
			if (exp.charAt(i) == '(') {
				num++;
			} else if (exp.charAt(i) == ')') {
				num--;
				if (num == 0) {
					return i;
				}
			}
		}
		return -1;
	}

	private int getRestOfTheNumber(String exp, int start) {
		int i = start;
		while (i + 1 < exp.length() && (Character.isDigit(exp.charAt(i + 1)) || (exp.charAt(i + 1) == '.'))) {
			i++;
		}
		return i;
	}
}

