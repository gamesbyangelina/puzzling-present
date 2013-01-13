package org.gba.pp.state;

import java.util.LinkedList;
import java.util.List;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.model.menu.BackButton;
import org.gba.pp.model.menu.LegacyBackButton;
import org.gba.pp.model.menu.World;
import org.gba.pp.model.powers.HighJumpPower;
import org.gba.pp.model.powers.InvertGravityPower;
import org.gba.pp.state.trans.FlashTransition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/*
	This is... the main menu, I think.
	That is, the menu after the title screen, where you can choose your world, or go to the options, etc.
*/

public class MenuState extends FlxState {
	
	FlxText levelSelectText;
	List<World> worldSprites = new LinkedList<World>();
	private LegacyBackButton aboutButton;
	private LegacyBackButton optionsButton;
	private BackButton howButton;
	List<FlashTransition> flashTransitions = new LinkedList<FlashTransition>();

	@Override
	public void create(){
		FlxG.setBgColor(Asset.CHRISTMAS_EVE);

		Registry.song.setVolume(Registry.musicVolume/10.0f);
		Registry.jingle_short.stop();
		
		levelSelectText = new FlxText(0, 10, FlxG.width);
		levelSelectText.setText(StringBank.ms_menuTitle);
		levelSelectText.setFormat(Asset.font, 24, Asset.CREAM, "center");
		add(levelSelectText);
		
		//Set up scores if they don't exit.
		int[] scores = new int[3];
		for(int world=0; world<3; world++){
			FileHandle file = Gdx.files.local("world"+world+"data.txt");
			if(!file.exists()){
				if(world == 0){
					file.writeString("0;0;0;0;0;0;0;0;0;0;", false);
					scores[world] = 0;
				}
				else{
					file.writeString("-1;0;0;0;0;0;0;0;0;0;", false);
					scores[world] = -1;
				}
			}
			else{
				String[] ls = file.readString().split(";");
				int i=0; for(String s : ls) i += Integer.valueOf(s);
				scores[world] = i;
			}
			
			flashTransitions.add(new FlashTransition(new LevelSelectState(world)));
		}

		int xoff = FlxG.width/6;
		for(int i=0; i<3; i++){
			World world = null;
			int worldinfo = scores[i];
			
			//Parse the scores from the file system to work out whether worlds are locked/progressed/done.
			if(worldinfo < 0)
				world = new World(xoff + (i*FlxG.width/3) - 16, FlxG.height/5, Asset.present_locked, 1);
			else if(i < Asset.world_icons.length)
				world = new World(xoff + (i*FlxG.width/3) - 16, FlxG.height/5, Asset.world_icons[i], Asset.world_frames[i]);
			else
				world = new World(xoff + (i*FlxG.width/3) - 16, FlxG.height/5);
			world.worldId = i;
			
			if(worldinfo < 0)
				world.unlocked = false;
			else
				world.completedLevels = worldinfo;
			
			worldSprites.add(world);
			add(world);

			String worldString;
			if(!world.unlocked)
				worldString = StringBank.ms_presentLockedString;
			else{
				worldString = world.completedLevels + "/" + world.maxLevels + StringBank.ms_completeString;
			}
			

			FlxText worldText = new FlxText(world.x + world.width/2 - 2, world.y + world.height, FlxG.width/3);
			worldText.setAlignment("center");
			worldText.setText(StringBank.ls_worldtitle+(i+1));
			worldText.setFont(worldText.getFont());
			worldText.x = worldText.x - (worldText.width/2);
			worldText.setColor(Asset.CREAM);
			add(worldText);
			
			FlxText completeTest = new FlxText(world.x + world.width/2 - 2, world.y + world.height + 12, FlxG.width/3);
			completeTest.setAlignment("center");
			completeTest.setText(worldString);
			completeTest.setFont(completeTest.getFont());
			completeTest.x = completeTest.x - (completeTest.width/2);
			completeTest.setColor(Asset.CREAM);
			add(completeTest);
		}
		
		//How to play
		howButton = new BackButton(new WorldState(9, 0, new InvertGravityPower()), FlxG.width/2-(123/2), FlxG.height/2);
		howButton.loadGraphic(Asset.genericLabel);
		add(howButton);
		add(howButton.genText(StringBank.ms_howToPlay, Asset.CHRISTMAS_EVE));
		
		//About box
		aboutButton = new LegacyBackButton(new AboutState(), 0, FlxG.height/2+FlxG.height/5);
		aboutButton.loadGraphic(Asset.label1);
		FlxText aboutText = new FlxText(42, FlxG.height/2+FlxG.height/5 + 16, 110);
		aboutText.setText(StringBank.ms_aboutGameString);
		aboutText.setAlignment("center");
		aboutText.setFont(Asset.font);
		aboutText.setColor(Asset.CREAM);
		add(aboutButton);
		add(aboutText);
		
		//options box
		optionsButton = new LegacyBackButton(new SettingsState(), FlxG.width/2, FlxG.height/2+FlxG.height/5);
		optionsButton.loadGraphic(Asset.label2);
		FlxText howText = new FlxText(FlxG.width/2+7, FlxG.height/2+FlxG.height/5 + 16, 100);
		howText.setText(StringBank.ms_optionsString);
		howText.setAlignment("center");
		howText.setFont(Asset.font);
		howText.setColor(Asset.CREAM);
		add(optionsButton);
		add(howText);
		
		FlxText link = new FlxText(0, FlxG.height-16, FlxG.width, "More games online at http://www.gamesbyangelina.org");
		link.setFormat(Asset.font, 8, Asset.CREAM, "center");
		add(link);
		
	}
	
	@Override
	public void update(){
		super.update();
		
		if(FlxG.mouse.justPressed()){
			final int mx = FlxG.mouse.screenX;
			final int my = FlxG.mouse.screenY;
			
			//This was eventually wrapped up into some code in AbstractButton I think.
			//Kind of a mishmash of encapsulated/reused code and not. I'm still learning and trying
			//to improve the care I take with code/planning.
			if(mx > optionsButton.x && mx < optionsButton.x+optionsButton.width
					&& my > optionsButton.y && my < optionsButton.y + optionsButton.height){
				optionsButton.click();
			}
			
			if(mx > aboutButton.x && mx < aboutButton.x+aboutButton.width
					&& my > aboutButton.y && my < aboutButton.y + aboutButton.height){
				aboutButton.click();
			}
			
			howButton.checkClick(mx, my, this);
			
			for(World world : worldSprites){
				if(world.unlocked && mx > world.x && mx < world.x + world.width
						&& my > world.y && my < world.y + world.height){
					FlxG.fade(0xFFFFFFFF, 0.3f, flashTransitions.get(world.worldId));
				}
			}
		}
	}

}
