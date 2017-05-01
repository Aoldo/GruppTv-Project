package com.grupptva.runnergame;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainMenu {
	
	BitmapFont hookHeroBF;
	private String hookHeroString;
	
	BitmapFont startGameBF;
	private String startGameString;
	
	BitmapFont highScoresBF;
	private String highScoresString;
	
	BitmapFont quitGameBF;
	private String quitGameString;
	
	MenuButton startGame;
	MenuButton highScores;
	MenuButton quitGame;
	
	Texture img;
	
	int screenWidth = Gdx.graphics.getWidth();
	int screenHeight = Gdx.graphics.getHeight();
	
	public int getScreenWidth() {
		return Gdx.graphics.getWidth();
	}
	
	public int getScreenHeight() {
		return Gdx.graphics.getHeight();
	}

    // private static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
	
	public MainMenu() {
		img = new Texture("mainmenubg2.png");
		
		hookHeroString = "Hook Hero";
		hookHeroBF = new BitmapFont();
		
		startGameString = "Start Game";
		startGameBF = new BitmapFont();
		
		highScoresString = "Highscores";
		highScoresBF = new BitmapFont();
		
		quitGameString = "Quit Game";
		quitGameBF = new BitmapFont();
		
		startGame = new MenuButton(screenWidth/2-80,screenHeight/2+40,160,40);
		highScores = new MenuButton(screenWidth/2-80,screenHeight/2-20,160,40);
		quitGame = new MenuButton(screenWidth/2-80,screenHeight/2-80,160,40);
	}
    
	public void renderBackground (ShapeRenderer sr, Batch batch) {
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void renderButtons (ShapeRenderer sr, Batch batch) {
		startGame.render(sr);
		highScores.render(sr);
		quitGame.render(sr);
	}
	
	public void renderText (ShapeRenderer sr, Batch batch) {
		startGameBF.draw(batch, startGameString, 284, 305);
		highScoresBF.draw(batch, highScoresString, 284, 246);
		quitGameBF.draw(batch, quitGameString, 284, 186);
	}
	
	/*
	public void update () {
		
	}
	*/
}
