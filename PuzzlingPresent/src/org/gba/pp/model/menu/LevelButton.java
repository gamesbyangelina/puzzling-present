package org.gba.pp.model.menu;

import org.flixel.FlxG;
import org.gba.pp.Registry;
import org.gba.pp.state.WorldState;

//Button for the level select screen
public class LevelButton extends LegacyButton {
	
	private int world;
	private int level;

	public LevelButton(int x, int y, int world, int level){
		super(x, y);
		this.world = world;
		this.level = level;
	}
	
	public void click(){
		FlxG.switchState(new WorldState(world, level, Registry.powers[world]));
	}

}
