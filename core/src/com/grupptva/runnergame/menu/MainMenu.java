package com.grupptva.runnergame.menu;

/**
 * 
 * @author Luka
 * revised by Mattias
 */
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.ScenePlugin;

public class MainMenu implements ScenePlugin {

	//TODO: Turn the BitmapFonts and the Strings into arrays
	Map<String, String> buttonTitles;
	Map<String, BitmapFont> buttonBF;

	BitmapFont buttons;

	private String startGameString, highScoresString, quitGameString;

	MenuButton startGame, highScores, quitGame;

	MenuListener listener;
	
	Texture img;

	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();
	
	// private static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();

	public MainMenu(MenuListener listener, Integer screenWidth, Integer screenHeight) {
		buttons = new BitmapFont();
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		this.listener = listener;

		img = new Texture(Gdx.files.internal("bg.png"));
		buttonTitles = new HashMap();
		buttonTitles.put("startgame", "Start Game");

		startGameString = "Start Game";
		highScoresString = "Highscores";
		quitGameString = "Quit Game";

		startGame = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 + 40, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
		highScores = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 - 20, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
		quitGame = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 - 80, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
	}

	public void render(ShapeRenderer sr, MenuButton button) {
		if (button.collides(-80, 40, screenWidth, screenHeight)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				sr.setColor(button.pressed);
			}
		} else
			sr.setColor(button.notPressed);
		sr.rect(button.x, button.y, button.width, button.height);
	}

	public void renderBackground(Batch batch) {
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void renderButtons(ShapeRenderer sr) {
		render(sr, startGame);
		render(sr, highScores);
		render(sr, quitGame);
	}

	public void renderText(Batch batch) {
		buttons.draw(batch, startGameString, 284, 305);
		buttons.draw(batch, highScoresString, 284, 246);
		buttons.draw(batch, quitGameString, 284, 186);
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		batch.begin();
		renderBackground(batch);
		batch.end();
		sr.begin(ShapeType.Filled);
		renderButtons(sr);
		sr.end();
		batch.begin();
		renderText(batch);
		batch.end();
	}
	
	private void startGame()
	{
		listener.startGameEvent();
	}
	
	private void enterHighscores() 
	{
		listener.enterHighscores();
	}

	
	public void update() {
		// Button collision detection
		// TODO: Click detection on release rather than click & 
		//       change rect colour on click/add feedback
		if (startGame.collides(-80, 40, screenWidth, screenHeight)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Start game pressed!");
				startGame();
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}

		} else if (highScores.collides(-80, -20, screenWidth, screenHeight)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Highscores pressed!");
				enterHighscores();
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}

		} else if (quitGame.collides(-80, -80, screenWidth, screenHeight)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Quit game pressed!");
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
				Gdx.app.exit();
			}
		}
	}
}
