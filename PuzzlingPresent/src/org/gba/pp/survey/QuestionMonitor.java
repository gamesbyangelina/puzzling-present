package org.gba.pp.survey;

import java.util.LinkedList;
import java.util.List;

import org.gba.pp.model.menu.ToggleButton;

/*
Convenience stuff. Takes a series of ToggleButtons and ensures that only one of them is 
activated at any time. Essentially turns them into a Radio Button-style UI thing.
*/
public class QuestionMonitor {
	
	List<ToggleButton> tb = new LinkedList<ToggleButton>();
	
	public QuestionMonitor(ToggleButton ... ts){
		for(ToggleButton t : ts)
			tb.add(t);
	}
	
	public int getResponse(){
		int response = -1;
		
		for(int i=0; i<tb.size(); i++){
			if(tb.get(i).toggled)
				response = i;
		}
		
		return response;
	}

}
