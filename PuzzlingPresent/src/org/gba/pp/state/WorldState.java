package org.gba.pp.state;

import java.util.LinkedList;
import java.util.List;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxGamePad;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxParticle;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTilemap;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.juice.TweenTarget;
import org.gba.pp.model.Exit;
import org.gba.pp.model.Player;
import org.gba.pp.model.Spike;
import org.gba.pp.model.actions.EndLevel;
import org.gba.pp.model.actions.SpikeCollision;
import org.gba.pp.model.menu.AbstractButton;
import org.gba.pp.model.menu.MenuButton;
import org.gba.pp.model.powers.Power;
import org.gba.pp.state.trans.FlashTransition;
import org.gba.pp.survey.QuestionMonitor;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntArray;

/*
	The WorldState - the main game level state.
	Lots of game logic here. This was initially more elegant but hacks crept in more and more during the crunch.
	Apologies for any obvious idiocy.
*/

public class WorldState extends FlxState {

	public Power power;
	private int world;
	private int level;
	private FlxTilemap map;
	private FlxGroup spikes;

	public FlxGamePad _pad;
	public Exit exit;

	public FlxGroup snowpuffs = new FlxGroup();
	public int lastused = 0;
	float snowinterval = 0.25f;
	float snowtimer = 0.25f;
	boolean wasairborne = false;
	private boolean crunching;

	public List<TweenTarget> tweens = new LinkedList<TweenTarget>();
	public boolean levelComplete;
	public boolean playerDead;

	public QuestionMonitor q1response;
	public QuestionMonitor q2response;
	public QuestionMonitor skipQuitQuestion;
	public boolean logging = true;
	private boolean debugmode = true;
	private boolean continue_pressed;
	private boolean isTutorial = false;

	public List<AbstractButton> buttons = new LinkedList<AbstractButton>();
	public List<AbstractButton> incomingButtons = new LinkedList<AbstractButton>();
	public boolean pauseMenu;
	private FlxText tutorialText;
	private boolean xWasPressed = false;
	public static boolean xPressed = false;
	private MenuButton menuButton;
	private AbstractButton restartButton;
	private int real_level;
	private int real_world;
	public int numpuffs;
	private int numpoofs;

	public EndLevel i_endLevel; 
	public SpikeCollision i_spikeCollision; 
	private int initial_x;
	private int initial_y;
	
	public static FlxText powerStatus;
	public static String powerOffText;
	public static String powerOnText;
	
	//Tutorial levels don't use world/level indices
	public WorldState(Power p){
		this.power = p;
		isTutorial = true;
	}

	//Specify what world or level we're on in the general case.
	public WorldState(int world, int level, Power p){
		this.world = world;
		this.level = level;
		this.power = p;
	}

