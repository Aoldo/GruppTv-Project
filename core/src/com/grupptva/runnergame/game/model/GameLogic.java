package com.grupptva.runnergame.game.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.ScenePlugin;
import com.grupptva.runnergame.controller.InputListener;
import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import com.grupptva.runnergame.game.services.WorldGenerator;
import com.grupptva.runnergame.game.view.GameRenderer;

/**
 * 
 * @author Mattias revised by Karl and Agnes
 */
public class GameLogic implements ScenePlugin, InputProcessor {
	GameRenderer gameRenderer;
	// private character
	private GameCharacter character;
	private WorldModel world;
	private WorldGenerator generator;

	private int chunkWidth = 40;
	private int chunkHeight = 20;

	Chunk c = new Chunk(chunkWidth, chunkHeight);
	Chunk d = new Chunk(chunkWidth, chunkHeight);

	private int tileSize = 20;

	private float pixelsPerFrame = 1.5f;

	private final int jumpKeyCode = Input.Keys.SPACE;
	private final int hookKeyCode = Input.Keys.H;
	private final int resetKeyCode = Input.Keys.R;

	//
	public GameLogic() {
		Gdx.input.setInputProcessor(this);

		gameRenderer = new GameRenderer();
		character = new GameCharacter(30, 150, pixelsPerFrame);
		world = new WorldModel();

		List<Integer[]> hookAttachOffsets = new ArrayList<Integer[]>();
		List<Integer[]> hookJumpOffsets = new ArrayList<Integer[]>();
		List<Integer[]> jumpOffsets = new ArrayList<Integer[]>();

		generator = new WorldGenerator(character.getJumpInitialVelocity(),
				character.getGravity(), pixelsPerFrame, 8, 4l, chunkWidth, chunkHeight, 0);

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
		world.setChunks(new Chunk[] { c, d, c });

		//TODO: First 3 chunks should be a tutorial.
		world.setChunks(
				new Chunk[] { c, generator.generateChunk(), generator.generateChunk() });
	}

	public void update() {
		world.moveLeft(pixelsPerFrame);
		handlePossibleCharacterCollision();
		character.update();
		if (world.getPosition() < -tileSize * chunkWidth) {
			world.incrementStartIndex();
			world.setPosition(0);
		}
		//move world here or world.update()?
		world.moveLeft(pixelsPerFrame);
		checkCharacterCollision();

		/*
		 * if(isCharacterCollidingFromBelow()){ handleCollisionFromBelow(); }
		 * if(isCharacterCollidingFromRight()){ handleCollisionFromRight(); }
		 */
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		gameRenderer.renderCharacter(tileSize, character, sr);
		gameRenderer.renderWorld(tileSize, getWorld(), sr);
		sr.end();
	}

	private void handlePossibleCharacterCollision() {
		int indexOfFirstVisibleCol = ((int) Math.abs(world.getPosition()) / tileSize)
				% chunkWidth;
		//out.println(indexOfFirstVisibleCol);
		character.setCollidingWithGround(false);

		for (int col = indexOfFirstVisibleCol; col < world.getChunksInRightOrder()[0]
				.getTiles().length; col++) {

			for (int row = 0; row < world.getChunksInRightOrder()[0]
					.getTiles()[col].length; row++) {

				float tileXPos = world.getPosition() + col * tileSize;
				float tileYPos = row * tileSize;
				if (world.getChunksInRightOrder()[0].getTiles()[col][row] != Tile.EMPTY
						&& 2 * Math.abs(character.getPosition().getX() - tileXPos) <= 2
								* tileSize
						&& 2 * Math.abs(character.getPosition().getY() - tileYPos) <= 2
								* tileSize) {
					// handle collision
					character.handleCollisionFromBelow(tileYPos + tileSize);
					character.setCollidingWithGround(true);
				}
			}
		}
		for (int col = 0; col < 5; col++) {

			for (int row = 0; row < world.getChunksInRightOrder()[1]
					.getTiles()[col].length; row++) {

				float tileXPos = world.getPosition() + col * tileSize
						+ chunkWidth * tileSize;
				float tileYPos = row * tileSize;
				if (world.getChunksInRightOrder()[1].getTiles()[col][row] != Tile.EMPTY
						&& 2 * Math.abs(character.getPosition().getX() - tileXPos) <= 2
								* tileSize
						&& 2 * Math.abs(character.getPosition().getY() - tileYPos) <= 2
								* tileSize) {
					// handle collision
					character.handleCollisionFromBelow(tileYPos + tileSize);
					character.setCollidingWithGround(true);
				}
			}
		}
	}

	private void checkCharacterCollision() {
		//if collision
		//send needed data to character:
		//character.handleCollisionFromBelow(10);
	}

	public WorldModel getWorld() {
		return world;
	}

	public void setWorld(WorldModel world) {
		this.world = world;
	}

	private void reset() {
		character = new GameCharacter(30, 150, pixelsPerFrame);
		world.setChunks(new Chunk[] { c, generator.generateChunk(),
				generator.generateChunk() });
		world.setPosition(0);
		world.setStartIndex(0);
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
				if(character.isAttachedWithHook())
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