package org.gba.pp.model.powers;

import org.gba.pp.Registry;
import org.gba.pp.model.Player;

public class HighJumpPower extends Power {

	public float oldValue;
	
	public HighJumpPower(){
		isToggleType = true;
	}
	
	@Override
	public void init(){
		oldValue = Player.S_JUMP_VELOCITY;
	}

	@Override
	public void doPower(){
		super.doPower();
		if(!isToggled)
			Registry.player.JUMP_VELOCITY += 100;
		else
			Registry.player.JUMP_VELOCITY -= 100;
		isToggled = !isToggled;
	}

	@Override
	public void revert(){
		super.revert();
		isToggled = false;
		Registry.player.JUMP_VELOCITY = oldValue;
	}

	@Override
	public String getTargetValue(){
		return ""+Registry.player.JUMP_VELOCITY;
	}

	@Override
	public String getName(){
		return "High Jump";
	}

	@Override
	public String getDescription(){
		return "Press X to make Santa jump extra high! Press again to return to normal.";
	}

}
