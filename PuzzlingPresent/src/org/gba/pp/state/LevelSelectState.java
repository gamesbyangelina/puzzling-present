package org.gba.pp.state;

import java.util.LinkedList;
import java.util.List;

import org.flixel.FlxG;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTileblock;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.model.menu.LegacyBackButton;
import org.gba.pp.model.menu.LegacyButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/*
	This is the class that displays the ten levels plus tutorial, back button, and so on.
	Includes some awful code I wrote to display the boxes in a pyramid.
*/
public class LevelSelectState extends FlxState {
	
	private int worldId;
	
	private List<LegacyButton> legacyButtons = new LinkedList<LegacyButton>();

	//There's probably a programmer joke in here somewhere
	private int real_world;

	public LevelSelectState(int id){
		this.worldId = id;
	}

	@Override
	public void create(){
		FlxG.setBgColor(Asset.CHRISTMAS_EVE);
		
		FileHandle worldOrdering = Gdx.files.local("ordering_world.txt");
		real_world = Integer.parseInt(worldOrdering.readString().split(",")[worldId]);
		
		FlxTileblock tree = new FlxTileblock(0, 0, FlxG.width, 80);
		tree.makeGraphic(FlxG.width, 60, Asset.GREEN_DARK);
		add(tree);
		FlxSprite baubel = new FlxSprite(10, 30);
		baubel.loadGraphic(Asset.baubel);
		add(baubel);
		baubel = new FlxSprite(255, 38);
		baubel.loadGraphic(Asset.baubel);
		add(baubel);
		
		FileHandle file = Gdx.files.local("world"+worldId+"data.txt");
		if(!file.exists()){
			file.writeString("0;0;0;0;0;0;0;0;0;0;", false);
		}
		String[] scores = file.readString().split(";");
		int complete = 0;
		for(String s : scores){
			if(s.equals("1"))
				complete++;
		}
		
		int pres_sz = 48;
		
		//Okay, as per this-is-why-you-write-code-clearly, I can no longer remember how this even works.
		//Because I wanted to number them in order, and display them in a pyramid, I do some awful 
		//multiple-for-loop nonsense. I don't recommend reading...
		int levelnum = 9;
		int stack_left_y = FlxG.height-(int)(pres_sz*3.25); int stack_left_x = FlxG.width/2 - pres_sz/2;
		for(int i=0; i<1; i++){
			addButton(stack_left_x, stack_left_y, pres_sz, i, levelnum+1, scores[levelnum]);
		}
		levelnum -= 1;
		stack_left_y += pres_sz - 12; stack_left_x -= pres_sz/2;
		for(int i=0; i<2; i++){
			addButton(stack_left_x, stack_left_y, pres_sz, i, levelnum+1-(1-i), scores[levelnum-(1-i)]);
		}
		levelnum -= 2;
		stack_left_y += pres_sz - 12; stack_left_x -= pres_sz/2;
		for(int i=0; i<3; i++){
			addButton(stack_left_x, stack_left_y, pres_sz, i, levelnum+1-(2-i), scores[levelnum-(2-i)]);
		}
		levelnum -= 3;
		stack_left_y += pres_sz - 12; stack_left_x -= pres_sz/2;
		for(int i=0; i<4; i++){
			addButton(stack_left_x, stack_left_y, pres_sz, i, levelnum+1-(3-i), scores[levelnum-(3-i)]);
			
		}
		
		FlxText worldText = new FlxText(0, 10, FlxG.width);
		worldText.setText(StringBank.ls_worldtitle+(worldId+1)+" - "+complete+"/10 "+StringBank.ms_completeString);
		worldText.setFormat(Asset.font, 16, Asset.CREAM, "center");
		add(worldText);
		FlxText levelSelectText = new FlxText(0, 35, FlxG.width);
		levelSelectText.setText(StringBank.ls_levelSelect);
		levelSelectText.setFormat(Asset.font, 8, Asset.CREAM, "center");
		add(levelSelectText);
		
		//Back button
		LegacyButton backButton = new LegacyBackButton(new MenuState(), 10, FlxG.height-48);
		backButton.loadGraphic(Asset.levelicon);
		legacyButtons.add(backButton);
		add(backButton);
		FlxText back = new FlxText(8, FlxG.height-26, 42);
		back.setFormat(Asset.font, 8, Asset.CREAM, "center");
		back.setText(StringBank.ls_back);
		add(back);
		
		//Tutorial level
		LegacyButton howButton = new LegacyBackButton(new WorldState(worldId, 10, Registry.powers[real_world]), FlxG.width-58, FlxG.height-48);
		howButton.loadGraphic(Asset.levelicon);
		legacyButtons.add(howButton);
		add(howButton);
		FlxText number = new FlxText(FlxG.width-60, FlxG.height-26, 42);
		number.setFormat(Asset.font, 8, Asset.CREAM, "center");
		number.setText(StringBank.ls_howToPlay);
		add(number);
		
		FlxSprite santa = new FlxSprite(FlxG.width-50, FlxG.height-52);
		santa.loadGraphic("pack:santa-lone", false, true);
		santa.setFacing(FlxObject.LEFT);
		add(santa);
	}
	
	//Convenience for the awful code above
	public void addButton(int x, int y, int size, int row, int num, String score){
		LegacyButton legacyButton = new LegacyBackButton(new WorldState(worldId, num-1, Registry.powers[real_world]), x + (row*size), y);
		legacyButton.loadGraphic(Asset.levelicon);
		legacyButtons.add(legacyButton);
		add(legacyButton);
		
		FlxText number = new FlxText(x+(row*size) - 2, y+size/3 - 2, size-6);
		number.setFormat(Asset.font, 16, Asset.CREAM, "center");
		number.setText(""+num);
		add(number);
		
		FlxSprite test = new FlxSprite(x+(row*size)+18, (y+size/3)+23);
		if(score.equals("0"))
			test.loadGraphic("pack:question");
		else
			test.loadGraphic("pack:tick");
		add(test);
	}
	
	@Override
	public void update(){
		
		if(FlxG.mouse.justPressed()){
			int mx = FlxG.mouse.screenX;
			int my = FlxG.mouse.screenY;
			for(LegacyButton b : legacyButtons){
				if(mx > b.x && mx < b.x + b.width && my > b.y && my < b.y + b.height){
					b.click();
					return;
				}
			}
		}
		
	}

}
