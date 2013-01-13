package org.gba.pp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;

import org.flixel.FlxSound;
import org.gba.pp.model.Player;
import org.gba.pp.model.powers.ElasticPower;
import org.gba.pp.model.powers.HighJumpPower;
import org.gba.pp.model.powers.InvertGravityPower;
import org.gba.pp.model.powers.LowGravityPower;
import org.gba.pp.model.powers.Power;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class Registry {

	private static final String NEWLINE = "\n";

	private static final String COLON = ":";

	public static UUID uuid;

	public static Player player;
	public static Power[] powers;
	public static int offset = 0;
	public static int worlds = 1;
	public static String logString = "";

	public static Music song;
	public static Sound jingle;
	public static Sound jingle_short;
	public static FlxSound snowfx;
	public static FlxSound snow_fall;
	public static int musicVolume = 10;

	public static final int UNLOCK_LIMIT = 5;
	public static final int LEVELS_PER_WORLD = 10;
	public static boolean SCIENCE_MODE = true;

	private static long localTime;

	public static void initRegistry(){
		//Please don't judge me it was a hard week.
		powers = new Power[10];
		powers[0] = new InvertGravityPower();
		powers[1] = new HighJumpPower();
		powers[2] = new ElasticPower();
		powers[9] = new InvertGravityPower();

		FileHandle settingsFile = Gdx.files.local("options.txt");
		if (!settingsFile.exists()) {
			String optionsString = "uuid:" + UUID.randomUUID().toString() + NEWLINE;
			optionsString += "mv:10\n";
			optionsString += "sm:1\n";
			settingsFile.writeString(optionsString, false);
		}
		String[] settings = settingsFile.readString().split(NEWLINE);
		uuid = UUID.fromString(settings[0].split(COLON)[1]);
		musicVolume = Integer.valueOf(settings[1].split(COLON)[1]);
		SCIENCE_MODE = Integer.valueOf(settings[2].split(COLON)[1]) == 1;
		
		localTime = System.currentTimeMillis();
	}

	public static void setMusicVolume(int vol){
		if (vol != musicVolume) {
			FileHandle settingsFile = Gdx.files.local("options.txt");
			String[] settings = settingsFile.readString().split(NEWLINE);
			settings[1] = "mv:" + vol;
			String newSettings = "";
			for(String s : settings){
				newSettings += s+NEWLINE;
			}
			settingsFile.writeString(newSettings, false);
			musicVolume = vol;
			if (song != null)
				song.setVolume((float) vol / 10);
		}
	}
	
	public static void setScienceMode(boolean setTo){
		FileHandle settingsFile = Gdx.files.local("options.txt");
		String[] settings = settingsFile.readString().split(NEWLINE);
		settings[2] = "sm:" + (setTo ? "1" : "0");
		String newSettings = "";
		for(String s : settings){
			newSettings += s+NEWLINE;
		}
		settingsFile.writeString(newSettings, false);
		SCIENCE_MODE = setTo;
	}

}
