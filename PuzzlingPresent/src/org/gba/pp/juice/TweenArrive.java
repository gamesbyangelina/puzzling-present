package org.gba.pp.juice;

import org.flixel.FlxObject;

//CAUTION: Some of these tweens don't work at all. This was a first try implementing them.
public class TweenArrive extends TweenTarget {

	float speed;
	
	public TweenArrive(FlxObject b, float target, float speed){
		super(b, target);
		this.speed = speed;
	}
	
	@Override
	public void doTween(){
		basic.y += speed * (target - basic.y);
		if(Math.abs(speed * (target - basic.y)) < 1)
			needsTweening = false;
	}

}
