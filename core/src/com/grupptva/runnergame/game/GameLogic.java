package com.grupptva.runnergame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.gamecharacter.GameCharacter;

public class GameLogic {
	private GameCharacter character;
	// private world
	private float tileSize = 50;

	//TODO: Decide how to deal with the world moving, move it in this class or actually move it inside of the world class?
	public GameLogic() {
		character = new GameCharacter(30, 30);
	}

	public void update() {
		character.update();
		//move world here or world.update()?
	}

	public void render(ShapeRenderer sr) {
		renderCharacter(sr);
	}

	private void renderCharacter(ShapeRenderer sr) {
		sr.setColor(Color.FOREST);
		sr.rect(character.getPosition().getX(), character.getPosition().getY(), tileSize, tileSize);
	}

	private void checkCharacterCollision() {
		//if collision
		//send needed data to character:
		//character.collide(yPos)
	}
}