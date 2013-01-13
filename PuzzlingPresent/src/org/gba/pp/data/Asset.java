package org.gba.pp.data;

import org.flixel.FlxText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset {
	
	//Colours - "Tis The Season" from http://www.colourlovers.com/palette/130451/Tis_the_Season
	public static final int RED_DARK = 0xff941f1f;
	public static final int RED_LITE = 0xffCE6b5d;
	public static final int CREAM = 0xffFFEFB9;
	public static final int TRANS_CREAM = 0x99FFEFB9;
	public static final int GREEN_LITE = 0xff7B9971;
	public static final int GREEN_DARK = 0xff34502B;
	//"Fiber Optic Angel" http://www.colourlovers.com/palette/629120/Fiber_Optic_Angel
	public static final int CHRISTMAS_EVE = 0xff2F2D3E;
	public static final int TRANS_CE = 0xBB2F2D3E;
	
	public static final String snow = "pack:snow";
	
	public static String[] present_icons = new String[2];
	public static String[] presents_open = new String[1];
	
	public static String[] world_icons = new String[3];
	public static int[] world_frames = {1,1,1};
	public static String present_antigrav;
	public static String present_locked;
	
	public static String tiles_default;
	public static String tiles_snow;
	public static String tiles_dirt;
	//Font removed for open sourcing, available on dafont
//	public static final String font = "PixelSleigh.ttf";// = Gdx.files.internal("PixelSleigh.ttf");
	
	public static String player_strip;
	public static String exit_strip;
	public static String spike_strip;
	
	public static String about_screen_1;
	public static final String button_plus = "pack:btn-plus";
	public static final String button_minus = "pack:btn-minus";
	
	public static final String label1 = "pack:label1";
	public static final String label2 = "pack:label2";
	public static final String levelicon = "pack:levelicon";
	
	public static final String snd_snowpunch = "snowpunch.mp3";
	public static final String snd_snowcrunch = "snowcrunch.mp3";
	public static final String santa = "pack:santa-lone";
	public static final String strip = "pack:strip";
	public static int col_black = 0xff000000;
	public static int col_white = 0xffffffff;
	public static final String menubutton = "pack:menubutton";
	public static final String baubel = "pack:baubel";
	public static final String candycane = "pack:candycane";
	public static final String genericLabel = "pack:labelplain";
	public static final String toggleStrip = "pack:togglestrip";
	public static final String nextbtn = "pack:nextbutton";
	
	public static void create()
	{			
		tiles_default = ("pack:autotiles");
		tiles_dirt = ("pack:tiles-dirt");
		tiles_snow = ("pack:tiles-snowtop");
		
		present_antigrav = "pack:present-1";
		world_icons[0] = "pack:present-1";
		world_icons[1] = "pack:present-2";
		world_icons[2] = present_antigrav;
		
		present_icons[0] = ("pack:pres1");
		presents_open[0] = ("pack:pres1open");
		present_icons[1] = ("pack:pres2");
		
		player_strip = ("pack:santa");
		exit_strip = ("pack:exit");
		spike_strip = ("pack:holly1");
		
		present_locked = ("pack:present-lock");
		
		about_screen_1 = ("pack:about_screen_1");
	}

}
