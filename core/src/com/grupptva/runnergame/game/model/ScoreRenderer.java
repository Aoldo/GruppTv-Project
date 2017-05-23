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

public class ScoreRenderer {

	String score;
	
	BitmapFont bf;
	
	MenuButton scoreBox;
	
	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();
			
	public ScoreRenderer() {
		
		score = "Score: " + GameLogic.getCurrentScore();// + getScore();
		
		bf = new BitmapFont();
		
		scoreBox = new MenuButton(screenWidth / 2 + 185, screenHeight / 2 + 175, 100, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.15f, 0.3f, 0.5f, 1));
		
	}
	
	public void render(SpriteBatch batch, ShapeRenderer sr) {
		renderScoreBox(sr);
		sr.end();
		///////////////////
		batch.begin();
		renderScoreString(batch);
		batch.end();
	}

	private void renderScoreString(SpriteBatch batch) {
		bf.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		bf.draw(batch, score, 520, 440);
	}

	private void renderScoreBox(ShapeRenderer sr) {
		render(sr, scoreBox);
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
