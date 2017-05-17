package com.grupptva.runnergame.menu;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainMenuRenderer {
	
	MainMenu mainMenu;
	
	public MainMenuRenderer(MainMenu m) {
		this.mainMenu = m;
	}

	public void render(ShapeRenderer sr, MenuButton button) {
		if (button.collides(-80, 40, mainMenu.screenWidth, mainMenu.screenHeight)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				sr.setColor(button.pressed);
			}
		} else
			sr.setColor(button.notPressed);
		sr.rect(button.x, button.y, button.width, button.height);
	}


	public void renderBackground(Batch batch) {
		batch.draw(mainMenu.img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void renderButtons(ShapeRenderer sr) {
		render(sr, mainMenu.startGame);
		render(sr, mainMenu.highScores);
		render(sr, mainMenu.quitGame);
	}

	public void renderText(Batch batch) {
		mainMenu.buttons.draw(batch, mainMenu.startGameString, 284, 305);
		mainMenu.buttons.draw(batch, mainMenu.highScoresString, 284, 246);
		mainMenu.buttons.draw(batch, mainMenu.quitGameString, 284, 186);
	}
	
}
