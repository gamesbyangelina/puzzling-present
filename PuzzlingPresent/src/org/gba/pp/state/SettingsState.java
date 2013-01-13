package org.gba.pp.state;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTileblock;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.model.menu.AbstractButton;
import org.gba.pp.model.menu.BackButton;
import org.gba.pp.model.menu.UnlockButton;

/*
	The options menu
*/

public class SettingsState extends FlxState {

	private UnlockButton unlockB;
	private ScienceModeButton scienceB;
	private BackButton back;
	private AbstractButton volumeUp;
	private AbstractButton volumeDown;
	private FlxText musicLabel;
	private float currentVolume;

	@Override
	public void create(){
		FlxG.setBgColor(Asset.CHRISTMAS_EVE);
		
		FlxText menuText = new FlxText(0, 10, FlxG.width);
		menuText.setText(StringBank.ss_stateTitle);
		menuText.setFormat(Asset.font, 16, Asset.CREAM, "center");
		add(menuText);
		
		//Options: Unlock everything.
		FlxText unlockDescription = new FlxText(FlxG.width/12, 40, (int) 2*FlxG.width/6, StringBank.ss_unlockDesc);
		unlockDescription.setFormat(Asset.font, 8, Asset.CREAM, "center");
		add(unlockDescription);
		
		int wx = FlxG.width/4 + FlxG.width/2 - FlxG.width/6;//3*FlxG.width/6
		unlockB = new UnlockButton(wx, 40, 2*FlxG.width/6, 40, Asset.GREEN_DARK, Asset.GREEN_LITE);
		add(unlockB);
		add(unlockB.genText(StringBank.ss_unlockLabel_off, Asset.CREAM));
		
		//Options: Science Mode on/off
		FlxText scienceModeDescription = new FlxText(FlxG.width/12, 90, (int) 2*FlxG.width/6, StringBank.ss_surveyDesc);
		scienceModeDescription.setFormat(Asset.font, 8, Asset.CREAM, "center");
		add(scienceModeDescription);
		
		scienceB = new ScienceModeButton(wx, 90, 2*FlxG.width/6, 40, Asset.RED_DARK, Asset.RED_LITE);
		add(scienceB);
		if(Registry.SCIENCE_MODE)
			add(scienceB.genText(StringBank.ss_surveyLabel_on, Asset.CREAM));
		else
			add(scienceB.genText(StringBank.ss_surveyLabel_off, Asset.CREAM));
		
		back = new BackButton(new MenuState(), 0, 0);
		add(back);
		
		//Options: Adjust music volume.
		//Not a great implementation. Ideally you'd want a slider or something - this might be possible to overlay
		//on top of LibGDX/flixel-android but I have no idea. I went for the quick and easy option.
		volumeDown = new AbstractButton(FlxG.width/4 - 20, 150){

			@Override
			public void click(FlxState s){
				if(Registry.musicVolume > 0){
					Registry.setMusicVolume(Registry.musicVolume - 1);
				}
			}
			
		};
		volumeDown.loadGraphic(Asset.button_minus);
		add(volumeDown);
		
		musicLabel = new FlxText(0, 150, FlxG.width);
		musicLabel.setText(StringBank.ss_musicVolume+((Registry.musicVolume*10))+"%");
		currentVolume = Registry.musicVolume;
		musicLabel.setFormat(Asset.font, 8, Asset.CREAM, "center");
		add(musicLabel);
		
		volumeUp = new AbstractButton(FlxG.width/2 + FlxG.width/4 - 20, 150){

			@Override
			public void click(FlxState s){
				if(Registry.musicVolume < 10){
					Registry.setMusicVolume(Registry.musicVolume + 1);
				}
			}
			
		};
		volumeUp.loadGraphic(Asset.button_plus);
		add(volumeUp);
		
		//Decoration at the bottom of the screen.
		FlxSprite santa = new FlxSprite(FlxG.width/2 - 8 - 32, FlxG.height - 32);
		santa.loadGraphic(Asset.santa);
		add(santa);
		FlxSprite present = new FlxSprite(FlxG.width/2 - 8 + 32, FlxG.height - 32);
		present.loadGraphic(Asset.exit_strip);
		add(present);
		FlxTileblock floor = new FlxTileblock(0, FlxG.height-16, FlxG.width, 16);
		floor.loadTiles(Asset.tiles_snow, 16, 16);
		add(floor);
		FlxTileblock holly_left = new FlxTileblock(64, FlxG.height-32, 32, 16);
		holly_left.loadTiles(Asset.spike_strip, 16, 16);
		add(holly_left);
		FlxTileblock holly_right = new FlxTileblock(FlxG.width-96, FlxG.height-32, 32, 16);
		holly_right.loadTiles(Asset.spike_strip, 16, 16);
		add(holly_right);
	}
	
	@Override
	public void update(){
		if(FlxG.keys.ESCAPE){
			FlxG.switchState(new MenuState());
		}
		
		if(FlxG.mouse.justPressed()){
			back.checkClick(FlxG.mouse.screenX, FlxG.mouse.screenY, this);
			
			unlockB.checkClick(FlxG.mouse.screenX, FlxG.mouse.screenY, this);
			scienceB.checkClick(FlxG.mouse.screenX, FlxG.mouse.screenY, this);
			
			volumeDown.checkClick(FlxG.mouse.screenX, FlxG.mouse.screenY, this);
			volumeUp.checkClick(FlxG.mouse.screenX, FlxG.mouse.screenY, this);
		}
		
		if(currentVolume != Registry.musicVolume){
			musicLabel.kill();
			musicLabel = new FlxText(0, 150, FlxG.width);
			int percentage = (Registry.musicVolume*10);
			musicLabel.setText(StringBank.ss_musicVolume+percentage+"%");
			currentVolume = Registry.musicVolume;
			musicLabel.setFormat(Asset.font, 8, Asset.CREAM, "center");
			add(musicLabel);
		}
	}
	
}
