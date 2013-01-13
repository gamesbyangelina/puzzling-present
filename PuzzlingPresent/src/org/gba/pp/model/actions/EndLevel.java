package org.gba.pp.model.actions;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxText;
import org.flixel.event.AFlxCollision;
import org.gba.pp.Registry;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;
import org.gba.pp.juice.TweenArrive;
import org.gba.pp.model.menu.BackButton;
import org.gba.pp.model.menu.SkipButton;
import org.gba.pp.model.menu.ToggleButton;
import org.gba.pp.state.LevelSelectState;
import org.gba.pp.state.MenuState;
import org.gba.pp.state.WorldState;
import org.gba.pp.survey.QuestionMonitor;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

//Callback for collision with the game exit
public class EndLevel implements AFlxCollision{
	
	private WorldState parentState;
	private float _inputCooldown;
	
	private final String FIRST_Q = StringBank.eb_question1;
	private final String SECOND_Q = StringBank.eb_question2;
	
	private int q_spacing = 25; float label_scale = 3.5f;
	private int first_q_height = FlxG.height/4;
	private int second_q_height = first_q_height + (q_spacing*3);
	private int option_left_border = 40;
	private int option_spacing = (FlxG.width - 80)/3;
	
	private SkipButton skip;
	private static FlxGroup ne;
	
	/*
	 * To anyone using the open source code: I apologise. This class got a lot of clobbering close to release and is not nice.
	 */
	
	public EndLevel(WorldState ws){
		parentState = ws;
		if(ne != null)
			ne.destroy();
		ne = null;
	}
	
