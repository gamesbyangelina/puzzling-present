package org.gba.pp.juice;

import org.flixel.FlxObject;

//CAUTION: Some of these tweens don't work at all. This was a first try implementing them.
public class TweenTarget {

	protected FlxObject basic;
	protected float target;
	public boolean needsTweening = true;

	public TweenTarget(FlxObject b, float target){
		this.basic = b;
		this.target = target;
	}
	
	public void doTween(){};
	
	
	
}
