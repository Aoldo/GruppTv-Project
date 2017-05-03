package com.grupptva.runnergame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.GamePlugin;
import com.grupptva.runnergame.gamecharacter.GameCharacter;
import com.grupptva.runnergame.world.Chunk;
import com.grupptva.runnergame.world.Tile;
import com.grupptva.runnergame.world.WorldModel;

/**
 * 
 * @author Mattias revised by Karl
 */
public class GameLogic implements GamePlugin {
	// private character
	private GameCharacter character;
	private WorldModel world;

	Chunk c = new Chunk(10, 5);
	Chunk d = new Chunk(10, 5);

	private int tileSize = 50;

	private float pixelsPerFrame = 1.5f;

	//TODO: Decide how to deal with the world moving, move it in this class or actually move it inside of the world class?
	public GameLogic() {
		character = new GameCharacter(30, 30);
		world = new WorldModel();
		for (int x = 0; x < c.getTiles().length; x++) {
			for (int y = 0; y < c.getTiles()[0].length; y++) {
				if (y == 3) {
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

	}

	public void update() {
		character.update();
		//move world here or world.update()?
		world.moveLeft(pixelsPerFrame);
		checkCharacterCollision();
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		renderCharacter(sr);
		renderWorld(sr);
		sr.end();
	}

	private void renderCharacter(ShapeRenderer sr) {
		sr.setColor(Color.FOREST);
		sr.rect(character.getPosition().getX(), character.getPosition().getY(), tileSize,
				tileSize);
	}

	private boolean isCharacterCollidingFromBelow() {
		return false;
	}

	private void checkCharacterCollision() {
		//if collision
		//send needed data to character:
		//character.handleCollisionFromBelow(10);
	}

	private void renderWorld(ShapeRenderer renderer) {
		Chunk[] chunks = getWorld().getChunksInRightOrder();
		for (int i = 0; i < chunks.length; i++) {
			renderChunk(getWorld().getPosition(), chunks[i], i, renderer);
		}
	}

	private void renderChunk(float worldPos, Chunk chunk, int chunkNumber,
			ShapeRenderer renderer) {
		for (int col = 0; col < chunk.getWidth(); col++) {
			for (int row = 0; row < chunk.getHeight(); row++) {
				renderTile(
						worldPos + col * tileSize
								+ chunkNumber * chunk.getTiles().length * tileSize,
						chunk.getTiles()[col][row], col, row, chunkNumber, renderer);
			}
		}
	}

	private void renderTile(float tilePos, Tile tile, int col, int row, int chunkNumber,
			ShapeRenderer renderer) {
		switch (tile) {
		case OBSTACLE:
			renderer.setColor(new Color(0, 0, 0, 1));
			renderer.rect(tilePos, row * tileSize, tileSize, tileSize);
			return;
		case EMPTY:
		default:
			return;
		}

	}

	public WorldModel getWorld() {
		return world;
	}

	public void setWorld(WorldModel world) {
		this.world = world;
	}
}