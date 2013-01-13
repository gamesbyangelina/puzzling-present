package org.gba.pp.state;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.Registry;
import org.gba.pp.data.Asset;

//This state was abandoned - originally it would describe the power to you before you used it. 
//In the end I ditched this and used a separate tutorial level that people could choose to play.
//As it turned out, a lot of people didn't notice the tutorial level was there, so not a total success.
public class PowerInfoScreen extends FlxState {

	//Default visibility, okay. Presumably this was 3am code.
	FlxState next;
	String name;
	String text;
	int theme;
	
	int oldcolor;
	
	public PowerInfoScreen(FlxState next, String powerName, String effectText, int theme){
		this.next = next;
		this.name = powerName;
		this.text = effectText;
		this.theme = theme;
	}
	
	@Override
	public void create(){
		
		oldcolor = FlxG.getBgColor();
		FlxG.setBgColor((theme == 1 ? Asset.CHRISTMAS_EVE : Asset.CREAM));
		
		FlxText powerName = new FlxText(0, FlxG.height/3, FlxG.width);
		powerName.setFormat(powerName.getFont(), 16, (theme == 0 ? Asset.CHRISTMAS_EVE : Asset.CREAM), "center");
		powerName.setText(name);
		add(powerName);
		
		FlxText description = new FlxText(FlxG.width/8, FlxG.height/2, FlxG.width/2 + FlxG.width/4);
		description.setFormat(description.getFont(), 8, (theme == 0 ? Asset.CHRISTMAS_EVE : Asset.CREAM), "center");
		description.setText(text);
		add(description);
		
		FlxText next = new FlxText(0, FlxG.height-FlxG.height/4, FlxG.width);
		next.setFormat(next.getFont(), 8, Asset.RED_LITE, "center");
		next.setText("Press space to continue.");
		add(next);
		
	}
	
	@Override
	public void update(){
		if(FlxG.keys.SPACE || FlxG.mouse.justPressed()){
			FlxG.setBgColor(oldcolor);
			Registry.worlds++;
			FlxG.switchState(next);
			return;
		}
	}

}
