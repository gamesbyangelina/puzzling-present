package org.gba.pp.model.actions;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxText;
import org.flixel.event.AFlxCollision;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.juice.TweenArrive;
import org.gba.pp.model.Player;
import org.gba.pp.state.WorldState;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

/*
	flixel-android uses these callbacks for collisions.
	This is when you die on a spike/holly
*/
public class SpikeCollision implements AFlxCollision {

	private WorldState ws;
	private static FlxGroup ne;

	public SpikeCollision(WorldState parent){
		this.ws = parent;
		if(ne != null)
			ne.destroy();
		ne = null;
	}

	@Override
	public void callback(FlxObject spike, FlxObject player){
		player.flicker(-1.0f); // Flicker forever. Ow!
		((Player) player).disable(); //Can't control it any more
		
		ne = new FlxGroup();

		// Display level complete text/buttons/etc.
		FlxText overText = new FlxText(0, FlxG.height + 10, FlxG.width, StringBank.sp_ouch);
		overText.setFormat(overText.getFont(), 24, 0xffffff, "center");
		ne.add(overText);
		ws.tweens.add(new TweenArrive(overText, FlxG.height / 2 - 30, 0.2f));
		
		FlxText nextText = new FlxText(0, FlxG.height + 50, FlxG.width, StringBank.sp_restart_desktop);
		if(Gdx.app.getType() == ApplicationType.Android)
			nextText.setText(StringBank.sp_restart);
		nextText.setFormat(nextText.getFont(), 16, Asset.CREAM, "center");
		ne.add(nextText);
		ws.tweens.add(new TweenArrive(nextText, FlxG.height / 2 + 20, 0.2f));
		
		// Remove the tablet controls
		if (Gdx.app.getType() == ApplicationType.Android)
			ws._pad.visible = false;
		ws.exit.allowCollisions = FlxObject.NONE;

		Registry.player.velocity.y = -150;

		ws.playerDead = true;
		
		ws.add(ne);
		
	}

	//This is for the parent state to signal it doesn't need the game over window displayed any more
	public static void tearDown(WorldState parentState){
		if(ne != null){
			parentState.remove(ne);
			for(FlxBasic fo : ne.members){
				fo.destroy();
			}
			ne.destroy();
			ne = null;
		}
		
		if (Gdx.app.getType() == ApplicationType.Android)
			parentState._pad.visible = true;
		
		Registry.player.enable(); //Can't control it any more
		Registry.player.flicker(0.0f);
		parentState.exit.allowCollisions = FlxObject.ANY;
		parentState.power.revert();
	}
	
}
