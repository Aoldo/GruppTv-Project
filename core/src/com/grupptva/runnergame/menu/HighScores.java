package com.grupptva.runnergame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.RunnerGame;
import com.grupptva.runnergame.ScenePlugin;

public class HighScores implements ScenePlugin  {
	
	//metod i konstruktorn
	//logik
	
	MenuListener listener;
	
	MenuButton returnButton;
	
	Texture img;
	
	BitmapFont buttons;
	private String returnButtonString;
	
	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();
	
	public HighScores(RunnerGame runnerGame, Integer screenWidth, Integer screenHeight) {
		//this.listener = listener;
		
		returnButton = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 - 200, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		buttons = new BitmapFont();
		returnButtonString = "Return";
		
		img = new Texture(Gdx.files.internal("mainmenubg2.png"));
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
		render(sr, returnButton);
	}
	
	public void renderText(Batch batch) {
		buttons.draw(batch, returnButtonString, 296, 66);
	}
	
	private void exitHighscores() {
		listener.exitHighscores();
	}

	@Override
	public void update() {
		if (returnButton.collides(-80, -200, screenWidth, screenHeight)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Return button pressed!");
				exitHighscores();
				//startGame();
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}
		}
	}

	@Override
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

}
