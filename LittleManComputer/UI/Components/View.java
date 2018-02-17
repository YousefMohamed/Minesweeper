package UI.Components;

import java.util.ArrayList;
import UI.UIComponent;

public class View extends UIComponent {
	ArrayList<UIComponent> children = new ArrayList<>();
	UIComponent focused;
	
	@Override
	public void draw(){
		for(UIComponent child : children){
			child.draw();
		}
	}
	public void add(UIComponent component){
		children.add(component);
	}
}
