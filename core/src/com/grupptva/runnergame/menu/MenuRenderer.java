package com.grupptva.runnergame.menu;

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

public class MenuRenderer {

	public void render(Batch batch, ShapeRenderer sr, MainMenu model, int mouseX,
			int mouseY) {
		batch.begin();
		renderBackground(batch, model);
		batch.end();
		sr.begin(ShapeType.Filled);
		renderButton(sr, model.startGame, model, mouseX, mouseY);
		renderButton(sr, model.highScores, model, mouseX, mouseY);
		renderButton(sr, model.quitGame, model, mouseX, mouseY);
		sr.end();
		batch.begin();
		renderText(batch, model);
		batch.end();

	}

	private void renderButton(ShapeRenderer sr, MenuButton button, MainMenu model,
			int mouseX, int mouseY) {
		int wHalf = model.screenWidth / 2;
		int hHalf = model.screenHeight / 2;
		if (button.collides(mouseX, mouseY, wHalf, hHalf)) {
				sr.setColor(button.pressed);
		} else
			sr.setColor(button.notPressed);
		sr.rect(button.x + wHalf, button.y + hHalf, button.width, button.height);
	}

	private void renderBackground(Batch batch, MainMenu model) {
		batch.draw(model.img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void renderText(Batch batch, MainMenu model) {
		model.buttons.draw(batch, model.startGameString, 284, 305);
		model.buttons.draw(batch, model.highScoresString, 284, 246);
		model.buttons.draw(batch, model.quitGameString, 284, 186);
	}

}
