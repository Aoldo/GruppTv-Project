package com.grupptva.runnergame.modulesystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * 
 * @author Mattias
 */
public interface ModuleAdapter {
	public void update();

	public void render(SpriteBatch batch, ShapeRenderer sr);
}
