package com.grupptva.runnergame.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.grupptva.runnergame.utils.Button;

/**
 * 
 * @author Luka
 * Revised by Mattias
 *
 */
public class GameOverMenu {

	public String mainMenuString, playAgainString, scoreString;

	public BitmapFont buttons;

	public Button mainMenu;

	public Button playAgain;

	public Button background;

	public Integer screenWidth = Gdx.graphics.getWidth();
	public Integer screenHeight = Gdx.graphics.getHeight();

	public Integer score;

	public GameOverMenu(Integer score) {
		mainMenuString = "Main Menu";
		playAgainString = "Play Again";
		scoreString = "Your Final Score: ";
		this.score = score;
		buttons = new BitmapFont();

		playAgain = new Button(-120, -80, 100, 40, new Color(0.10f, 0.2f, 0.4f, 1),
				new Color(0.15f, 0.3f, 0.5f, 1));

		mainMenu = new Button(20, -80, 100, 40, new Color(0.10f, 0.2f, 0.4f, 1),
				new Color(0.15f, 0.3f, 0.5f, 1));

		background = new Button(-160, -120, 320, 240, new Color(0.1f, 0.1f, 1f, 0.2f),
				new Color(0.1f, 0.1f, 1f, 0.2f));
	}
}