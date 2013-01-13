package org.gba.pp.state;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.gba.pp.data.Asset;
import org.gba.pp.model.Player;
import org.gba.pp.model.menu.BackButton;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * State that contains a short 'About This App' section
 * detailing Mike, ANGELINA, Imperial College, and the research.
 * Left and right buttons, slideshow-style.
 * Back button goes to LevelSelectState
 */
public class AboutState extends FlxState {
	
	int page = 0;
	static final int pagelim = 3;
	
	public static String SCREEN_ONE;
	public static String SCREEN_TWO;
	public static String SCREEN_THREE;
	public static String SCREEN_FOUR;
	public static String[] SCREENS;
	
	public static final String PAGEONE = "A Puzzling Present is a game made by computer scientists from Imperial College in London. We're building a computer program called ANGELINA that can design videogames all on its own.";
	public static final String PAGETWO = "Recently we've begun experimenting with getting ANGELINA to design levels and powerups on its own. In A Puzzling Present, all of Santa's special powers, and all of the levels you play, were designed by a computer - not a human!";
	public static final String PAGETHREE = "Getting computers to do clever things like designing games requires what's known as Artificial Intelligence, or AI for short.";
	public static final String PAGEFOUR = "We work in a special area of AI called Computational Creativity. We aren't just interested in computers being clever - we want them to be creative, to think of new ideas and help humans be more creative too!";
	public static final String PAGEFIVE = "ANGELINA has made lots of other games, from arcade games to platformers about news stories, and each project is different - but so far, ANGELINA has always needed a bit of help from a human.";
	public static final String PAGESIX = "In A Puzzling Present, ANGELINA is helped out by Harriet Jones, who designed the title and logo art, and Kevin Macleod. Michael Cook - ANGELINA's designer - drew the in-game art and wrote the code.";
	public static final String PAGESEVEN = "A Puzzling Present was possible thanks to 'I'm A Scientist, Get Me Out Of Here', a project that connects UK schools with scientists and lets students take part in live chats and ask questions.";
	public static final String PAGEEIGHT = "We hope you enjoy playing with A Puzzling Present!";
	
	public static final String[] pages_upper = new String[]{PAGEONE, PAGETHREE, PAGEFIVE, PAGESEVEN};
	public static final String[] pages_lower = new String[]{PAGETWO, PAGEFOUR, PAGESIX, PAGEEIGHT};
	
	FlxText title;
	FlxText upperText;
	FlxSprite screen;
	FlxText lowerText;
	FlxText pageNumbers;
	private BackButton back;
	private FlxSprite leftArrow;
	private FlxSprite rightArrow;
	
	@Override
	public void create(){
		title = new FlxText(0, 10, FlxG.width);
		title.setFormat(Asset.font, 24, Asset.RED_DARK, "center");
		title.setText("About This Game");
		add(title);
		
		upperText = new FlxText(10, 50, FlxG.width-20);
		upperText.setFormat(upperText.getFont(), 8, Asset.CREAM, "left");
		upperText.setText(pages_upper[page]);
		add(upperText);
		
		SCREEN_ONE = ("about-pack:mainscreen"); 
		SCREEN_TWO = ("about-pack:firstscreen");
		SCREEN_THREE = ("about-pack:gameshots");
		SCREEN_FOUR = ("about-pack:imascientist");
		SCREENS = new String[]{SCREEN_ONE, SCREEN_TWO, SCREEN_THREE, SCREEN_FOUR};
		
		screen = new FlxSprite(10, upperText.y + upperText.height + 5, SCREEN_ONE);
		screen.x = (FlxG.width-screen.width)/2; 
		add(screen);
		
		lowerText = new FlxText(10, screen.y + screen.height + 5, FlxG.width-20);
		lowerText.setFormat(lowerText.getFont(), 8, Asset.CREAM, "left");
		lowerText.setText(pages_lower[page]);
		add(lowerText);
		
		pageNumbers = new FlxText(FlxG.width- 65, FlxG.height - 15, 60);
		pageNumbers.setFormat(lowerText.getFont(), 8, Asset.CREAM, "right");
		pageNumbers.setText((page+1)+"/"+(pagelim+1));
		add(pageNumbers);
		
		back = new BackButton(new MenuState(), 0, FlxG.height-16);
		add(back);
		
		leftArrow = new FlxSprite(1, FlxG.height/2 - 15);
		leftArrow.loadGraphic("about-pack:leftarrow");
		add(leftArrow);
		leftArrow.visible = false;
		rightArrow = new FlxSprite(FlxG.width - 16, FlxG.height/2 - 15);
		rightArrow.loadGraphic("about-pack:rightarrow");
		add(rightArrow);
	}
	
	@Override
	public void update(){
		if(FlxG.mouse.justPressed()){
			back.checkClick(FlxG.mouse.screenX, FlxG.mouse.screenY, this);
			
			if(FlxG.mouse.screenY > FlxG.height/4 && FlxG.mouse.screenY < 3*FlxG.height/4){
				if(FlxG.mouse.screenX > 2*FlxG.width/3){
					page++;
					if(page > pagelim)
						page = pagelim;
					
					if(page == pagelim)
						rightArrow.visible = false;
					else
						rightArrow.visible = true;
					
					if(page == 0)
						leftArrow.visible = false;
					else
						leftArrow.visible = true;
					
				}
				else if(FlxG.mouse.screenX < FlxG.width/3){
					if((--page) < 0) page = 0;
					
					if(page == pagelim)
						rightArrow.visible = false;
					else
						rightArrow.visible = true;
					
					if(page == 0)
						leftArrow.visible = false;
					else
						leftArrow.visible = true;
				}
				
				upperText.visible = false;
				upperText = new FlxText(10, 50, FlxG.width-20);
				upperText.setFormat(upperText.getFont(), 8, Asset.CREAM, "left");
				upperText.setText(pages_upper[page]);
				add(upperText);
				
				screen.visible = false;
				screen = new FlxSprite(10, upperText.y + upperText.height + 5, SCREENS[page]);
				screen.x = (FlxG.width-screen.width)/2; 
				add(screen);
				
				lowerText.visible = false;
				lowerText = new FlxText(10, screen.y + screen.height + 5, FlxG.width-20);
				lowerText.setFormat(lowerText.getFont(), 8, Asset.CREAM, "left");
				lowerText.setText(pages_lower[page]);
				add(lowerText);
				
				pageNumbers.setText((page+1)+"/"+(pagelim+1));
			}
		}
		
		if(	(Gdx.app.getType() == ApplicationType.Desktop && FlxG.keys.ESCAPE) ||
			(Gdx.app.getType() == ApplicationType.Android && Gdx.input.isKeyPressed(Keys.BACK))){
			FlxG.switchState(new MenuState());
		}
		
	}
}
