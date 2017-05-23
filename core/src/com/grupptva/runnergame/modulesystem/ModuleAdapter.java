package com.grupptva.runnergame.modulesystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Interface used as an adapter to turn a controller class into a module that
 * RunnerGame can run.
 * 
 * @author Mattias
 */
public interface ModuleAdapter {
	/**
	 * Tells the controller to update the model.
	 */
	public void update();

	/**
	 * Tells the controller to update the view.
	 * 
	 * @param batch
	 * @param sr
	 */
	public void render(SpriteBatch batch, ShapeRenderer sr);
}
