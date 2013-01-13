package org.gba.pp.state;

import org.flixel.FlxState;
import org.gba.pp.Registry;

import com.badlogic.gdx.Gdx;

public class QuitState extends FlxState {

	@Override
	public void create(){
		Gdx.app.exit();
	}

}
