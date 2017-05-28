package com.grupptva.runnergame.game.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.game.model.GameOverMenu;
import com.grupptva.runnergame.utils.Button;

/**
 * Renders all the gameover menu compontents.
 * 
 * @author Luka Revised by Mattias
 * 
 */
public class GameOverRenderer {

	public static void render(SpriteBatch batch, ShapeRenderer sr, int mouseX, int mouseY,
			GameOverMenu model) {
		//render.renderButtons(sr);
		sr.begin(ShapeType.Filled);
		renderButtons(sr, mouseX, mouseY, model);
		sr.end();
		batch.begin();
		//render.renderText(batch);
		renderText(batch, model);
		batch.end();
	}

	private static void renderText(SpriteBatch batch, GameOverMenu model) {
		model.buttons.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		model.buttons.draw(batch, model.mainMenuString, 353, 187);
		model.buttons.draw(batch, model.playAgainString, 216, 187);
		model.buttons.draw(batch, model.scoreString + model.score, 255, 274);
	}

	private static void renderButtons(ShapeRenderer sr, int mouseX, int mouseY,
			GameOverMenu model) {
		render(sr, model.background, mouseX, mouseY, model);
		render(sr, model.mainMenu, mouseX, mouseY, model);
		render(sr, model.playAgain, mouseX, mouseY, model);
	}

	private static void render(ShapeRenderer sr, Button button, int mouseX, int mouseY,
			GameOverMenu model) {
		int wHalf = model.screenWidth / 2;
		int hHalf = model.screenHeight / 2;
		if (button.collides(mouseX, mouseY, wHalf, hHalf)) {
			sr.setColor(button.pressed);
		} else
			sr.setColor(button.notPressed);
		sr.rect(button.x + wHalf, button.y + hHalf, button.width, button.height);
	}
}