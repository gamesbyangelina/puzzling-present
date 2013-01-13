package org.gba.pp.model.powers;

import org.gba.pp.state.WorldState;

//Describes one of Santa's special abilities.
//It's a bit crazy as it was based on in-development Mechanic Miner code which then changed
//and also included alterations to make it make sense for a game.
public abstract class Power {
	
	protected boolean isToggleType = false;
	protected boolean isToggled;

	public abstract void init();
	
	public void doPower(){
		if(canToggle() && isToggled())
			WorldState.powerStatus.setText(WorldState.powerOffText);
		else
			WorldState.powerStatus.setText(WorldState.powerOnText);
	}
	
	//Do we need this?
	public void revert(){
		WorldState.powerStatus.setText(WorldState.powerOffText);
	}

	public abstract String getTargetValue();
	
	public abstract String getName();

	public abstract String getDescription();

	public boolean canToggle(){
		return isToggleType;
	}

	public boolean isToggled(){
		return isToggled;
	}

}
