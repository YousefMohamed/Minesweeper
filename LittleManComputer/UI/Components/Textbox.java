package UI.Components;

import java.util.HashSet;

import UI.UIComponent;
import term.TermColor;
import term.TermStyle;
import term.Terminal;

public class Textbox extends UIComponent {

	private int x;
	private int y;
	private String text = "";
	private String newText = "";
	private TermColor textColor = TermColor.WHITE;
	private TermColor textBGColor = TermColor.BLACK;
	private HashSet<TermStyle> currentStyles = new HashSet<>();

	public Textbox(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Textbox(int x, int y, String text){
		this(x, y);
		this.text = text;
	}

	@Override
	public void draw(){

		if(!newText.equals(text)){
			Terminal.setCursorPosition(x, y);
			System.out.print(text);
		}
		text = newText;
	}
	
	public void setStyle(TermStyle style){
		currentStyles.add(style);
	}

	public void removeStyle(TermStyle style){
		currentStyles.remove(style);
	}

	public String getText(){
		return text;
	}
	
	public void setText(String newText){
		this.newText = newText;
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}
		
	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

}
