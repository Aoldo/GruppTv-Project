package com.grupptva.runnergame.menu.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.menu.model.HighScoreMenu;
import com.grupptva.runnergame.menu.model.MainMenu;
import com.grupptva.runnergame.menu.view.MenuRenderer;
/**
 * 
 * @author Luka
 * Revised by Mattias
 */
public class MenuController implements InputProcessor {
	MainMenu model;

	HighScoreMenu scoreModel;

	Integer width;
	Integer height;

	public MenuController() {
		Gdx.input.setInputProcessor(this);

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		model = new MainMenu(width, height);
		scoreModel = new HighScoreMenu(width, height);
	}

	public void update() {
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();

	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		if (!inHighScore)
			MenuRenderer.render(batch, sr, model, mouseX, mouseY);
		else
			scoreModel.render(batch, sr, mouseX, mouseY); //TODO: Split scoreModel into m&v
	}

	int mouseX, mouseY;

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean hasPressed = false;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		hasPressed = true;
		return false;
	}

	public boolean startGame = false;
	boolean inHighScore = false;

	private void handleClick(int x, int y, int pointer, int button) {
		int widthHalf = width / 2;
		int heightHalf = height / 2;
		if (!inHighScore) {
			if (model.startGame.collides(x, y, widthHalf, heightHalf)) {
				startGame = true;

			} else if (model.highScores.collides(x, y, widthHalf, heightHalf)) {
				inHighScore = true;

			} else if (model.quitGame.collides(x, y, widthHalf, heightHalf)) {
				Gdx.app.exit();
			}
		}
		else	//if in highscore
		{
			if(scoreModel.returnButton.collides(x, y,widthHalf, heightHalf))
			{
				inHighScore = false;
			}
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (hasPressed) {
			handleClick(screenX, screenY, pointer, button);
		}
		hasPressed = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
