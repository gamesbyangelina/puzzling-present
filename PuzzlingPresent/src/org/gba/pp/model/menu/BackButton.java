package org.gba.pp.model.menu;

import org.flixel.FlxG;
import org.flixel.FlxState;

//Button also basically used to switch state, except I stupidly hardcoded a graphic in there.
//Should've been gutted and replaced with a more general implementation, but no time.
public class BackButton extends AbstractButton {
	
	protected FlxState targetState;
	
	public BackButton(FlxState target, int x, int y){
		super(x, y);
		this.targetState = target;
		loadGraphic("pack:backbutton");
	}
	
	public BackButton(FlxState target, int x, int y, int w, int h, int col){
		super(x, y);
		this.targetState = target;
		this.makeGraphic(w, h, col);
	}

	@Override
	public void click(FlxState s){
		FlxG.switchState(targetState);
	}

}
