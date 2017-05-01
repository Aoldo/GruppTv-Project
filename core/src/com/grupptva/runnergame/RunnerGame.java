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
		int startGameButtonHeight = mainMenu.getScreenHeight() - (mainMenu.getScreenHeight()/2+40);
		int startGameButtonWidth =  mainMenu.getScreenWidth()/2-80 + mainMenu.startGame.getButtonWidth();
		
		int highScoreButtonHeight =  mainMenu.getScreenHeight() - (mainMenu.getScreenHeight()/2-20);
		int highScoreButtonWidth =  mainMenu.getScreenWidth()/2-80 + mainMenu.highScores.getButtonWidth();
		
		int quitGameButtonHeight =  mainMenu.getScreenHeight() - (mainMenu.getScreenHeight()/2-80);
		int quitGameButtonWidth =  mainMenu.getScreenWidth()/2-80 + mainMenu.quitGame.getButtonWidth();
		
		// Start button collision
		if (((Gdx.input.getX() < startGameButtonWidth) && !(Gdx.input.getX() < mainMenu.getScreenWidth()/2-80)) 
			&& (((Gdx.input.getY() < startGameButtonHeight)) && !(Gdx.input.getY() < (startGameButtonHeight-mainMenu.startGame.getButtonHeight())))){                  
		
			if (Gdx.input.isButtonPressed(Buttons.LEFT)){
				System.exit(0);
			}
		}
		
		// Highscores button collision
		if (((Gdx.input.getX() < highScoreButtonWidth) && !(Gdx.input.getX() < mainMenu.getScreenWidth()/2-80)) 
			&& (((Gdx.input.getY() < highScoreButtonHeight)) && !(Gdx.input.getY() < (highScoreButtonHeight-mainMenu.highScores.getButtonHeight())))){                  
			
			if (Gdx.input.isButtonPressed(Buttons.LEFT)){
				System.exit(0);
			}
		}
		
		// Quit game button collision
		if (((Gdx.input.getX() < quitGameButtonWidth) && !(Gdx.input.getX() < mainMenu.getScreenWidth()/2-80)) 
			&& (((Gdx.input.getY() < quitGameButtonHeight)) && !(Gdx.input.getY() < (quitGameButtonHeight-mainMenu.highScores.getButtonHeight())))){                  
			
			if (Gdx.input.isButtonPressed(Buttons.LEFT)){
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
