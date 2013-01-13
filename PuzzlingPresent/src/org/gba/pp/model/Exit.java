package org.gba.pp.model;

import org.flixel.FlxSprite;
import org.gba.pp.data.Asset;

//Exit in the world.
public class Exit extends FlxSprite{

	public Exit(int i, int j){
		super(i, j, Asset.exit_strip);
		immovable = true;
	}

}
