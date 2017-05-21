package com.grupptva.runnergame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameOverMenu {

	String saveScoreString;
	
	BitmapFont buttons;
	
	MenuButton saveScore;
	
	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();
	
	public GameOverMenu(MenuListener listener, Integer screenWidth, Integer screenHeight) {
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		saveScoreString = "Save Score";
		buttons = new BitmapFont();
		
		MenuButton startGame = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 + 40, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
		
	}
	
}
