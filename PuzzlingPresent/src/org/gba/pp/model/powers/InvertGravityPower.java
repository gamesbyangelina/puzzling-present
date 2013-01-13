package org.gba.pp.model.powers;

import org.flixel.FlxG;
import org.gba.pp.Registry;

public class InvertGravityPower extends Power{
	
	private float oldValue;
	
	public InvertGravityPower(){
		isToggleType = true;
	}
	
	@Override
	public void init(){
		isToggled = false;
		oldValue = Registry.player.acceleration.y;
	}

	@Override
	public void doPower(){
		super.doPower();
		isToggled = !isToggled;
		Registry.player.acceleration.y *= -1;
	}

	@Override
	public void revert(){
		super.revert();
		Registry.player.acceleration.y = oldValue;
		isToggled = false;
	}

	@Override
	public String getTargetValue(){
		return ""+Registry.player.acceleration.y;
	}

	@Override
	public String getName(){
		return "Flip Gravity";
	}

	@Override
	public String getDescription(){
		return "Make down up, and up down! Press X to turn gravity upside down, and again to reset it.";
	}

}
