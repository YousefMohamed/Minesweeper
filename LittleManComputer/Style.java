public enum Style{
	BOLD(1), ITALIC(3), UNDERLINE(4), STRIKETHROUGH(9);

	public final int value;
	private Style(int value){
		this.value = value;
	}
}
