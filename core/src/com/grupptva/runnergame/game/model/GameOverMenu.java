package com.grupptva.runnergame.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.menu.MenuButton;
import com.grupptva.runnergame.menu.MenuListener;

public class GameOverMenu {

	String mainMenuString, playAgainString, scoreString;
	
	BitmapFont buttons;
	
	MenuButton mainMenu, playAgain, background;
	
	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();
	
	MenuListener listener;
	
	GameOverRenderer render;
	
	public GameOverMenu() {
		
		mainMenuString = "Main Menu";
		playAgainString = "Play Again";
		scoreString = "Your final score: " + 5;// + getScore();
		
		buttons = new BitmapFont();
		
		mainMenu = new MenuButton(screenWidth / 2 - 120, screenHeight / 2 - 80, 100, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.15f, 0.3f, 0.5f, 1));
		
		playAgain = new MenuButton(screenWidth / 2 + 20, screenHeight / 2 - 80, 100, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.15f, 0.3f, 0.5f, 1));
		
		background = new MenuButton(screenWidth / 2 - 160, screenHeight / 2 - 120, 320, 240,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.1f, 0.1f, 1f, 0.2f));
		
	}

	private void startGame() {
		listener.startGameEvent();
	}
	
	private void enterMainMenu() {
		listener.enterMainMenu();
	}
	
	public void update() {
		System.out.println("not active");
		if (mainMenu.collides2(-120, -80, 100 ,screenWidth, screenHeight)) {
			System.out.println("mainMenu active");
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Start game pressed!");
				enterMainMenu();
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}

		} else if (playAgain.collides2(20, -80, 100, screenWidth, screenHeight)) {
			System.out.println("Play Again active");
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Highscores pressed!");
				startGame();
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}
		}
	}
	
	public void render(SpriteBatch batch, ShapeRenderer sr) {
		//render.renderButtons(sr);
		renderButtons(sr);
		batch.begin();
		//render.renderText(batch);
		renderText(batch);
		batch.end();
	}

	private void renderText(SpriteBatch batch) {
		buttons.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		buttons.draw(batch, mainMenuString, 284, 305);
		buttons.draw(batch, playAgainString, 284, 246);
		buttons.draw(batch, scoreString, 300, 226);
	}

	private void renderButtons(ShapeRenderer sr) {
		render(sr, background);
		render(sr, mainMenu);
		render(sr, playAgain);
	}
	
	public void render(ShapeRenderer sr, MenuButton button) {
//		if (button.collides2(-120, -80, 100, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())) {
//			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
//				sr.setColor(button.pressed);
//			}
//		} else
//			sr.setColor(button.notPressed);
		sr.setColor(button.notPressed);
		sr.rect(button.x, button.y, button.width, button.height);
	}
	
}
