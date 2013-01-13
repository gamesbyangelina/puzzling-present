package org.gba.pp.state;

import java.util.LinkedList;
import java.util.List;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTileblock;
import org.gba.pp.Registry;
import org.gba.pp.data.Asset;
import org.gba.pp.model.menu.AbstractButton;
import org.gba.pp.model.menu.BackButton;
import org.gba.pp.survey.QuestionMonitor;

/*
 * This class displays on the initial game load, after the titles.
 * It gives players a chance to switch off the surveys from the start.
 */
public class SurveyAcceptState extends FlxState {

	private QuestionMonitor q1response;
	private List<AbstractButton> buttons = new LinkedList<AbstractButton>(); 

	@Override
	public void create(){
		
		FlxG.setBgColor(Asset.CHRISTMAS_EVE);
		
		FlxText title = new FlxText(0, 10, FlxG.width);
		title.setText("Surveys");
		title.setFormat(Asset.font, 16, Asset.CREAM, "center");
		add(title);
		
		//More strings I didn't localise/externalise
		FlxText main = new FlxText(5, 35, FlxG.width-10);
		main.setText("A Puzzling Present is part of a science experiment about making games. The levels and superpowers"+
					 " in the game were made by a computer, not by a human! You can help us with our experiment by answering"+
					 " short questions after each level.");
		main.setFormat(Asset.font, 8, Asset.CREAM, "center");
		add(main);
		
		FlxState next = new MenuState();
		
		int bw = 120; int bh = 40;
		
		AbstractButton b_off = new BackButton(next, FlxG.width/4 - bw/2, 120, bw, bh, Asset.RED_DARK){

			@Override
			public void click(FlxState s){
				Registry.setScienceMode(false);
				FlxG.switchState(this.targetState);				
			}
			
		};
		add(b_off);
		add(b_off.genText("Disable Surveys", Asset.CREAM));
		buttons.add(b_off);
		AbstractButton b_on = new BackButton(next, FlxG.width/2 + FlxG.width/4 - bw/2, 120, bw, bh, Asset.GREEN_DARK){

			@Override
			public void click(FlxState s){
				Registry.setScienceMode(true);
				FlxG.switchState(this.targetState);				
			}
			
		};
		add(b_on);
		add(b_on.genText("Enable Surveys!", Asset.CREAM));
		buttons.add(b_on);
		
		FlxText anytime = new FlxText(0, FlxG.height - 70, FlxG.width);
		anytime.setText("Don't worry - you can change this setting at any time from the Options screen on the Main Menu.");
		anytime.setFormat(Asset.font, 8, Asset.CREAM, "center");
		add(anytime);
		
		FlxSprite santa = new FlxSprite(FlxG.width/2 - 8, FlxG.height - 32);
		santa.loadGraphic(Asset.santa);
		add(santa);
		FlxTileblock floor = new FlxTileblock(0, FlxG.height-16, FlxG.width, 16);
		floor.loadTiles(Asset.tiles_snow, 16, 16);
		add(floor);
		FlxTileblock holly_left = new FlxTileblock(0, 0, 96, 32);
		holly_left.loadTiles(Asset.spike_strip, 16, 16);
		add(holly_left);
		FlxTileblock holly_right = new FlxTileblock(FlxG.width-96, 0, 96, 32);
		holly_right.loadTiles(Asset.spike_strip, 16, 16);
		add(holly_right);
	}
	
	@Override
	public void update(){
		if(FlxG.mouse.justPressed()){
			int mx, my;
			mx = FlxG.mouse.screenX; my = FlxG.mouse.screenY;
			for(AbstractButton b : buttons){
				b.checkClick(mx, my, this);
			}
		}
	}

}
