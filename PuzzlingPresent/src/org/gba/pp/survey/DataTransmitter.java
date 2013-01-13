package org.gba.pp.survey;

public abstract class DataTransmitter {

/*
	In GDX, the idea is to distribute platform-specific code in the respective android/desktop
	projects. Initially I split the data-sending code into different sections, but in the end
	abandoned it for a simpler PHP-submit approach. I'm leaving this file here because... yes.
*/

	public abstract void sendData(String data);
	
}
