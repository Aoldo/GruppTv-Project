package com.grupptva.runnergame.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.GamePlugin;
import com.grupptva.runnergame.gamecharacter.GameCharacter;
import com.grupptva.runnergame.input.InputListener;
import com.grupptva.runnergame.world.Chunk;
import com.grupptva.runnergame.world.Tile;
import com.grupptva.runnergame.world.WorldModel;
import com.grupptva.runnergame.worldgenerator.WorldGenerator;

/**
 * 
 * @author Mattias revised by Karl
 */
public class GameLogic implements GamePlugin, InputListener {
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

	//
	public GameLogic() {
		character = new GameCharacter(30, 150);
		world = new WorldModel();

		List<Integer[]> hookAttachOffsets = new ArrayList<Integer[]>();
		List<Integer[]> hookJumpOffsets = new ArrayList<Integer[]>();
		List<Integer[]> jumpOffsets = new ArrayList<Integer[]>();

		generator = new WorldGenerator(hookAttachOffsets, jumpOffsets, hookJumpOffsets,
				1l, chunkWidth, chunkHeight);

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
		world.setChunks(new Chunk[] { c, generator.generateChunk(0),
				generator.generateChunk(5) });
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
		renderCharacter(sr);
		renderWorld(sr);
		sr.end();
	}

	private void renderCharacter(ShapeRenderer sr) {
		sr.setColor(Color.FOREST);
		sr.rect(character.getPosition().getX(), character.getPosition().getY(), tileSize,
				tileSize);
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

	@Override
	public void jumpPressed() {
		// TODO Auto-generated method stub
		character.jump();
		//System.out.println("JUMP HAS BEEN PRESSED IN GAMELOGIC");
	}

	@Override
	public void jumpReleased() {
		// TODO Auto-generated method stub
		//System.out.println("JUMP HAS BEEN RELEASED IN GAMELOGIC");
	}

	@Override
	public void hookPressed() {
		// TODO Auto-generated method stub
		//System.out.println("HOOK HAS BEEN RELEASED IN GAMELOGIC");
	}

	@Override
	public void hookReleased() {
		// TODO Auto-generated method stub
		//System.out.println("HOOK HAS BEEN RELEASED IN GAMELOGIC");

	}
}