	@Override
	public void callback(FlxObject exit, FlxObject player){
		
		Registry.snowfx.stop();
		if(Registry.musicVolume > 0)
			Registry.song.setVolume(0.1f);
		Registry.jingle_short.play(Registry.musicVolume < 0.5f ? (Registry.musicVolume/10.0f) : 0.5f);
		
		FlxSprite backing = new FlxSprite(0, 0);
		backing.makeGraphic(FlxG.width, FlxG.height, Asset.TRANS_CE);
		parentState.add(backing);
		
		ne = new FlxGroup();
		
		if(parentState.getLevel() == 10 || parentState.getWorld() == 9){ //Tutorial levels, special case
			//Display level complete text/buttons/etc.
			FlxText overText = new FlxText(0, -50, FlxG.width, StringBank.eb_levelcomplete);
			overText.setFormat(overText.getFont(), 24, Asset.RED_LITE, "center");
			ne.add(overText);
			parentState.tweens.add(new TweenArrive(overText, FlxG.height/2-20, 0.2f));
			
			BackButton backToMenu; String buttonText;
			if(parentState.getWorld() == 9 && parentState.getLevel() < 2){
				backToMenu = new BackButton(new WorldState(parentState.getWorld(), parentState.getLevel()+1, Registry.powers[parentState.getWorld()]), FlxG.width/2 - 100, FlxG.height - 50, 200, 40, Asset.CHRISTMAS_EVE);
				if(Gdx.app.getType() == ApplicationType.Desktop){
					buttonText = StringBank.eb_nexttext_desktop;
				}
				else{ //Android
					buttonText = StringBank.eb_nexttext;
				}
			}
			else{
				if(parentState.getWorld() == 9)
					backToMenu = new BackButton(new MenuState(), FlxG.width/2 - 100, FlxG.height - 50, 200, 40, Asset.CHRISTMAS_EVE);
				else
					backToMenu = new BackButton(new LevelSelectState(parentState.getWorld()), FlxG.width/2 - 100, FlxG.height - 50, 200, 40, Asset.CHRISTMAS_EVE);
				if(Gdx.app.getType() == ApplicationType.Desktop){
					buttonText = StringBank.eb_backtext_desktop;
				}
				else{ //Android
					buttonText = StringBank.eb_backtext;
				}
			}
			backToMenu.loadGraphic(Asset.nextbtn);
			ne.add(backToMenu);
			parentState.buttons.add(backToMenu);
			if(Gdx.app.getType() == ApplicationType.Desktop){
				ne.add(backToMenu.genText(buttonText, Asset.CREAM));
			}
			else{ //Android
				ne.add(backToMenu.genText(buttonText, Asset.CREAM));
			}
			
			
		}
		else{
			//Set level as complete if not
			FileHandle file = Gdx.files.local("world"+parentState.getWorld()+"data.txt");
			String data = file.readString();
			String[] leveldatas = data.split(";");
			
			if(leveldatas[parentState.getLevel()].equalsIgnoreCase("0")){
			
				//Update the level data file
				leveldatas[parentState.getLevel()] = "1";
				data = "";
				for(String s : leveldatas)
					data += s+";";
				file.writeString(data, false);
				//Check to see if this triggered a world unlock
				if(countComplete(leveldatas) == Registry.UNLOCK_LIMIT && parentState.getWorld() < 2){
					FlxText worldUnlockText = new FlxText(0, 5, FlxG.width, StringBank.eb_unlockWorld);
					worldUnlockText.setFormat(worldUnlockText.getFont(), 16, Asset.CREAM, "center");
					ne.add(worldUnlockText);
//					parentState.tweens.add(new TweenArrive(worldUnlockText, 5, 0.2f));
					
					FileHandle nextWorldFile = Gdx.files.local("world"+(parentState.getWorld()+1)+"data.txt");
					String nextWorldData = nextWorldFile.readString();
					if(nextWorldData.startsWith("-1")){
						nextWorldFile.writeString("0;0;0;0;0;0;0;0;0;0;", false);
					}
				}
				//Check to see if this triggered a world completion
				if(countComplete(leveldatas) == Registry.LEVELS_PER_WORLD){
					FlxText worldCompleteText = new FlxText(0, 5, FlxG.width, StringBank.eb_worldComplete);
					worldCompleteText.setFormat(worldCompleteText.getFont(), 16, Asset.CREAM, "center");
					ne.add(worldCompleteText);
//					parentState.tweens.add(new TweenArrive(worldCompleteText, 5, 0.2f));
				}
			}
		
			//Display level complete text/buttons/etc.
			FlxText overText = new FlxText(0, -50, FlxG.width, StringBank.eb_levelcomplete);
			overText.setFormat(overText.getFont(), 24, Asset.CREAM, "center");
			ne.add(overText);
			
			if(!Registry.SCIENCE_MODE){
				parentState.tweens.add(new TweenArrive(overText, FlxG.height/2-20, 0.2f));
			}
			else{
				parentState.tweens.add(new TweenArrive(overText, 30, 0.2f));
				//Display survey questions
				FlxText firstQ = new FlxText(0, first_q_height, FlxG.width, FIRST_Q);
				firstQ.setFormat(firstQ.getFont(), 16, Asset.CREAM, "center");
				ne.add(firstQ);
//				parentState.tweens.add(new TweenArrive(firstQ, first_q_height, 0.2f));
				
				int xoff = option_left_border; int width = 20;
				ToggleButton option1 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
				ToggleButton option2 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
				ToggleButton option3 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
				ToggleButton option4 = new ToggleButton(xoff - width/2, first_q_height+q_spacing, width, width); xoff += option_spacing;
				ne.add(option1.genSubText(StringBank.eb_q1response1, Asset.CREAM, 8, label_scale));
				ne.add(option2.genSubText(StringBank.eb_q1response2, Asset.CREAM, 8, label_scale));
				ne.add(option3.genSubText(StringBank.eb_q1response3, Asset.CREAM, 8, label_scale));
				ne.add(option4.genSubText(StringBank.eb_q1response4, Asset.CREAM, 8, label_scale));
				option1.addLinkedButtons(option2, option3, option4); parentState.add(option1); parentState.buttons.add(option1);
				option2.addLinkedButtons(option1, option3, option4); parentState.add(option2); parentState.buttons.add(option2);
				option3.addLinkedButtons(option2, option1, option4); parentState.add(option3); parentState.buttons.add(option3);
				option4.addLinkedButtons(option2, option3, option1); parentState.add(option4); parentState.buttons.add(option4);
				parentState.q1response = new QuestionMonitor(option1, option2, option3, option4);
				
				FlxText secondQ = new FlxText(0, second_q_height, FlxG.width, SECOND_Q);
				secondQ.setFormat(secondQ.getFont(), 16, Asset.CREAM, "center");
				ne.add(secondQ);
//				parentState.tweens.add(new TweenArrive(secondQ, second_q_height, 0.2f));
				
				xoff = option_left_border;
				option1 = new ToggleButton(xoff - width/2, second_q_height+q_spacing, width, width); xoff += option_spacing;
				option2 = new ToggleButton(xoff - width/2, second_q_height+q_spacing, width, width); xoff += option_spacing;
				option3 = new ToggleButton(xoff - width/2, second_q_height+q_spacing, width, width); xoff += option_spacing;
				option4 = new ToggleButton(xoff - width/2, second_q_height+q_spacing, width, width); xoff += option_spacing;
				ne.add(option1.genSubText(StringBank.eb_q2response1, Asset.CREAM, 8, label_scale));
				ne.add(option2.genSubText(StringBank.eb_q2response2, Asset.CREAM, 8, label_scale));
				ne.add(option3.genSubText(StringBank.eb_q2response3, Asset.CREAM, 8, label_scale));
				ne.add(option4.genSubText(StringBank.eb_q2response4, Asset.CREAM, 8, label_scale));
				option1.addLinkedButtons(option2, option3, option4); parentState.add(option1); parentState.buttons.add(option1);
				option2.addLinkedButtons(option1, option3, option4); parentState.add(option2); parentState.buttons.add(option2);
				option3.addLinkedButtons(option2, option1, option4); parentState.add(option3); parentState.buttons.add(option3);
				option4.addLinkedButtons(option2, option3, option1); parentState.add(option4); parentState.buttons.add(option4);
				parentState.q2response = new QuestionMonitor(option1, option2, option3, option4);
			}
			skip = new SkipButton(parentState, FlxG.width/2 - 100, FlxG.height-30, 200, 25, Asset.RED_DARK);
			skip.loadGraphic(Asset.nextbtn);
			ne.add(skip);
			if(Gdx.app.getType() == ApplicationType.Desktop){
				ne.add(skip.genText(StringBank.eb_nexttext_desktop, Asset.CREAM));
			}
			else{ //Android
				ne.add(skip.genText(StringBank.eb_nexttext, Asset.CREAM));
			}
			parentState.buttons.add(skip);
			
		}
		
		//Remove the tablet controls
		if(Gdx.app.getType() == ApplicationType.Android)
			parentState._pad.exists = false;
		parentState.exit.allowCollisions = FlxObject.NONE;
		
		parentState.levelComplete = true;
		parentState.add(ne);
		
	}
	
	public static void tearDown(WorldState parentState){
		if(ne != null){
			parentState.remove(ne);
			for(FlxBasic fo : ne.members){
				fo.destroy();
			}
			ne.destroy();
			ne = null;
		}
		parentState.exit.allowCollisions = FlxObject.ANY;
		parentState.levelComplete = false;
	}

	private int countComplete(String[] data){
		int count = 0;
		for(String s : data){
			if(s.equalsIgnoreCase("1"))
				count++;
		}
		return count;
	}
}
