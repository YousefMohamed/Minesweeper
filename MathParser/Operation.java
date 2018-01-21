public enum Operation {
	ADD (0),
	MULTIPLY (1),
	DIV (1),
	POW (2);

	public final int priority;
	private Operation(int priority) {
		this.priority = priority;
	}
}
