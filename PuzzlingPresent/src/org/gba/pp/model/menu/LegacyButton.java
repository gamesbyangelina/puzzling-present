package org.gba.pp.model.menu;

import org.flixel.FlxSprite;

//Old code describing a button
public abstract class LegacyButton extends FlxSprite {

	public LegacyButton(int x, int y){
		super(x,y);
	}

	public abstract void click();
	
}
