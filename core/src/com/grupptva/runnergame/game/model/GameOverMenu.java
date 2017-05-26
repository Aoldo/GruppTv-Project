package com.grupptva.runnergame.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.menu.MenuButton;

/**
 * 
 * @author Luka
 * Revised by Mattias
 *
 */
public class GameOverMenu {

	String mainMenuString, playAgainString, scoreString;

	BitmapFont buttons;

	public MenuButton mainMenu;

	public MenuButton playAgain;

	MenuButton background;

	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();

	public Integer score;

	public GameOverMenu(Integer score) {
		mainMenuString = "Main Menu";
		playAgainString = "Play Again";
		scoreString = "Your Final Score: ";
		this.score = score;
		buttons = new BitmapFont();

		playAgain = new MenuButton(-120, -80, 100, 40, new Color(0.10f, 0.2f, 0.4f, 1),
				new Color(0.15f, 0.3f, 0.5f, 1));

		mainMenu = new MenuButton(20, -80, 100, 40, new Color(0.10f, 0.2f, 0.4f, 1),
				new Color(0.15f, 0.3f, 0.5f, 1));

		background = new MenuButton(-160, -120, 320, 240, new Color(0.1f, 0.1f, 1f, 0.2f),
				new Color(0.1f, 0.1f, 1f, 0.2f));
	}

	public void render(SpriteBatch batch, ShapeRenderer sr, int mouseX, int mouseY) {
		//render.renderButtons(sr);
		sr.begin(ShapeType.Filled);
		renderButtons(sr, mouseX, mouseY);
		sr.end();
		batch.begin();
		//render.renderText(batch);
		renderText(batch);
		batch.end();
	}

	private void renderText(SpriteBatch batch) {
		buttons.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		buttons.draw(batch, mainMenuString, 353, 187);
		buttons.draw(batch, playAgainString, 216, 187);
		buttons.draw(batch, scoreString + score, 255, 274);
	}

	private void renderButtons(ShapeRenderer sr, int mouseX, int mouseY) {
		render(sr, background, mouseX, mouseY);
		render(sr, mainMenu, mouseX, mouseY);
		render(sr, playAgain, mouseX, mouseY);
	}

	public void render(ShapeRenderer sr, MenuButton button, int mouseX, int mouseY) {
		int wHalf = screenWidth / 2;
		int hHalf = screenHeight / 2;
		if (button.collides(mouseX, mouseY, wHalf, hHalf)) {
				sr.setColor(button.pressed);
		} else
			sr.setColor(button.notPressed);
		sr.rect(button.x + wHalf, button.y + hHalf, button.width, button.height);
	}
}