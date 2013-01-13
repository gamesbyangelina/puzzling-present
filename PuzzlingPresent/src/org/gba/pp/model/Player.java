package org.gba.pp.model;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxGamePad;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.event.AFlxButton;
import org.gba.pp.Registry;
import org.gba.pp.data.Asset;
import org.gba.pp.model.powers.Power;
import org.gba.pp.state.WorldState;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

public class Player extends FlxSprite {
	
	public AFlxButton specialPressed;
	public AFlxButton specialReleased;
	public AFlxButton jumpPressed;
	public AFlxButton jumpReleased;
	public AFlxButton leftPressed;
	public AFlxButton leftReleased;
	public AFlxButton rightPressed;
	public AFlxButton rightReleased;

	public static float VELOCITY = 100;
	public static float S_JUMP_VELOCITY = 200;
	public float JUMP_VELOCITY = 200;
	public static float DEFAULT_GRAVITY = 500;
	
	private Power power;
	private boolean blockSpecial = false;
	private boolean attractMode;
	private float tickToNextAction;
	private float lastActionTick;
	private boolean leftLast;

	private FlxGamePad pad;
	private boolean blockjump = false;
	private int xStatus;
	private boolean specialUsed;
	private boolean a_rightPressed;
	private boolean a_leftPressed;
	private boolean a_jumpPressed;
	private boolean enabled = true;
	
	public void setPad(FlxGamePad p){
		this.pad = p;
	}

	public Player(int x, int y, Power p){
		super(x, y);
		loadGraphic(Asset.player_strip, true, true, 16, 16);

		addAnimation(IDLE, new int[] { 0 }, 1, true);
		addAnimation(WALK, new int[] { 1, 2, 3, 4 }, 8, true);
		addAnimation("jump", new int[] { 5 }, 1, true);
		addAnimation("tiebow", new int[] { 6, 7 }, 3, true);
		play(IDLE);

		this.power = p;
		acceleration.y = DEFAULT_GRAVITY;
		maxVelocity.x = 400;
		
		offset.x = 2;
		offset.y = 2;
		width = 12;
		height = 14;

	}

	@Override
	public void update(){
		if (!enabled || attractMode) {
			return;
		}

		/*
			Getting the controls right on Android was tricky. There were issues with buttons,
			fingers sliding off buttons while pressing, pressing two buttons quickly. There are 
			a lot of nasty flags in the code as a result, but it did solve those hurdles.
		*/
		if (Gdx.app.getType() == ApplicationType.Android) {
			if (!specialUsed && pad.buttonB.status == FlxButton.PRESSED ) {
				specialPressed();
				specialUsed = true;
			}
			else if ((pad.buttonB.status != FlxButton.PRESSED && specialUsed)) {
				specialReleased();
				if(Gdx.app.getType() == ApplicationType.Android){
					specialUsed = false;
				}
			}

			if (!a_jumpPressed && pad.buttonA.status == FlxButton.PRESSED) {
				jumpPressed();
				a_jumpPressed = true;
			}
			else if(a_jumpPressed){
				a_jumpPressed = false;
			}

			if (pad.buttonRight.status == FlxButton.PRESSED ) {
				rightPressed();
				if(!a_rightPressed){
					a_rightPressed = true;
				}
			}
			else if (pad.buttonLeft.status == FlxButton.PRESSED) {
				leftPressed();
				if(!a_leftPressed){
					a_leftPressed = true;
				}
			}
			else {
				nothingPressed();
			}
			
			if(a_rightPressed && pad.buttonRight.status != FlxButton.PRESSED){
				a_rightPressed = false;
			}
			if(a_leftPressed && pad.buttonLeft.status != FlxButton.PRESSED){
				a_leftPressed = false;
			}
		}
		else if (Gdx.app.getType() == ApplicationType.Desktop) {
			if (FlxG.keys.justPressed("X")) {
				specialPressed();
			}
			else if (FlxG.keys.justReleased("X")) {
				specialReleased();
			}

			if (FlxG.keys.SPACE) {
				jumpPressed();
			}

			if (FlxG.keys.RIGHT) {
				rightPressed();
			}
			else if (FlxG.keys.LEFT) {
				leftPressed();
			}
			else {
				nothingPressed();
			}
		}
		
		if(xStatus == 0)
			nothingPressed();
		else if(xStatus > 0)
			rightPressed();
		else if(xStatus < 0)
			leftPressed();

		if (velocity.x != 0) {
			play(WALK);
		}
		else {
			play(IDLE);
		}

	}

	private void rightPressed(){
		xStatus = 1;
		acceleration.x = 0;
		velocity.x = VELOCITY;
		setFacing(RIGHT);
	}

	private void leftPressed(){
		xStatus = -1;
		acceleration.x = 0;
		velocity.x = -VELOCITY;
		setFacing(LEFT);
	}

	private void nothingPressed(){
		xStatus = 0;
		velocity.x = 0;
	}

	private void jumpPressed(){
		if (isTouching(FlxObject.FLOOR)) {
			velocity.y = -JUMP_VELOCITY;
		}
	}

	private void jumpReleased(){
		blockjump = false;
	}

	private void specialPressed(){
		if(!WorldState.xPressed){
			WorldState.xPressed = true;
		}
		if (!blockSpecial) {
			power.doPower();
			blockSpecial = true;
		}
	}

	private void specialReleased(){
		blockSpecial = false;
	}

	public void attractMode(){
		attractMode = true;
	}

	public void disable(){
		enabled = false;
	}
	
	public void enable(){
		enabled = true;
	}

}
