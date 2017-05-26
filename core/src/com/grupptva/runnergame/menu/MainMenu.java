package com.grupptva.runnergame.menu;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * 
 * @author Luka revised by Mattias
 */
public class MainMenu {
	//TODO: Turn the BitmapFonts and the Strings into arrays
	Map<String, String> buttonTitles;
	Map<String, BitmapFont> buttonBF;

	BitmapFont buttons;

	boolean hasPressed = false;

	String startGameString, highScoresString, quitGameString;

	MenuButton startGame, highScores, quitGame;

	Texture img;

	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();

	// private static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();

	public MainMenu(Integer screenWidth, Integer screenHeight) {

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		buttons = new BitmapFont();

		img = new Texture(Gdx.files.internal("bg.png"));

		buttonTitles = new HashMap();
		buttonTitles.put("startgame", "Start Game");

		startGameString = "Start Game";
		highScoresString = "Highscores";
		quitGameString = "Quit Game";

		startGame = new MenuButton(-80, 40, 160, 40, new Color(0.15f, 0.3f, 0.5f, 1),
				new Color(0.3f, 0.6f, 1f, 1));
		highScores = new MenuButton(-80, -20, 160, 40, new Color(0.15f, 0.3f, 0.5f, 1),
				new Color(0.3f, 0.6f, 1f, 1));
		quitGame = new MenuButton(-80, -80, 160, 40, new Color(0.15f, 0.3f, 0.5f, 1),
				new Color(0.3f, 0.6f, 1f, 1));

	}
}
