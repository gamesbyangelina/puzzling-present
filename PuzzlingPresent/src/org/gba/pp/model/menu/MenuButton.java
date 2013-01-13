package org.gba.pp.model.menu;

import java.util.LinkedList;
import java.util.List;

import org.flixel.FlxG;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.juice.TweenArrive;
import org.gba.pp.state.LevelSelectState;
import org.gba.pp.state.MenuState;
import org.gba.pp.state.WorldState;
import org.gba.pp.survey.QuestionMonitor;

//Button that opens the in-game menu. Had to hack this a bit.
public class MenuButton extends AbstractButton {
	
	WorldState parentState;
	List<FlxObject> toTearDown = new LinkedList<FlxObject>();
	
	public MenuButton(WorldState parent, int x, int y, String sprite){
		super(x, y);
		this.parentState = parent;
		loadGraphic(sprite);
	}

	@Override
	public void click(FlxState s){
		if(parentState.levelComplete || parentState.pauseMenu){
			return;
		}
		
		parentState.pause();
		
		FlxSprite bgCover = new FlxSprite(0,0);
		bgCover.makeGraphic(FlxG.width, FlxG.height, Asset.TRANS_CE);
		parentState.add(bgCover);
		toTearDown.add(bgCover);
		
		FlxText overText = new FlxText(0, -50, FlxG.width, StringBank.mb_paused);
		overText.setFormat(overText.getFont(), 24, Asset.CREAM, "center");
		parentState.add(overText);
		parentState.tweens.add(new TweenArrive(overText, 20, 0.2f));
		toTearDown.add(overText);
		
		//Display a button resuming the game.
		AbstractButton resume = new AbstractButton(FlxG.width/4 - 50, 60){
			@Override
			public void click(FlxState s){
				tearDown();
			}
		};
		resume.makeGraphic(100, 30, Asset.CREAM);
		parentState.add(resume); parentState.incomingButtons.add(resume); toTearDown.add(resume);
		FlxText resumetext = resume.genText(StringBank.mb_resume, Asset.RED_DARK);
		toTearDown.add(resumetext); parentState.add(resumetext);
		//Display a exit to menu button
		AbstractButton mainmenu = new AbstractButton(FlxG.width/2 + FlxG.width/4 - 50, 60){
			@Override
			public void click(FlxState s){
				tearDown();
				parentState.power.revert();
				if(parentState.getWorld() == 9)
					FlxG.switchState(new MenuState());
				else
					FlxG.switchState(new LevelSelectState(parentState.getWorld()));
			}
		};
		mainmenu.makeGraphic(100, 30, Asset.CREAM);
		parentState.add(mainmenu); parentState.incomingButtons.add(mainmenu); toTearDown.add(mainmenu);
		FlxText mainmenutext = mainmenu.genText(StringBank.mb_exit, Asset.RED_DARK);
		toTearDown.add(mainmenutext); parentState.add(mainmenutext);
		
		
		//Display a skip button
		AbstractButton skip = new AbstractButton(FlxG.width/2 - 50, 120){
			@Override
			public void click(FlxState s){
				tearDown();
				parentState.unpause();
				if(parentState.getLevel() == 10)
					FlxG.switchState(new LevelSelectState(parentState.getWorld()));
				else
					parentState.goToNextState();
			}
		};
		skip.makeGraphic(100, 30, Asset.RED_DARK);
		parentState.add(skip); parentState.incomingButtons.add(skip); toTearDown.add(skip);
		FlxText skiptext = skip.genText(StringBank.mb_skip, Asset.CREAM);
		toTearDown.add(skiptext); parentState.add(skiptext);
		
		//Survey time.
		if(Registry.SCIENCE_MODE){
			FlxText sayWhy = new FlxText(0, 160, FlxG.width);
			sayWhy.setText(StringBank.mb_whyskip);
			sayWhy.setFormat(Asset.font, 8, Asset.CREAM, "center");
			parentState.add(sayWhy); toTearDown.add(sayWhy);
			
			int xoff = 40; int width = 20;
			int first_q_height = 175; int q_spacing = 0; float label_scale = 3.5f; int option_spacing = (FlxG.width - 80)/3;
			ToggleButton option1 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
			ToggleButton option2 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
			ToggleButton option3 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
			ToggleButton option4 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
			FlxText subtext;
			subtext = option1.genSubText(StringBank.mb_frustrated, Asset.CREAM, 8, label_scale);
			parentState.add(subtext); toTearDown.add(subtext);
			subtext = (option2.genSubText(StringBank.mb_bored, Asset.CREAM, 8, label_scale));
			parentState.add(subtext); toTearDown.add(subtext);
			subtext = (option3.genSubText(StringBank.mb_complete, Asset.CREAM, 8, label_scale));
			parentState.add(subtext); toTearDown.add(subtext);
			subtext = (option4.genSubText(StringBank.mb_other, Asset.CREAM, 8, label_scale));
			parentState.add(subtext); toTearDown.add(subtext);
			option1.addLinkedButtons(option2, option3, option4); parentState.add(option1); parentState.incomingButtons.add(option1);
			option2.addLinkedButtons(option1, option3, option4); parentState.add(option2); parentState.incomingButtons.add(option2);
			option3.addLinkedButtons(option2, option1, option4); parentState.add(option3); parentState.incomingButtons.add(option3);
			option4.addLinkedButtons(option2, option3, option1); parentState.add(option4); parentState.incomingButtons.add(option4);
			parentState.skipQuitQuestion = new QuestionMonitor(option1, option2, option3, option4);
			toTearDown.add(option1); toTearDown.add(option2); toTearDown.add(option3); toTearDown.add(option4);
		}
		
	}
	
	/*
		For various reasons, the buttons generated by MenuButton were hard to access in order to kill when
		the player closed the menu. So we use a list here to hold onto stuff, MenuButton then tears that list down
		manually when the player unpauses the game.
	*/
	public void tearDown(){
		for(FlxObject fo : toTearDown){
			fo.kill();
		}
		parentState.unpause();
	}

}
