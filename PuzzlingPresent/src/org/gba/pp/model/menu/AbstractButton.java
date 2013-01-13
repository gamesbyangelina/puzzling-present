package org.gba.pp.model.menu;

import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.data.Asset;

//Generic button class.
//Tried to add in some stuff that was convenient while developing, like generating label text.
public abstract class AbstractButton extends FlxSprite{

	protected int bg_width;
	protected int bg_height;
	protected int bg_color;
	protected int bg_clicked_color;
	protected FlxText attachedText;
	
	protected AbstractButton(int x, int y){
		super(x, y);
	}
	
	public abstract void click(FlxState s);
	
	public void checkClick(float x, float y, FlxState s){
		if(x > this.x && x < this.x + this.width
				&& y > this.y && y < this.y + this.height){
			this.click(s);
		}
	}
	
	//GEnerates text and positions it over the label. Returns the text so the caller can add it to the appropriate state.
	public FlxText genText(String string, int Color){
		FlxText ft = new FlxText(this.x-1, this.y, (int) width, string);
		ft.setFormat(Asset.font, 8, Color, "center");
		ft.setText(string);
		float txt_height = ft.height;
		int offset = (int) (this.height - txt_height)/2;
		ft.y += offset;
		this.attachedText = ft;
		return ft;
	}
	
	//Generates text and positions it under the label. Returns the text so the caller can add it to the appropriate state.
	public FlxText genSubText(String string, int color, int size, float scale){
		
		int newx = (int)((x + width/2) - (width*scale/2));
		
		FlxText ft = new FlxText(newx, this.y, (int) (width * scale), string);
		ft.setFormat(ft.getFont(), size, color, "center");
		ft.setText(string);
		int offset = (int) height;
		ft.y += offset;
		this.attachedText = ft;
		return ft;
	}
	
}
