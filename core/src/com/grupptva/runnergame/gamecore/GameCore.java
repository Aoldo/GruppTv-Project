package com.grupptva.runnergame.gamecore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.character.CharacterModel;
import com.grupptva.runnergame.character.Rectangle;
import com.grupptva.runnergame.world.GridWorld;
import com.grupptva.runnergame.world.World;

public class GameCore {
	CharacterModel character;
	GridWorld world;
	World secondWorld;
	ShapeRenderer testRenderer;

	final short worldGridSize = 25;

	public GameCore() {
		character = new CharacterModel(100, 100);
		world = new GridWorld();

		secondWorld = new World();
	}

	/**
	 * Updates the game state, should be called once per iteration of the game's
	 * logic loop.
	 */
	public void update() {
		character.update();
		testingMethodGround();
		testingInput();

		testingMoveGround();
	}

	/**
	 * Reads input and forwards it to the character in the form of method calls.
	 */
	private void testingInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			character.jump();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			character.initHook();
		}
		if (character.attachedWithHook) {
			if (!Gdx.input.isKeyPressed(Input.Keys.X)) {
				character.stopHook();
			}
		}
		if (Gdx.input.isButtonPressed(0)) {
			secondWorld.obstacles.add(new Rectangle(Gdx.input.getX(),
					Gdx.graphics.getHeight() - Gdx.input.getY(), 25, 25));
		}
	}

	/**
	 * Tells the character if it has collided with anything.
	 */
	private void testingMethodGround() {
		int collisionY = testingCollisionGroundPosition();

		if (collisionY != -1) {
			character.collideWithGround(collisionY);
		} else {
			character.setGroundCollision(false);
		}
	}

	private void testingMoveGround() {
		List<Integer> oobIndexes = new ArrayList<Integer>();
		
		Random rng = new Random();

		for (int i = 0; i < secondWorld.obstacles.size(); i++) {
			Rectangle obstacle = secondWorld.obstacles.get(i);

			obstacle.position.moveX(-character.xVelocity);
			if (obstacle.position.getX() + obstacle.getWidth() < 0) {
				secondWorld.obstacles.set(i, new Rectangle(500, rng.nextInt(75), 75, 25));
			}
		}

	}

	/**
	 * Does collision between the character and the world and returns the y
	 * coordinate that the character should be at in order to collide against
	 * the ground with one pixel.
	 * 
	 * @return the y coordinate one pixel below the top of the object the
	 *         character is colliding with.
	 */
	private int testingCollisionGroundPosition() {
		//Not grid collision
		for (Rectangle obstacle : secondWorld.obstacles) {
			float dx = Math.abs(character.box.getCenterX() - character.getX() + 50
					- obstacle.getCenterX());
			float dy = Math.abs(character.box.getCenterY() - obstacle.getCenterY());
			float totalWidth = character.box.getWidth() + obstacle.getWidth();
			float totalHeight = character.box.getHeight() + obstacle.getHeight();

			if (dx * 2 <= totalWidth && dy * 2 <= totalHeight) {

				//TODO: If collision with side => Trigger gameover!

				return (int) (obstacle.position.getY() + obstacle.getHeight());
			}
		}

		//GridWorld collision
		/*
		 * for (int y = 0; y < world.grid.length; y++) { for (int x = 0; x <
		 * world.grid[0].length; x++) { if (world.grid[y][x] != 0) {
		 * 
		 * float dx = Math.abs(character.box.getCenterX() - (x * worldGridSize +
		 * worldGridSize / 2f)); float dy = Math.abs(character.box.getCenterY()
		 * - (y * worldGridSize + worldGridSize / 2f)); float totalWidth =
		 * character.box.getWidth() + worldGridSize; float totalHeight =
		 * character.box.getHeight() + worldGridSize;
		 * 
		 * if (dx * 2 <= totalWidth && dy * 2 <= totalHeight) {
		 * 
		 * //TODO: If collision with side => Trigger gameover!
		 * 
		 * return y * worldGridSize + worldGridSize; } }
		 * 
		 * } } return -1;
		 */
		return -1;
	}

	/**
	 * Renders the world and the character.
	 * 
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		testRenderer = new ShapeRenderer();
		testRenderer.begin(ShapeType.Filled);

		//Draw grid world
		/*
		 * for (int i = 0; i < world.grid.length; i++) { for (int u = 0; u <
		 * world.grid[0].length; u++) { if (world.grid[i][u] != 0)
		 * testRenderer.rect(u * worldGridSize, i * worldGridSize,
		 * worldGridSize, worldGridSize); } }
		 */

		//Draw not grid world

		for (Rectangle obstacle : secondWorld.obstacles) {
			testRenderer.rect(obstacle.position.getX(), obstacle.position.getY(),
					obstacle.getWidth(), obstacle.getHeight());
		}
		testRenderer.end();
		testRenderer.begin(ShapeType.Line);
		//Draw player hook circle
		if (character.attachedWithHook) {
			testRenderer.circle(character.hookPoint.getX() - character.getX() + 50,
					character.hookPoint.getY(), character.hookRadius);
		}

		testRenderer.end();
		testRenderer.begin(ShapeType.Filled);
		//Draw playerCharacter box

		testRenderer.setColor(0f, 0f, 0f, 1f);
		testRenderer.rect(50, character.getY(), character.box.dimensions.getX(),
				character.box.dimensions.getY());

		testRenderer.end();
	}
}