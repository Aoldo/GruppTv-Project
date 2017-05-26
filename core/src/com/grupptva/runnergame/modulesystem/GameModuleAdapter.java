package com.grupptva.runnergame.modulesystem;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.game.controller.GameController;

/**
 * Adapter for the game package/module.
 * 
 * @author Mattias
 *
 */
public class GameModuleAdapter implements ModuleAdapter {
	GameController controller;

	public GameModuleAdapter() {
		controller = new GameController(Input.Keys.H, Input.Keys.SPACE, Input.Keys.R);
	}

	@Override
	public void update() {
		controller.update();
	}
	
	@Override
	public boolean inactive()
	{
		return false;
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer sr) {
		controller.render(batch, sr);
	}

}
