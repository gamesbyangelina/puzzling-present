package org.gba.pp.state;

import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.Registry;
import org.gba.pp.data.Asset;
import org.gba.pp.model.menu.AbstractButton;

//I thought "SCIENCE MODE" would make it sound fun. 
//This button is used in the SettingsState
public class ScienceModeButton extends AbstractButton {

	public ScienceModeButton(int x, int y, int s_width, int s_height, int color_on, int color_off){
		super(x, y);
		this.bg_width = s_width;
		this.bg_height = s_height;
		this.bg_color = color_on;
		this.bg_clicked_color = color_off;
		if (Registry.SCIENCE_MODE)
			makeGraphic(s_width, s_height, color_on);
		else
			makeGraphic(s_width, s_height, color_off);
	}

	@Override
	public void click(FlxState s){
		// Change the files
		Registry.setScienceMode(!Registry.SCIENCE_MODE);
		this.makeGraphic(bg_width, bg_height, Registry.SCIENCE_MODE ? bg_color : bg_clicked_color);

		// Add some new text
		attachedText.visible = false;
		int oldcolor = attachedText.getColor();
		attachedText = new FlxText(this.x, this.y, bg_width, Registry.SCIENCE_MODE ? "Surveys: On" : "Surveys: Off");
		attachedText.setFormat(Asset.font, 8, oldcolor, "center");
		s.add(attachedText);
		float txt_height = attachedText.height;
		int offset = (int) (bg_height - txt_height) / 2;
		attachedText.y += offset;
	}

}
