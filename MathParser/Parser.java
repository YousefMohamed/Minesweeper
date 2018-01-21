import java.util.*;
import javafx.util.Pair;

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
				System.out.println("Result: " + parser.eval(expression, 0, expression.length()));
			}
		}
		scanner.close();
	}

	// A stupid algorithm that I came up with.

	public double eval(String exp, int start, int end) {

		Stack<Pair<Operation, Double>> operations = new Stack<>();
		Stack<Character> encounteredSymbols = new Stack<>();

		for (int i = start; i < end; i++) {
			char current = exp.charAt(i);

			if (current == '(') {
				int endIndex = findMatchingParenthesis(exp, i);
				if (endIndex == -1) endIndex = exp.length();

				// I could just pre-process the string, but whatever..  
				if ((i - 1) >= 0 && Character.isDigit(exp.charAt(i - 1))) encounteredSymbols.push('*');
				operations.push(determineSignAndOp(encounteredSymbols, eval(exp, i + 1, endIndex)));
				if ((endIndex + 1) < exp.length() && Character.isDigit(exp.charAt(endIndex + 1))) encounteredSymbols.push('*');
				
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

	private double calculate(Pair<Operation, Double> first, Pair<Operation, Double> second) {
		switch (first.getKey()) {
		case ADD: return first.getValue() + second.getValue();
		case MULTIPLY: return first.getValue() * second.getValue();
		case DIV: return second.getValue() / first.getValue();
		case POW: return Math.pow(second.getValue(), first.getValue());
		default: return 0;
		}
	}

	private double calculateAll(Stack <Pair<Operation, Double>> operations) {
		while (operations.size() > 1) {
			doOperations(operations, operations.pop());
		}
		return operations.pop().getValue();
	}

	private void doOperations(Stack<Pair<Operation, Double>> operations, Pair<Operation, Double> first) {
		Pair<Operation, Double> second = operations.pop();
		while (second.getKey().priority > first.getKey().priority) {
			doOperations(operations, second);
			second = operations.pop();
		}
		operations.push(new Pair<Operation, Double> (second.getKey(), calculate(first, second)));
	}

	private Pair<Operation, Double> determineSignAndOp(Stack<Character> encounteredSymbols, double lastNumber) {

		Character lastSymbol = encounteredSymbols.empty() ? ' ' : encounteredSymbols.pop();
		Operation op = Operation.ADD;
		double value = lastNumber;

		switch (lastSymbol) {
		case '-': value = lastNumber * -1; break;
		case '^': op = Operation.POW; break;
		case '÷':
		case '/': op = Operation.DIV; break;
		case '×':
		case '*': op = Operation.MULTIPLY; break;
		}

		return encounteredSymbols.empty() ? new Pair<Operation, Double>(op, value) : determineSignAndOp(encounteredSymbols, value);
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

