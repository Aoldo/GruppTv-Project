package com.grupptva.runnergame.gamecore;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.character.CharacterController;
import com.grupptva.runnergame.character.CharacterModel;
import com.grupptva.runnergame.world.World;

public class GameCore {
	CharacterModel character;
	World world;
	ShapeRenderer testRenderer;

	final short worldGridSize = 25;

	public GameCore() {
		character = new CharacterModel(0, 50);
		world = new World();
	}

	public void update() {
		testingMethodGround();

		character.update();
	}

	private void testingMethodGround() {
		int collisionY = testingCollisionGroundPosition();

		if (collisionY != -1) {
			character.collideWithGround(collisionY);
		} else {
			character.setGroundCollision(false);
		}
	}

	private int testingCollisionGroundPosition() {
		for (int i = 0; i < world.grid.length; i++) {
			for (int u = 0; u < world.grid[0].length; u++) {
				if (world.grid[i][u] != 0) {
					//Check if character position is inside of any world square, if it is return top of said square.
					//TODO: Actual rectangle collision checks.
					
					if (character.box.position.getY() > i * worldGridSize
							&& character.box.position.getY() < i * worldGridSize + worldGridSize
							&& character.box.position.getX() > u * worldGridSize
							&& character.box.position.getX() < u * worldGridSize + worldGridSize) {


						return i * worldGridSize + worldGridSize;
					}
				}

			}
		}
		return -1;

	}

	//ub3r l33t h4x
	public void render(SpriteBatch batch) {
		testRenderer = new ShapeRenderer();
		testRenderer.begin(ShapeType.Filled);

		//Draw world
		for (int i = 0; i < world.grid.length; i++) {
			for (int u = 0; u < world.grid[0].length; u++) {
				if (world.grid[i][u] != 0)
					testRenderer.rect(u * worldGridSize, i * worldGridSize, worldGridSize, worldGridSize);
			}
		}
		//Draw playerCharacter box
		testRenderer.setColor(0f, 0f, 0f, 1f);
		testRenderer.rect(character.getX(), character.getY(), character.box.dimensions.getX(),
				character.box.dimensions.getY());

		testRenderer.end();
	}
}
