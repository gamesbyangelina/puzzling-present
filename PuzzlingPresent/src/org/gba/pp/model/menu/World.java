package org.gba.pp.model.menu;

import org.flixel.FlxSprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

//Refers to the tiny clickable world icons in MenuState
//They were originally going to be animated, but I couldn't think of a good animation for 'jump higher'.
//#gamedev
public class World extends FlxSprite{
	
	public boolean unlocked = true;
	public int maxLevels = 10;
	public int completedLevels;
	public int worldId;
	
	public World(int p, int q, String t, int frames){
		super(p,q);
		if(frames > 1){
			loadGraphic(t, true, false, 32, 32);
			int[] animation = new int[frames];
			for(int i=0; i<animation.length; i++){
				animation[i] = i;
			}
			addAnimation("show", animation, 6, true);
			play("show");
		}
		else{
			loadGraphic(t);
		}	
	}
	
	public World(int i, int j){
		super(i,j);
		makeGraphic(32, 32);
	}
	
	public void update(){
		super.update();
	}

}
