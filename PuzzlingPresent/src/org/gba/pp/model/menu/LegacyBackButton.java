package org.gba.pp.model.menu;

import org.flixel.FlxG;
import org.flixel.FlxState;

//Old code describing a state switch button
public class LegacyBackButton extends LegacyButton {

	private FlxState state;

	public LegacyBackButton(FlxState f, int x, int y){
		super(x, y);
		this.state = f;
	}

	@Override
	public void click(){
		FlxG.switchState(state);
	}

}
