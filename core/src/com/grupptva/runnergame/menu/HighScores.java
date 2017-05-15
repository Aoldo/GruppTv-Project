package com.grupptva.runnergame.menu;

import java.lang.reflect.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.ScenePlugin;
import com.grupptva.runnergame.highscores.HighScoresData;

public class HighScores implements ScenePlugin  {
	
	//metod i konstruktorn
	//logik
	
	MenuListener listener;
	
	MenuButton returnButton;
	
	Texture img;
	
	//TODO: Move to HighScoresData
	//int highScores[] = new int[10];
	//String[] names = new String[10];
	
	BitmapFont font;
	BitmapFont headerFont;
	BitmapFont highscoresFont;
	
	int hFontWidth;
	
	final int maxScoreCount = 10;
	/////////////////////////////
	
	private String returnString;
	private String highScoresString;
	private String scores;
	
	GlyphLayout layout;
	
	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();
	
	float textWidth;
	
	HighScoresData hs;
		
	public HighScores(MenuListener listener, Integer screenWidth, Integer screenHeight) {
		
		// TODO: If highscore save file empty = initiate it with placeholders
//		if (true) {
//			HighScoresData.initHighScore();
//		}
		
		this.listener = listener;
		
		hs = new HighScoresData();
				
		returnButton = new MenuButton(screenWidth / 2 - 80, screenHeight / 2 - 200, 160, 40,
				new Color(0.15f, 0.3f, 0.5f, 1), new Color(0.3f, 0.6f, 1f, 1));
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		font = new BitmapFont();
		headerFont = new BitmapFont();
		highscoresFont = new BitmapFont();
		
		headerFont.getData().setScale(1.7f);
		
		returnString = "Return";
		highScoresString = "Highscores";
		scores = "";
		
		img = new Texture(Gdx.files.internal("bg.png"));
				
		layout = new GlyphLayout(font, highScoresString);
		textWidth = layout.width;
				
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
		font.draw(batch, returnString, 296, 66);
		headerFont.draw(batch, highScoresString, (screenWidth/2-textWidth), 395);
		
	
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
