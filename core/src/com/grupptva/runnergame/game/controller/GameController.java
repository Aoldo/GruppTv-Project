package com.grupptva.runnergame.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.game.model.GameLogic;
import com.grupptva.runnergame.game.model.GameOverMenu;
import com.grupptva.runnergame.game.view.GameOverRenderer;
import com.grupptva.runnergame.game.view.GameRenderer;

/**
 * The controller class for the entire game module. Handles input aswell as
 * telling the model & view to update.
 * 
 * @author Mattias
 *
 */
public class GameController implements InputProcessor {
	GameLogic model;
	GameRenderer view;
	GameOverMenu gameOverModel;
	GameOverRenderer gameOverView;

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
		view = new GameRenderer();
		gameOverView = new GameOverRenderer();
	}

	public void update() {
		if (!isGameOver) {
			model.update();
			updateGameOver();
		} else {
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
	}

	boolean enterMenu = false;

	private void gameOverButtonCheck(int mouseX, int mouseY) {
		int widthHalf = Gdx.graphics.getWidth() / 2;
		int heightHalf = Gdx.graphics.getHeight() / 2;

		System.out.println("X:" + gameOverModel.playAgain.x + " > " + mouseX + " < "
				+ (gameOverModel.playAgain.x + gameOverModel.playAgain.width));
		System.out.println("Y:" + gameOverModel.playAgain.y + " > "
				+ (heightHalf * 2 - mouseY) + " < "
				+ (gameOverModel.playAgain.y + gameOverModel.playAgain.width));

		if (gameOverModel.playAgain.collides(mouseX, mouseY, widthHalf, heightHalf)) {
			System.out.println("RESTART");
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				restartGame();
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}

		} else if (gameOverModel.mainMenu.collides(mouseX, mouseY, widthHalf,
				heightHalf)) {
			System.out.println("LEAVE");
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {

				enterMenu = true;
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
			}
		}
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		view.render(batch, sr, model);
		if (isGameOver) {
			gameOverModel.render(batch, sr, Gdx.input.getX(), Gdx.input.getY()); //TODO: split into m&v
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
