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
	
	int screenWidth = Gdx.graphics.getWidth();
	int screenHeight = Gdx.graphics.getHeight();
	
	startGameString = "Start Game";
	startGameBF = new BitmapFont();
	
	MenuButton startGame;
	MenuButton highScores;
	MenuButton quitGame;
	
	Texture img;
	
	img = new Texture("mainmenubg.png");
	
	BitmapFont startGameBF;
	private String startGameString;

    private static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
	
	public MainMenu() {
		menuButtons.add(new MenuButton(0, 0, 0, 0));
		
		startGame = new MenuButton(screenWidth/2-80,screenHeight/2+40,160,40);
		highScores = new MenuButton(screenWidth/2-80,screenHeight/2-20,160,40);
		quitGame = new MenuButton(screenWidth/2-80,screenHeight/2-80,160,40);
	}
	
    public static void renderAllMenuButtons(ShapeRenderer sr){
    	menuButtons.forEach(item -> {
            item.render(sr);
        });
    }
    
	public void renderBackground (ShapeRenderer sr, Batch b) {
		
	}
	
	public void renderButtons (ShapeRenderer sr, Batch b) {
		
	}
	
	public void renderText (ShapeRenderer sr, Batch b) {
		
	}

}
