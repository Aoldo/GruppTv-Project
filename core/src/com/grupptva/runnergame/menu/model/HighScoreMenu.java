package com.grupptva.runnergame.menu.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.grupptva.runnergame.utils.Button;
import com.grupptva.runnergame.utils.HighScoresData;

/**
 * Responsibility: Represents the high score menu.
 *
 * Used by:
 * @see com.grupptva.runnergame.menu.controller.MenuController
 *
 * Uses:
 * @see Button
 * @see HighScoresData
 *
 *
 * @author Luka
 * Revised by Mattias
 *
 */
public class HighScoreMenu {

	public Button returnButton;

	Texture img;

	BitmapFont font;
	BitmapFont headerFont;
	BitmapFont highscoresFont;

	private String returnString;
	private String highScoresString;
	private String scores;

	GlyphLayout layout;

	Integer screenWidth = Gdx.graphics.getWidth();
	Integer screenHeight = Gdx.graphics.getHeight();

	float textWidth;

	HighScoresData hs;

	public HighScoreMenu(Integer screenWidth, Integer screenHeight) {

		hs = new HighScoresData();

		returnButton = new Button(-80, -200, 160, 40, new Color(0.15f, 0.3f, 0.5f, 1),
				new Color(0.3f, 0.6f, 1f, 1));

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		font = new BitmapFont();
		headerFont = new BitmapFont();
		highscoresFont = new BitmapFont();

		headerFont.getData().setScale(1.7f);

		returnString = "Return";
		highScoresString = "Highscores";
		scores = "";

		img = new Texture(Gdx.files.internal("core" + System.getProperty("file.separator") + "assets" + System.getProperty("file.separator") + "bg.png"));
		
		layout = new GlyphLayout(font, highScoresString);
		textWidth = layout.width;

	}

	public void render(SpriteBatch batch, ShapeRenderer sr, int mouseX, int mouseY) {
		batch.begin();
		renderBackground(batch);
		batch.end();
		sr.begin(ShapeType.Filled);
		renderButton(sr, returnButton, mouseX, mouseY);
		sr.end();
		batch.begin();
		renderText(batch);
		batch.end();
	}

	public void renderButton(ShapeRenderer sr, Button button, int mouseX,
			int mouseY) {
		if (button.collides(mouseX, mouseY, screenWidth / 2, screenHeight / 2)) {
			sr.setColor(button.pressed);
		} else
			sr.setColor(button.notPressed);
		sr.rect(button.x+screenWidth/2, button.y+screenHeight/2, button.width, button.height);
	}

	public void renderBackground(Batch batch) {
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void renderText(Batch batch) {
		font.draw(batch, returnString, 296, 66);
		headerFont.draw(batch, highScoresString, (screenWidth / 2 - textWidth), 395);

		for (int i = 0; i < (hs.getLength()); i++) {
			scores = String.format("%02d. %20s %5s", i + 1, hs.getHighScore(i),
					hs.getName(i));
			highscoresFont.draw(batch, scores, (screenWidth / 2 - textWidth),
					(330 - 20 * i));
		}
	}


}
