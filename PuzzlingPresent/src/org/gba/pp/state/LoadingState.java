package org.gba.pp.state;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.event.AFlxCamera;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;

/*
	The loading state claims to be loading the next level, but really just masks
	the data upload process that pushes survey responses etc. to the server.
	As such, it's a short state.
*/

public class LoadingState extends FlxState {

	FlxState next;
	boolean run = false;
	
	public LoadingState(FlxState next){
		this.next = next;
	}

	@Override
	public void create(){
		FlxG.setBgColor(Asset.CHRISTMAS_EVE);
		FlxText loading = new FlxText(0, -15 + FlxG.height/2, FlxG.width);
		loading.setText(StringBank.l_loading);
		loading.setFormat(loading.getFont(), 16, Asset.CREAM, "center");
		add(loading);
	}
	
	@Override 
	public void update(){
		if(run)
			return;
		run = true;
		Registry.postLog();
		FlxG.fade(Asset.CHRISTMAS_EVE, 1.0f, new AFlxCamera(){

			@Override
			public void callback(){
				FlxG.switchState(next);
			}
			
		});
	}

}
