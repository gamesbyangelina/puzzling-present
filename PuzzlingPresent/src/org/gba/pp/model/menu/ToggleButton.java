package org.gba.pp.model.menu;

import java.util.LinkedList;
import java.util.List;

import org.flixel.FlxState;
import org.gba.pp.data.Asset;

//Generic button that has two states.
public class ToggleButton extends AbstractButton {

	public boolean toggled = false;
	private int c_on;
	private int c_off;
	
	public ToggleButton(int x, int y, int w, int h){
		super(x, y);
		
		this.c_on = -1;
		this.c_off = -1;
		
		loadGraphic(Asset.toggleStrip, false, false, 20, 20);
		addAnimation("on", new int[]{0});
		addAnimation("off", new int[]{1});
		
		play("off");
	}
	
	public ToggleButton(int x, int y, int w, int h, int c_on, int c_off){
		super(x, y);
		
		this.c_on = c_on;
		this.c_off = c_off;
		
		makeGraphic(w, h, c_off);
	}


	List<ToggleButton> linkedButtons = new LinkedList<ToggleButton>();
	
	@Override
	public void click(FlxState s){
		
		if(!toggled){
			for(ToggleButton b : linkedButtons){
				b.toggleOff();
			}
			toggleOn();
		}
		else{
			toggleOff();
		}
	}
	
	public void addLinkedButtons(ToggleButton ... ts){
		for(ToggleButton t : ts){
			if(t != this)
				linkedButtons.add(t);
		}
	}
	
	public void toggleOn(){
		if(c_on == -1)
			play("on");
		else
			makeGraphic((int) width, (int) height, c_on);
		toggled = true;
	}
	
	public void toggleOff(){
		toggled = false;
		if(c_off == -1)
			play("off");
		else
			makeGraphic((int) width, (int) height, c_off);
	}
	
	public void setToggled(boolean t){
		if(toggled){
			toggleOn();
		}
		else
			toggleOff();
	}

}
