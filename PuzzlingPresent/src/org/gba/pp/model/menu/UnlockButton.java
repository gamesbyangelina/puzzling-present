package org.gba.pp.model.menu;

import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.StringBank;
import org.gba.pp.data.Asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

//Code for the 'unlock everything' logic.
public class UnlockButton extends AbstractButton {
	
	public UnlockButton(int x, int y, int s_width, int s_height, int Color, int ClickColor){
		super(x, y);
		this.bg_width = s_width;
		this.bg_height = s_height;
		this.bg_color = Color;
		this.bg_clicked_color = ClickColor;
		makeGraphic(s_width, s_height, Color);
	}

	public void click(FlxState s){
		//Change the files
		for(int world=0; world<3; world++){
			FileHandle file = Gdx.files.local("world"+world+"data.txt");
			file.writeString("1;1;1;1;1;1;1;1;1;1;", false);
		}
		this.makeGraphic(bg_width, bg_height, bg_clicked_color);
		
		//Add some new text
		attachedText.visible = false; int oldcolor = attachedText.getColor();
		attachedText = new FlxText(this.x, this.y, bg_width, StringBank.ub_unlocked);
		attachedText.setFormat(Asset.font, 8, oldcolor, "center");
		s.add(attachedText);
		float txt_height = attachedText.height;
		int offset = (int) (bg_height - txt_height)/2;
		attachedText.y += offset;
	}
}
