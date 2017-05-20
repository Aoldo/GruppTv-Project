package com.grupptva.runnergame.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import com.grupptva.runnergame.game.model.worldgenerator.WorldGenerator;
import com.grupptva.runnergame.game.services.collision.CollisionChecker;
import com.grupptva.runnergame.game.services.collision.ICollisionChecker;
import com.grupptva.runnergame.game.view.GameRenderer;
import com.grupptva.runnergame.pluginsystem.ScenePlugin;

/**
 * @author Mattias revised by Karl and Agnes
 */
public class GameLogic implements InputProcessor {
	// private character
	public GameCharacter character;
	private WorldModel world;
	private WorldGenerator generator;
	private ICollisionChecker collisionChecker;
	private CollisionLogic collisionLogic;

	private int chunkWidth = 40;
	private int chunkHeight = 20;

	Chunk c = new Chunk(chunkWidth, chunkHeight);
	Chunk d = new Chunk(chunkWidth, chunkHeight);

	public int tileSize = 20;

	private float pixelsPerFrame = 3f;

	private final int jumpKeyCode = Input.Keys.SPACE;
	private final int hookKeyCode = Input.Keys.H;
	private final int resetKeyCode = Input.Keys.R;

	//
	public GameLogic() {
		Gdx.input.setInputProcessor(this);

		character = new GameCharacter(30, 150, pixelsPerFrame);
		world = new WorldModel();

		collisionChecker = new CollisionChecker();
		collisionLogic = new CollisionLogic(character, world, tileSize, collisionChecker);

		generator = new WorldGenerator(pixelsPerFrame, tileSize, 4l, chunkWidth, chunkHeight, 0, character);
		//generator = new WorldGenerator(character.getJumpInitialVelocity(),
		//		character.getGravity(), pixelsPerFrame, tileSize, 4l, chunkWidth, chunkHeight, 0, 1, 75);

		for (int x = 0; x < c.getTiles().length; x++) {
			for (int y = 0; y < c.getTiles()[0].length; y++) {
				if (y == 0) {
					c.getTiles()[x][y] = Tile.OBSTACLE;
				} else {
					c.getTiles()[x][y] = Tile.EMPTY;
				}
			}
		}
		for (int x = 0; x < c.getTiles().length; x++) {
			for (int y = 0; y < c.getTiles()[0].length; y++) {
				if (y == 4) {
					d.getTiles()[x][y] = Tile.OBSTACLE;
				} else {
					d.getTiles()[x][y] = Tile.EMPTY;
				}
			}
		}
		world.setChunks(new Chunk[]{c, d, c});

		//TODO: First 3 chunks should be a tutorial.
		world.setChunks(
				new Chunk[]{c, generator.generateChunk(), generator.generateChunk()});
	}

	public void update() {
		if (character.isDead()) {
			character.setDead(false);
			reset();
		} else {
			world.moveLeft(pixelsPerFrame);
			collisionLogic.handlePossibleCollision();
			character.update();
			if (world.getPosition() < -tileSize * chunkWidth) {
				world.incrementStartIndex();
				world.setPosition(0);
				world.updateChunk(generator.generateChunk());
			}
		}
		//move world here or world.update()?
		//world.moveLeft(pixelsPerFrame);
	}


	public WorldModel getWorld() {
		return world;
	}

	public void setWorld(WorldModel world) {
		this.world = world;
	}

	private void reset() {
		character = new GameCharacter(30, 150, pixelsPerFrame);

		generator = new WorldGenerator(pixelsPerFrame, tileSize, 5l, chunkWidth, chunkHeight, 0, character);

		world.setChunks(new Chunk[]{c, generator.generateChunk(), generator.generateChunk()});
		world.setPosition(0);
		world.setStartIndex(0);
		collisionLogic.setGameCharacter(character);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case jumpKeyCode:
				character.jump();
				return true;
			case hookKeyCode:
				character.initHook(75);
				return true;
			case resetKeyCode:
				reset();
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case hookKeyCode:
				if (character.isAttachedWithHook())
					character.removeHook();
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}