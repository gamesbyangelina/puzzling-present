package org.gba.pp.state;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxObject;
import org.flixel.FlxParticle;
import org.flixel.FlxSound;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTileblock;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.model.Player;
import org.gba.pp.state.trans.FlashTransition;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/*
	This is the first screen you see when you start the game.
*/

public class TitleState extends FlxState {
	
	FlxText bigTitle;
	FlxText pressToStart;
	FlxText titleLesser;
	private FlxEmitter snowcloud;
	private Player santa;
	private FlxTileblock snowlayer;
	private FlxSprite present;
	private boolean started;
	private int introstage;
	private FlxEmitter paper;
	private FlashTransition flashTransition = new FlashTransition(new SurveyAcceptState());
	private FlashTransition flashTransitionOld = new FlashTransition(new MenuState());

	@Override
	public void create(){
		//Game init stuff.
		Asset.create();
		Registry.initRegistry();
		FlxG.setBgColor(Asset.CHRISTMAS_EVE);

		//Start playin' some music
		//I removed these from the git stuff, but please go to www.incompetech.com
		//for some awesome and free music.
		Registry.song = Gdx.audio.newMusic(Gdx.files.internal("holidaywish.mp3"));
		Registry.song.setLooping(true);
		Registry.song.setVolume(Registry.musicVolume/10.0f);
		Registry.song.play();
		Registry.snowfx = new FlxSound(); Registry.snowfx.loadEmbedded(Asset.snd_snowcrunch);
		Registry.snow_fall = new FlxSound(); Registry.snow_fall.loadEmbedded(Asset.snd_snowpunch);
		//Hard to say whether preloading the sound improves things or not. I think it does but, of course,
		//keeping stuff like this loaded at all times is more of a strain on Android (as I understand it)
		Registry.jingle = Gdx.audio.newSound(Gdx.files.internal("jingle.mp3"));
		Registry.jingle_short = Gdx.audio.newSound(Gdx.files.internal("jingle_short.mp3"));
		
		//Tiles layers first
		snowlayer = new FlxTileblock(-128, FlxG.height-48, FlxG.width+128, 48);
		snowlayer.loadTiles(Asset.tiles_snow, 16, 16, 0);
		add(snowlayer);
		FlxTileblock dirtlayers = new FlxTileblock(-64, FlxG.height-32, FlxG.width+64, 32);
		dirtlayers.loadTiles(Asset.tiles_dirt, 16, 16);
		add(dirtlayers);
		
		//Now the text
		titleLesser = new FlxText(0, FlxG.height/3, FlxG.width);
		titleLesser.setAlignment("center");
		titleLesser.setText(StringBank.ts_title_line1);
		titleLesser.setSize(24);
		titleLesser.setFont(Asset.font);
		add(titleLesser);
		
		bigTitle = new FlxText(0, FlxG.height/3 + FlxG.height/6, FlxG.width);
		bigTitle.setAlignment("center");
		bigTitle.setText(StringBank.ts_title_line2);
		bigTitle.setSize(24);
		bigTitle.setFont(Asset.font);
		add(bigTitle);
		
		pressToStart = new FlxText(0, FlxG.height - FlxG.height/6, FlxG.width);
		pressToStart.setAlignment("center");
		//CROSS PLATFORM DEVELOPMENT, EVERYONE!
		if(Gdx.app.getType() == ApplicationType.Android)
			pressToStart.setText(StringBank.ts_beginText);
		else
			pressToStart.setText(StringBank.ts_beginTextDesktop);
		pressToStart.setFont(Asset.font);
		add(pressToStart);
		
		present = new FlxSprite((FlxG.width/2)-16, FlxG.height-48-24);
		present.loadGraphic(Asset.present_icons[0]);
		present.moves = false;
		present.height -= 8;
		present.offset.y += 8;
		add(present);
		
		for(int e=0; e<3; e++){
			FlxEmitter snowcloud = new FlxEmitter(FlxG.width/6 + e*FlxG.width/3, -40, 200);
			snowcloud.setXSpeed(-50, 50);
			snowcloud.setYSpeed(50,150);
			snowcloud.setRotation(0,0);
			add(snowcloud);
			FlxParticle snowflake;
			for (int i = 0; i < 200; i++) 
			{
				snowflake = new FlxParticle();
				snowflake.loadGraphic(Asset.snow, false, false, 2, 2);
				snowcloud.add(snowflake);
			}
			snowcloud.start(false, 10, 0.1f, 0);
		}
		
		paper = new FlxEmitter(-5, -5, 50);
		paper.setXSpeed(-60, 60);
		paper.setYSpeed(-60, -60);
		paper.setRotation(0, 0);
		paper.gravity = 80.0f;
		add(paper);
		FlxParticle strip;
		for(int i=0; i<50; i++){
			strip = new FlxParticle();
			strip.loadGraphic(Asset.strip, false, false, 4, 2);
			paper.add(strip);
		}
		
		santa = new Player(-32, FlxG.height-48-32, null);
		santa.velocity.x = 100;
		santa.attractMode();
		santa.play("walk");
		add(santa);
		
	}		
	
	@Override
	public void update(){
		super.update();
		present.update();
		FlxG.collide(santa, snowlayer);
		FlxG.collide(santa, present);
		FlxG.collide(present, snowlayer);
		FlxG.collide(paper, snowlayer);
		//This is some awful scripting to make the intro sequence.
		if(!started){
			if(introstage == 0 && santa.x >= present.x - 32){
			introstage = 1;
			santa.velocity.y = - 200;
			santa.velocity.x = 0;
			}
			if(introstage == 1 && santa.velocity.y == 0){
				introstage = 2;
				santa.velocity.y = - 200;
				santa.velocity.x = 60;
			}
			if(introstage == 2 && santa.x > present.x+8){
				introstage = 3;
				santa.velocity.x = 0;
				santa.play("tiebow");
				
				paper.x = santa.x + 8;
				paper.y = santa.y + 9;
				paper.start(false, 12, 0.5f, 0);
			}
			if((FlxG.keys.SPACE || FlxG.mouse.justPressed())){
				started = true;
				santa.play("jump");
				santa.y = present.y - 16;
				santa.x = present.x + 8;
				santa.velocity.y = -300;
				santa.velocity.x = 0;
				santa.setFacing(FlxObject.RIGHT);
				
				present.loadGraphic(Asset.presents_open[0]);
				
				//This stuff just checks to see if the game's been started before
				if(!Gdx.files.local("world0data.txt").exists()){
					FileHandle worldOrdering = Gdx.files.local("ordering_world.txt");
					List<Integer> worlds = new LinkedList<Integer>();
					for(int i=0; i<3; i++) worlds.add(i);
					Collections.shuffle(worlds);
					String ws = "";
					for(Integer i : worlds)
						ws += i+",";
					worldOrdering.writeString(ws, false);
					//First time setup! LET'S PARTY GUYS.
					for(int w = 0; w<3; w++){
						FileHandle orderingFile = Gdx.files.local("ordering"+w+".txt");
						List<Integer> options = new LinkedList<Integer>();
						for(int i=0; i<10; i++) options.add(i);
						Collections.shuffle(options);
						String s = "";
						for(Integer i : options)
							s += i+",";
						orderingFile.writeString(s, false);
					}
					FlxG.flash(Asset.CHRISTMAS_EVE, 1.2f, flashTransition); //MenuState
				}
				else
					FlxG.flash(Asset.CHRISTMAS_EVE, 1.2f, flashTransitionOld); //MenuState
			}
		}
	}

}
