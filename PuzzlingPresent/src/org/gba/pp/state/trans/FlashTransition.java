package org.gba.pp.state.trans;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.event.AFlxCamera;

/*
	This was before I started inlining these convenience classes, although in fairness I use this a lot.
	I imagine Java 7 might allow flixel-android to get around this a bit more neatly, but for now callbacks
	are done by providing implementations of AFlxCamera/AFlxCollision/etc. 
*/
public class FlashTransition implements AFlxCamera {

	private FlxState state;

	public FlashTransition(FlxState s){
		this.state = s;
	}

	@Override
	public void callback(){
		FlxG.switchState(state);
	}
	
}
