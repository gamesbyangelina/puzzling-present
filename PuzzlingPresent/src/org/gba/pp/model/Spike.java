package org.gba.pp.model;

import org.flixel.FlxSprite;
import org.gba.pp.data.Asset;

public class Spike extends FlxSprite {
	
	public Spike(int x, int y){
		super(x, y);
		immovable = true;
		loadGraphic(Asset.spike_strip, true, true, 16, 16);
		
		width = 14;
		height = 14;
		offset.x = -1;
		offset.y = -1;	
	}

}
