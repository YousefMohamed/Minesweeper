public enum Color{
	BLACK(30, 40), RED(31, 41), GREEN(32, 42), YELLOW(33, 43), BLUE(34, 44), MAGENTA(35, 45), CYAN(36, 46), WHITE(37, 47),
	BRIGHTBLACK(90, 100), BRIGHTRED(91, 101), BRIGHTGREEN(92, 102), BRIGHTYELLOW(93, 103), BRIGHTBLUE(94, 104), BRIGHTMAGENTA(95, 105), BRIGHTCYAN(96, 106), BRIGHTWHITE(97, 107);

	public final int fgValue;
	public final int bgValue;
	private Color(int fgValue, int bgValue){
		this.fgValue = fgValue;
		this.bgValue = bgValue;
	}
}
