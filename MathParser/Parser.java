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

		Stack<Double> values = new Stack<>();
		Stack<Character> encounteredSymbols = new Stack<>();

		for (int i = start; i < end; i++) {
			char current = exp.charAt(i);

			if (current == '(') {
				int endIndex = findMatchingParenthesis(exp, i);
				if (endIndex == -1) endIndex = exp.length();

				Pair<Operation, Double> operation = determineSignAndOp(encounteredSymbols, eval(exp, i + 1, endIndex));
				doOperation(operation, values);

				i = endIndex;
			} else if (Character.isDigit(current)) {
				int endOfNum = getRestOfTheNumber(exp, i);

				Pair<Operation, Double> operation = determineSignAndOp(encounteredSymbols, Double.parseDouble(exp.substring(i, endOfNum + 1)));
				doOperation(operation, values);

				i = endOfNum;
			} else {
				encounteredSymbols.push(current);
			}
		}

		double sum = 0;
		while (!values.empty()) sum += values.pop();

		return sum;

	}

	private int getRestOfTheNumber(String exp, int start) {
		int i = start;
		while (i + 1 < exp.length() && (Character.isDigit(exp.charAt(i + 1)) || (exp.charAt(i + 1) == '.'))) {
			i++;
		}
		return i;
	}

	private void doOperation(Pair<Operation, Double> operation, Stack<Double> values) {
		Operation op = operation.getKey();

		switch (op) {
		case ADD: values.push(operation.getValue()); break;
		case MULTIPLY: values.push(values.pop() * operation.getValue()); break;
		case DIV: values.push(values.pop() / operation.getValue()); break;
			// case POW: values.push(Math.pow(values.pop(), operation.getValue())); break;
		}
	}

	private Pair<Operation, Double> determineSignAndOp(Stack<Character> encounteredSymbols, double lastNumber) {

		Character lastSymbol = encounteredSymbols.empty() ? ' ' : encounteredSymbols.pop();
		Operation op = Operation.ADD;
		double value = lastNumber;

		switch (lastSymbol) {
		case '-': value = lastNumber * -1; break;
		//case '^': op = Operation.POW; break;
		case '÷':
		case '/': op = Operation.DIV; break;
		case '×':
		case '*': op = Operation.MULTIPLY; break;
		}

		return encounteredSymbols.empty() ? new Pair<Operation, Double>(op, value) : determineSignAndOp(encounteredSymbols, value);
	}


	public int findMatchingParenthesis(String exp, int index) {
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
}

enum Operation {
	ADD,
	MULTIPLY,
	DIV,
	//POW;
}
