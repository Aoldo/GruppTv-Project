package com.grupptva.runnergame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Mattias
 *
 */
public class GameLogic {
	// private character
	// private world

	//TODO: Decide how to deal with the world moving, move it in this class or actually move it inside of the world class?
	public GameLogic() {
		
	}

	public void update() {
		//character.update();
		//move world here or world.update()?
	}

	public void render(SpriteBatch batch) {
		//character.render(batch);
		//world.render(batch);
	}

	private void checkCharacterCollision() {
		//if collision
		//send needed data to character:
		//character.collide(yPos)
	}
}