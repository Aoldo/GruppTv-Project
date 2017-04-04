package com.grupptva.runnergame.gamecore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.character.CharacterModel;
import com.grupptva.runnergame.world.World;

public class GameCore {
	CharacterModel character;
	World world;
	ShapeRenderer testRenderer;

	final short worldGridSize = 25;

	public GameCore() {
		character = new CharacterModel(100, 100);
		world = new World();
	}

	public void update() {
		testingMethodGround();

		testingInput();
		character.update();
	}

	private void testingInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			character.jump();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			character.initHook();
		}
		if(character.attachedWithHook)
		{
			if(!Gdx.input.isKeyPressed(Input.Keys.X))
			{
				character.stopHook();
			}
		}
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
		for (int y = 0; y < world.grid.length; y++) {
			for (int x = 0; x < world.grid[0].length; x++) {
				if (world.grid[y][x] != 0) {

					float dx = Math.abs(character.box.getCenterX()
							- (x * worldGridSize + worldGridSize / 2f));
					float dy = Math.abs(character.box.getCenterY()
							- (y * worldGridSize + worldGridSize / 2f));
					float totalWidth = character.box.getWidth() + worldGridSize;
					float totalHeight = character.box.getHeight() + worldGridSize;

					if (dx * 2 <= totalWidth && dy * 2 <= totalHeight) {

						//TODO: If collision with side => Trigger gameover!

						return y * worldGridSize + worldGridSize;
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
					testRenderer.rect(u * worldGridSize, i * worldGridSize, worldGridSize,
							worldGridSize);
			}
		}
		//Draw player hook circle
		if (character.attachedWithHook) {
			testRenderer.circle(character.hookPoint.getX(), character.hookPoint.getY(),
					character.hookRadius);
		}

		//Draw playerCharacter box
		testRenderer.setColor(0f, 0f, 0f, 1f);
		testRenderer.rect(character.getX(), character.getY(),
				character.box.dimensions.getX(), character.box.dimensions.getY());

		testRenderer.end();
	}
}