package com.grupptva.runnergame.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.game.model.GameLogic;
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

	private final int hookKeyCode;
	private final int jumpKeyCode;
	private final int resetKeyCode;

	public GameController(int hookKeyCode, int jumpKeyCode, int resetKeyCode) {
		Gdx.input.setInputProcessor(this);
		
		this.hookKeyCode = hookKeyCode;
		this.jumpKeyCode = jumpKeyCode;
		this.resetKeyCode = resetKeyCode;

		model = new GameLogic();
		view = new GameRenderer();
	}

	public void update() {
		model.update();
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		view.render(batch, sr, model);
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
