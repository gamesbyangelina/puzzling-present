package org.gba.pp.model.powers;

import org.gba.pp.Registry;

public class ElasticPower extends Power {

	float value;
	
	public ElasticPower(){
		isToggleType = true;
	}
	
	@Override
	public void init(){
		isToggled = false;
		value = Registry.player.elasticity;
	}

	@Override
	public void doPower(){
		super.doPower();
		if(!isToggled){
			Registry.player.elasticity  += 1.2;
		}
		else{
			Registry.player.elasticity  -= 1.2;
		}
		isToggled = !isToggled;
	}

	@Override
	public void revert(){
		super.revert();
		Registry.player.elasticity = value;
		isToggled = false;
	}
	
	@Override
	public String getTargetValue(){
		return ""+Registry.player.elasticity;
	}

	@Override
	public String getName(){
		return "Bounce!";
	}

	@Override
	public String getDescription(){
		return "Activate make yourself bouncy,\n press again to return to normal.\nBounce longer to get higher!";
	}

}
