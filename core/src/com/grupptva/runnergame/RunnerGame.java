package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
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
		// Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 0.4f);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		int buttonHeight = mainMenu.getScreenHeight() - (mainMenu.getScreenHeight()/2+40);
		
		// Start button collision
		if (((Gdx.input.getX() < (mainMenu.getScreenWidth()/2-80) + mainMenu.startGame.getButtonWidth()) && !(Gdx.input.getX() < mainMenu.getScreenWidth()/2-80)) 
			&& ((Gdx.input.getY() < buttonHeight)) && !(Gdx.input.getX() < ???????) ){                  
			System.out.println("Active!");
		} else {
			System.out.println("Not Active!");
		}
		
		//Background
		batch.begin(); 
		mainMenu.renderBackground(sr, batch);
		batch.end();
		
		// %% TODO %% 
		// Change ShapeRenderer to SpriteBatch, 
		// add texture to SpriteBatch and make it clickable
		
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
