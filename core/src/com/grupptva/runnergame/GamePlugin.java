package com.grupptva.runnergame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * 
 * @author Mattias
 */
public interface GamePlugin {
	public void update();

	public void render(SpriteBatch batch, ShapeRenderer sr);
}
