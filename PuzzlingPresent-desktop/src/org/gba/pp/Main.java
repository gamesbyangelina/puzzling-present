package org.gba.pp;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;

public class Main {
	public static void main(String[] args){
		Settings settings = new Settings();
		settings.padding = 2;
		settings.maxWidth = 512;
		settings.maxHeight = 512;
		settings.incremental = true;
		TexturePacker.process(settings, "../PuzzlingPresent-android/images", "../PuzzlingPresent-android/assets");
		TexturePacker.process(settings, "../PuzzlingPresent-android/about-images", "../PuzzlingPresent-android/assets", "about-pack");

		LwjglApplication app = new LwjglApplication(new org.gba.pp.PuzzlingPresentMain(), "A Puzzling Present!", 640, 480, false);
	}
}
