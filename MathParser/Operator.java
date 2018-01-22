import java.util.function.BiFunction;

public enum Operator {
	ADD (0, (a, b) -> a + b ),
	MULTIPLY (1, (a, b) -> a * b),
	DIV (1, (a, b) -> a / b),
	POW (2, Math::pow);

	public final int priority;
	public final BiFunction<Double, Double, Double> function;
	private Operator(int priority, BiFunction<Double, Double, Double> function) {
		this.priority = priority;
		this.function = function;
	}
}
