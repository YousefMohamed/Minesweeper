package term;

public enum TermStyle{
	BOLD(1), ITALIC(3), UNDERLINE(4), REVERSEVIDEO(7), CONCEAL(8), STRIKETHROUGH(9);

	public final int enable;
	public final int disable;
	private TermStyle(int enable){
		this.enable = enable;
		this.disable = enable + 20;
	}
}
