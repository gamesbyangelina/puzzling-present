package org.gba.pp;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.flixel.FlxGame;
import org.gba.pp.state.*;

import com.badlogic.gdx.Gdx;

public class PuzzlingPresentMain extends FlxGame{
	
	public PuzzlingPresentMain()
	{
		super(320, 240, TitleState.class, 3, 30, 30, true, 960, 720);
	}
	
	public PuzzlingPresentMain(int x, int y){
		super(320, 240, TitleState.class, 3, 30, 30, true, x, y);
	}

	@Override
	public void create()
	{
		super.create();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
}
