package com.grupptva.runnergame.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.game.model.GameLogic;
import com.grupptva.runnergame.game.model.GameOverMenu;
import com.grupptva.runnergame.game.view.GameOverRenderer;
import com.grupptva.runnergame.game.view.GameRenderer;
import com.grupptva.runnergame.utils.HighScore;
import com.grupptva.runnergame.utils.HighScoresData;

/**
 * The controller class for the entire game module. Handles input aswell as
 * telling the model & view to update.
 * 
 * @author Mattias
 * 
 * Uses:
 * @see com.grupptva.runnergame.game.model.GameLogic
 * @see com.grupptva.runnergame.game.model.GameOverMenu
 * @see com.grupptva.runnergame.game.view.GameRenderer
 * @see com.grupptva.runnergame.game.view.GameOverRenderer
 * 
 * Used by:
 * @see com.grupptva.runnergame.modulesystem.GameModuleAdapter
 *
 */
public class GameController implements InputProcessor {
	GameLogic model;
	GameOverMenu gameOverModel;

	private final int hookKeyCode;
	private final int jumpKeyCode;
	private final int resetKeyCode;
	boolean isGameOver = false;
	
	public GameController(int hookKeyCode, int jumpKeyCode, int resetKeyCode) {
		Gdx.input.setInputProcessor(this);

		this.hookKeyCode = hookKeyCode;
		this.jumpKeyCode = jumpKeyCode;
		this.resetKeyCode = resetKeyCode;

		model = new GameLogic();
	}

	boolean gameOverLastFrame = false;

	public void update() {
		if (!isGameOver) {
			model.update();
			updateGameOver();
		} else {
			if (!gameOverLastFrame) {
				gameOverModel.score = model.score;
				new HighScoresData().addScore(new HighScore(model.score, "---")); //HighScoresData might aswell be static if this works.
			}
			gameOverLastFrame = true;
			gameOverButtonCheck(Gdx.input.getX(), Gdx.input.getY());
		}
	}

	private void updateGameOver() {
		if (model.character.isDead()) {
			isGameOver = true;
			gameOverModel = new GameOverMenu(model.score);
		}
	}

	private void restartGame() {
		model = new GameLogic();
		isGameOver = false;
		gameOverLastFrame = false;
	}

	public boolean enterMenu = false;

	private void gameOverButtonCheck(int mouseX, int mouseY) {
		int widthHalf = Gdx.graphics.getWidth() / 2;
		int heightHalf = Gdx.graphics.getHeight() / 2;

		if (gameOverModel.playAgain.collides(mouseX, mouseY, widthHalf, heightHalf)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				restartGame();
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}

		} else if (gameOverModel.mainMenu.collides(mouseX, mouseY, widthHalf,
				heightHalf)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {

				enterMenu = true;
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}
		}
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		GameRenderer.render(batch, sr, model);
		if (isGameOver) {
			GameOverRenderer.render(batch, sr, Gdx.input.getX(), Gdx.input.getY(),
					gameOverModel);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == jumpKeyCode) {
			model.recieveJumpPressed();
			return true;
		}
		if (keycode == hookKeyCode) {
			model.recieveHookPressed();
			return true;
		}
		if (keycode == resetKeyCode) {
			model.recieveResetPressed();
			return true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == hookKeyCode) {
			model.recieveHookReleased();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
