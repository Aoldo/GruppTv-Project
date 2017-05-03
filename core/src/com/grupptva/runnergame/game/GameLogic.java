package com.grupptva.runnergame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.GamePlugin;

/**
 * 
 * @author Mattias
 *
 */
public class GameLogic implements GamePlugin{
	// private character
	// private world

	//TODO: Decide how to deal with the world moving, move it in this class or actually move it inside of the world class?
	public GameLogic() {
		
	}

	public void update() {
		//character.update();
		//move world here or world.update()?
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		//character.render(batch);
		//world.render(batch);
	}

	private void checkCharacterCollision() {
		//if collision
		//send needed data to character:
		//character.collide(yPos)
	}
}