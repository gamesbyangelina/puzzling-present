package org.gba.pp.state;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.data.Asset;
import org.gba.pp.state.trans.FlashTransition;

//Unused screen to display the GBA logo.
public class GBAState extends FlxState {

	FlxText txt_gba;	float gba_timer = 0.5f;
	float timer_in = 0.0f;
	private float timer_out = 2.5f;
	
	@Override
	public void create(){
		FlxG.setBgColor(Asset.col_white);
		
		txt_gba = new FlxText(0, FlxG.height/2-10, FlxG.width, "gamesbyangelina");
		txt_gba.setColor(0xffff0000);
		txt_gba.setAlignment("center");
		txt_gba.setAlpha(0.0f);
		txt_gba.setSize(24);
		add(txt_gba);
		
	}
	
	@Override
	public void update(){
		timer_in += FlxG.elapsed;
		if(gba_timer < timer_in)
			txt_gba.setAlpha((timer_in-gba_timer)/1.0f);
		if(timer_in > timer_out ){
			txt_gba.setAlpha(Math.max(0, timer_out+1.0f - timer_in));
			if(txt_gba.getAlpha() <= 0.1f){
				FlxG.fade(Asset.col_white, 1.0f, new FlashTransition(new TitleState()));
			}
		}
	}

}
