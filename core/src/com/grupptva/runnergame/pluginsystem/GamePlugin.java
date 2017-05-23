package com.grupptva.runnergame.pluginsystem;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.game.controller.GameController;

/**
 * A facade for the actual game. This is the class that should be run in order
 * to run the game.
 * 
 * @author Mattias
 *
 */
public class GamePlugin implements ScenePlugin {
	GameController controller;

	public GamePlugin() {
		controller = new GameController(Input.Keys.H, Input.Keys.SPACE, Input.Keys.R);
	}

	@Override
	public void update() {
		controller.update();

	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer sr) {
		controller.render(batch, sr);
	}

}
