package com.grupptva.runnergame;

/**
 * 
 * @author Luka
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainMenu {

	//TODO: Turn the BitmapFonts and the Strings into arrays
	Map<String, String> buttonTitles;
	Map<String, BitmapFont> buttonBF;

	BitmapFont buttons;
	private String hookHeroString;

	// BitmapFont startGameBF;
	private String startGameString;

	// BitmapFont highScoresBF;
	private String highScoresString;

	// BitmapFont quitGameBF;
	private String quitGameString;

	MenuButton startGame;
	MenuButton highScores;
	MenuButton quitGame;

	Texture img;

	int screenWidth = Gdx.graphics.getWidth();
	int screenHeight = Gdx.graphics.getHeight();

	public int getScreenWidth() {
		return Gdx.graphics.getWidth();
	}

	public int getScreenHeight() {
		return Gdx.graphics.getHeight();
	}

	// private static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();

	public MainMenu() {
		buttons = new BitmapFont();

		img = new Texture(Gdx.files.internal("mainmenubg2.png"));

		buttonTitles = new HashMap();
		buttonTitles.put("startgame", "Start Game");

		//hookHeroString = "Hook Hero";
		//hookHeroBF = new BitmapFont();

		startGameString = "Start Game";
		// startGameBF = new BitmapFont();

		highScoresString = "Highscores";
		// highScoresBF = new BitmapFont();

		quitGameString = "Quit Game";
		// quitGameBF = new BitmapFont();

		startGame = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 + 40, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
		highScores = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 - 20, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
		quitGame = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 - 80, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
	}

	public void render(ShapeRenderer sr, MenuButton button) {
		if (button.collides(-80, 40, this)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				sr.setColor(button.pressed);
			}
		} else
			sr.setColor(button.notPressed);
		sr.rect(button.x, button.y, button.width, button.height);
	}

	public void renderBackground(ShapeRenderer sr, Batch batch) {
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void renderButtons(ShapeRenderer sr, Batch batch) {
		render(sr, startGame);
		render(sr, highScores);
		render(sr, quitGame);
	}

	public void renderText(ShapeRenderer sr, Batch batch) {
		buttons.draw(batch, startGameString, 284, 305);
		buttons.draw(batch, highScoresString, 284, 246);
		buttons.draw(batch, quitGameString, 284, 186);
	}

	public void update() {
		// Button collision detection
		// TODO: Click detection on release rather than click & 
		//       change rect colour on click/add feedback
		if (startGame.collides(-80, 40, this)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Start game pressed!");
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}

		} else if (highScores.collides(-80, -20, this)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Highscores pressed!");
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}

		} else if (quitGame.collides(-80, -80, this)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Quit game pressed!");
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
				Gdx.app.exit();
			}
		}
	}
}
