package org.gba.pp.model.menu;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.gba.pp.state.WorldState;

//Button for switching states, basically.
public class SkipButton extends AbstractButton {

	WorldState next;
	
	public SkipButton(WorldState next, int x, int y, int w, int h, int color){
		super(x, y);
		makeGraphic(w, h, color);
		this.next = next;
	}

	@Override
	public void click(FlxState s){
		next.goToNextState();
	}

}