	//One thing I learnt later is that you should instantiate as much as you can in the create() method.
	//Otherwise Android slowly died a memory death and you get lots of bad reviews. Live and learn.
	@Override
	public void create(){
		i_endLevel = new EndLevel(this);
		i_spikeCollision = new SpikeCollision(this);
		
		//Code like this is mostly "I've lost track of the traces through the game, let's just do this
		//to make sure the music isn't playing."
		Registry.song.setVolume(Registry.musicVolume / 10.0f);
		Registry.jingle_short.stop();
		
		//I forget which is which. One of them is a tiny burst of snow from walking, one is from jumping. Technical terms, obv.
		numpuffs = 6; numpoofs = 40;

		FlxG.setBgColor(Asset.CHRISTMAS_EVE);

		spikes = new FlxGroup();

		//10 is the level ID for the world tutorial. It shows what the power is and gives a stock description.
		if (level == 10) {
			FlxText name = new FlxText(16, 16, FlxG.width - 32);
			name.setFormat(Asset.font, 16, Asset.CREAM, "center");
			name.setText(power.getName());
			add(name);

			FlxText desc = new FlxText(16, 36, FlxG.width - 32);
			desc.setFormat(Asset.font, 8, Asset.CREAM, "center");
			desc.setText(power.getDescription());
			add(desc);
		}

		//Particle effects! 
		for (int e = 0; e < 2; e++) {
			FlxEmitter snowcloud = new FlxEmitter((e * FlxG.width / 2), -40, 100);
			snowcloud.setXSpeed(-50, 50);
			snowcloud.setYSpeed(50, 150);
			snowcloud.setRotation(0, 0);
			snowcloud.width = FlxG.width / 2;
			add(snowcloud);
			FlxParticle snowflake;
			for (int i = 0; i < 100; i++) {
				snowflake = new FlxParticle();
				snowflake.loadGraphic(Asset.snow, false, false, 2, 2);
				snowcloud.add(snowflake);
			}
			snowcloud.start(false, 10, 0.1f, 0);
		}

		// Load the level data.
		FileHandle levelData;
		//The ordering code is used to randomise the order in which the player experiences levels.
		//Yes, this is why there seems to be no difficulty curve when you play the game.
		if (!isTutorial && level <= 9 && world < 9) {
			FileHandle worldOrdering = Gdx.files.local("ordering_world.txt");
			real_world = Integer.parseInt(worldOrdering.readString().split(",")[world]);
			FileHandle orderingFile = Gdx.files.local("ordering"+real_world+".txt");
			real_level = Integer.parseInt(orderingFile.readString().split(",")[level]);
			levelData = Gdx.files.internal("worlddata/world" + real_world + "level" + real_level + ".txt");
			if (!levelData.exists()) {
				//Graceful what now?
				throw new IllegalStateException("SEVERE: Missing level data for level " + real_level + " in world " + real_world);
			}
		}
		else if(world == 9){
			real_level = level;
			real_world = world;
			levelData = Gdx.files.internal("worlddata/world" + world + "level" + level + ".txt");
			if (!levelData.exists()) {
				throw new IllegalStateException("SEVERE: Missing level data for level " + level + " in world " + world);
			}
		}
		else {
			levelData = Gdx.files.internal("worlddata/tutorial.txt");
			real_level = 10;
			FileHandle worldOrdering = Gdx.files.local("ordering_world.txt");
			real_world = Integer.parseInt(worldOrdering.readString().split(",")[world]);
			if (!levelData.exists()) {
				throw new IllegalStateException("SEVERE: Missing level data for tutorial level.");
			}
		}

		//Normally, levels in Flixel are described as integer arrays, and only specify where tiles go.
		//I added a few characters in to describe player locations, spikes and the exit. This processes that.
		String levelAsRawString = levelData.readString();
		int[] rawdata = new int[15 * 20];
		String[] lines = levelAsRawString.split("\n");
		int index = 0;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 20; j++) {
				char c = (lines[i].subSequence(j, j + 1)).charAt(0);
				if (c == 'X') {
					exit = new Exit(j * 16, i * 16);
				}
				else if (c == 'S') {
					Registry.player = new Player(j * 16, i * 16, power);
					initial_x = j*16; initial_y = i*16;
				}
				else if (c == 'V') {
					spikes.add(new Spike(j * 16, i * 16));
				}
				else {
					rawdata[index] = Integer.valueOf(lines[i].substring(j, j + 1));
				}
				index++;
			}
		}

		//Add in the level and other bits.
		map = new FlxTilemap();
		IntArray ia = new IntArray(rawdata);
		map.loadMap(FlxTilemap.arrayToCSV(ia, 20), Asset.tiles_default, 16, 16, FlxTilemap.AUTO);
		add(map);

		add(spikes);

		if (exit == null)
			FlxG.switchState(new LevelSelectState(world));
		add(exit);
		add(Registry.player);

		//Turn down the particle effects for Android users.
		//Was originally going to make this device-dependent, but...
		if(Gdx.app.getType() == ApplicationType.Android){
			numpuffs = 3; numpoofs = 30;
		}
		for (int i = 0; i < numpuffs; i++) {
			FlxEmitter fe = new FlxEmitter(0, 0, numpoofs);
			fe.setYSpeed(-40, -100);
			fe.setXSpeed(-100, 100);
			fe.setRotation(0, 0);
			fe.gravity = 150;
			fe.bounce = 0.1f;
			
			FlxParticle snowflake;
			for (int j = 0; j < 40; j++) {
				snowflake = new FlxParticle();
				snowflake.loadGraphic(Asset.snow, false, false, 1 + j % 2, 1 + j % 2);
				fe.add(snowflake);
			}
			
			snowpuffs.add(fe);
		}
		add(snowpuffs);

		power.init();

		menuButton = new MenuButton(this, 0, 0, Asset.menubutton);
		add(menuButton);

		restartButton = new AbstractButton(FlxG.width - 48, 0) {

			@Override
			public void click(FlxState s){
				restartState();
				return;
			}

		};

		restartButton.loadGraphic("pack:retrybutton");
		add(restartButton);

		if (Gdx.app.getType() == ApplicationType.Android) {
			// The gamepad is added as last and will be in the front.
			_pad = new FlxGamePad(FlxGamePad.LEFT_RIGHT, FlxGamePad.A_B);
			_pad.setAlpha(0.5f);
			_pad.setDPadPositionY(12);
			_pad.setActionPositionY(12);
			Registry.player.setPad(_pad);
			add(_pad);
		}

		/*
		 * At the last minute I realised we needed a tutorial to show you the
		 * controls. This text guides the player through three tutorial levels,
		 * accessed through the "How To Play" button on the main menu. They are
		 * listed as world9level0-2 in the world data.
		 */
		if (world == 9) {
			if (level == 0) {
				tutorialText = new FlxText(0, 32, FlxG.width);
				tutorialText.setFormat(Asset.font, 8, Asset.CREAM, "center");
				if (Gdx.app.getType() == ApplicationType.Android)
					tutorialText.setText(StringBank.ws_tutorial1);
				else
					tutorialText.setText(StringBank.ws_tutorial1_desktop);
				add(tutorialText);
			}
			else if (level == 1) {
				tutorialText = new FlxText(0, 32, FlxG.width);
				tutorialText.setFormat(Asset.font, 8, Asset.CREAM, "center");
				if (Gdx.app.getType() == ApplicationType.Android)
					tutorialText.setText(StringBank.ws_tutorial2);
				else
					tutorialText.setText(StringBank.ws_tutorial2_desktop);
				add(tutorialText);
			}
			else if (level == 2) {
				xPressed = false;
				tutorialText = new FlxText(0, 64, FlxG.width);
				tutorialText.setFormat(Asset.font, 8, Asset.CREAM, "center");
				if (Gdx.app.getType() == ApplicationType.Android)
					tutorialText.setText(StringBank.ws_tutorial3);
				else
					tutorialText.setText(StringBank.ws_tutorial3_desktop);
				add(tutorialText);
			}
		}
		
		//Added this later after suggestions from players
		powerStatus = new FlxText(0, 0, FlxG.width);
		powerStatus.setFormat(Asset.font, 8, Asset.CREAM, "center");
		
		//As you can see, Strings crept in that weren't put in the StringBank.
		powerOffText = power.getName()+": OFF [";
		if(Gdx.app.getType() == ApplicationType.Android)
			powerOffText += "B";
		else
			powerOffText += "X";
		powerOffText += " to switch]";
		powerOnText = powerOffText.replaceFirst("OFF", "ON");
		
		powerStatus.setText(powerOffText);
		add(powerStatus);
	}

	@Override
	public void update(){
		
		/*
		 * At the last minute it turned out that *actually running* Flixel, elasticity has implementation issues.
		 * This clamps velocity to stop the player accelerating out of the screen in-between update() calls.
		 */
		if(Registry.player.velocity.y > 500)
			Registry.player.velocity.y = 500;
		else if(Registry.player.velocity.y < -500)
			Registry.player.velocity.y = -500;

		// Oh god this is the worst thing.
		if (world == 9 && level == 2 && xPressed != xWasPressed) {
			xWasPressed = xPressed;
			// Seriously, look at that code
			// I am too tired to workaround this.
			tutorialText.kill();
			tutorialText = new FlxText(0, 64, FlxG.width);
			tutorialText.setFormat(Asset.font, 8, Asset.CREAM, "center");
			if (Gdx.app.getType() == ApplicationType.Android)
				tutorialText.setText(StringBank.ws_tutorial3_2);
			else
				tutorialText.setText(StringBank.ws_tutorial3_2_desktop);
			add(tutorialText);
		}

		//I hacked this tween stuff together, don't look at it too closely.
		//But if things need tweening, tween them a bit.
		for (TweenTarget t : tweens) {
			if(t.needsTweening)
				t.doTween();
		}

		//Mouse presses are also how taps are interpreted in GDX.
		//Multiple taps are accessed through a list of mouse presses somewhere in FlxG.mouse
		if (FlxG.mouse.justPressed()) {
			int mx = FlxG.mouse.screenX;
			int my = FlxG.mouse.screenY;
			for (AbstractButton b : buttons) {
				b.checkClick(mx, my, this);
			}
			if(!levelComplete){
				menuButton.checkClick(mx, my, this);
				restartButton.checkClick(mx, my, this);
			}
			//Some button presses add buttons to the state. But because of the way I'm iterating through buttons,
			//this causes Java to explode. This is a pretty terrible solution, but I have a list of buttons that
			//are waiting to be added. Add them here.
			for (AbstractButton b : incomingButtons) {
				buttons.add(b);
			}
			incomingButtons.clear();
		}

		if (pauseMenu) {
			if (FlxG.keys.justPressed("ESCAPE")) {
				menuButton.tearDown();
			}
			return;
		}
		// Everything below here WILL NOT HAPPEN WHEN PAUSED //Yes, thanks, Past Me.

		//A lot of what follows is very bad state-machine stuff that got very tangled. Restarts/pauses/continues/deaths/transitions
		//all in a big mess.

		if (FlxG.keys.justPressed("ESCAPE")) {
			menuButton.click(this);
			for (AbstractButton b : incomingButtons) {
				buttons.add(b);
			}
			incomingButtons.clear();
		}

		if (levelComplete && (FlxG.keys.justPressed("X") || FlxG.keys.justPressed("SPACE"))) {
			goToNextState();
			return;
		}

		if (!levelComplete) {
			super.update();
			exit.update();
		}

		// If the player's dead, don't perform map collisions (mario-style wipe)
		if (!playerDead) {
			FlxG.collide(map, Registry.player);
		}
		// If the player IS dead, and a restart has been requested
		if (playerDead && (FlxG.mouse.justPressed() || FlxG.keys.R || FlxG.keys.justPressed("SPACE"))) {
			playerDead = false;
			
			restartState();
			return;
		}
		// If the level is complete and the player is asking to go to the next
		// level/world
		if (!continue_pressed && levelComplete && (FlxG.keys.X || FlxG.keys.justPressed("SPACE"))) {
			return;
		}

		if (!levelComplete && !continue_pressed && debugmode && FlxG.keys.Q) {
			power.revert();
			nextWorld();
			continue_pressed = true;
			return;
		}
		else if (!levelComplete && !continue_pressed && debugmode && FlxG.keys.R) {
			power.revert();
			restartState();
			return;
		}

		//Do the snow walking FX
		if (Registry.player.isTouching(FlxObject.FLOOR) && Registry.player.velocity.x != 0) {
			if (!crunching) {
				Registry.snowfx.play();
				crunching = true;
			}
			snowtimer -= FlxG.elapsed;
			if (snowtimer < 0) {
				snowtimer += snowinterval;
				FlxParticle snowflake;
				FlxEmitter sp = (FlxEmitter) snowpuffs.members.get(lastused);
				
				sp.setYSpeed(-10, -40);
				sp.x = Registry.player.x + Registry.player.width / 2;
				sp.y = Registry.player.y - 1 + Registry.player.height;
				sp.start(true, 3.0f, 0.1f, 10);
				if (++lastused >= numpuffs)
					lastused = 0;
			}
		}
		else {
			crunching = false;
			Registry.snowfx.stop();
		}

		//If the player was airborne and is now touching the ground, SNOWPLOSION.
		if (wasairborne && Registry.player.isTouching(FlxObject.FLOOR)) {
			Registry.snow_fall.play(true);
			FlxParticle snowflake;
			FlxEmitter sp = (FlxEmitter) snowpuffs.members.get(lastused);
			
			sp.x = Registry.player.x + Registry.player.width / 2;
			sp.y = Registry.player.y - 1 + Registry.player.height;
			sp.setYSpeed(-50, -100);
			sp.start(true, 3.0f, 0.1f, 40);
			if (++lastused >= numpuffs)
				lastused = 0;
		}
		wasairborne = !Registry.player.isTouching(FlxObject.FLOOR);

		if (!playerDead && !levelComplete) {
			FlxG.collide(spikes, Registry.player, i_spikeCollision);
		}
		
		if (!levelComplete) {
			FlxG.collide(snowpuffs, map);
			FlxG.collide(exit, Registry.player, i_endLevel);
		}
	}

	public void restartState(){
		EndLevel.tearDown(this);
		SpikeCollision.tearDown(this);
		
		power.revert();
		//This was added in later. Initially I very lazily just recreated WorldState if the player had to/wanted to restart.
		//This is hugely wasteful, which is fine on Desktops and seriously not fine at all on Android. Fortunately, APP is very
		//simple, so we can just put the player back where she was and re-enable controls.
		Registry.player.x = initial_x;
		Registry.player.y = initial_y;
		Registry.player.velocity.x = 0;
		Registry.player.velocity.y = 0;
		//Stop flickering
		Registry.player.flicker(0);
		//Re-enable controls
		Registry.player.enable();
	}

	public void nextWorld(){
		//You can't spell hardcoding without 'rad coding!'
		world = (world + 1) % 4;
	}

	public int getWorld(){
		return world;
	}

	public int getLevel(){
		return level;
	}

	public void goToNextState(){
		if (continue_pressed)
			return;

		Registry.song.setVolume(Registry.musicVolume / 10.0f);
		Registry.jingle_short.stop();
		power.revert();

		Registry.writeLog();
		if (level == 9 || level == 10) {
			LevelSelectState next = new LevelSelectState(world);
			FlxG.fade(Asset.CHRISTMAS_EVE, 0.3f, new FlashTransition(next));
		}
		else if ((world == 9 && level == 2)) {
			FlxG.fade(Asset.CHRISTMAS_EVE, 0.3f, new FlashTransition(new MenuState()));
		}
		else {
			FlxG.fade(Asset.CHRISTMAS_EVE, 0.3f, new FlashTransition(new LoadingState(new WorldState(world, level + 1, Registry.powers[real_world]))));
		}
		continue_pressed = true;
	}

	/*
		I like how I have the worst spaghetti code in the world further up, but for some reason
		I decided it was really necessary to write a nice getter/setter-style method here.
	*/
	public void pause(){
		pauseMenu = true;
	}

	public void unpause(){
		pauseMenu = false;
	}

}
