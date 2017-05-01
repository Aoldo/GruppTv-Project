package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class RunnerGame extends ApplicationAdapter {
	ShapeRenderer sr;
	SpriteBatch batch;
	
	MainMenu mainMenu;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		mainMenu = new MainMenu();
	}

	@Override
	public void render () {
		
		// Button collision detection
		// TODO: Click detection on release rather than click & 
		//       change rect colour on click/add feedback
		if (mainMenu.startGame.collides(-80, 40, mainMenu)) {	
			if (Gdx.input.isButtonPressed(Buttons.LEFT)){
				System.out.println("Start game pressed!");
			}
			
		} else if (mainMenu.highScores.collides(-80, -20, mainMenu)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)){
				System.out.println("Highscores pressed!");
			}
			
		} else if (mainMenu.quitGame.collides(-80, -80, mainMenu)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)){
				System.out.println("Quit game pressed!");
				Gdx.app.exit();
			}
		}
		
		//Background
		batch.begin(); 
		mainMenu.renderBackground(sr, batch);
		batch.end();

		//Buttons 
		sr.begin(ShapeType.Filled); 
		mainMenu.renderButtons(sr, batch);
		sr.end();
		
		//Text
		batch.begin(); 
		mainMenu.renderText(sr, batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
