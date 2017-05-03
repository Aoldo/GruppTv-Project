package com.grupptva.runnergame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.gamecharacter.GameCharacter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.gamecharacter.Point;
import com.grupptva.runnergame.world.Chunk;
import com.grupptva.runnergame.world.Tile;
import com.grupptva.runnergame.world.WorldModel;

import static java.lang.System.out;

public class GameLogic {
	private GameCharacter character;
	private WorldModel world;
	private int chunkWidth = 40;
	private int chunkHeight = 20;

	Chunk c = new Chunk(chunkWidth, chunkHeight);
	Chunk d = new Chunk(chunkWidth, chunkHeight);

	private int tileSize = 20;

	private float pixelsPerFrame = 1.5f;

	//TODO: Decide how to deal with the world moving, move it in this class or actually move it inside of the world class?
	public GameLogic() {
		character = new GameCharacter(30, 150);
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
		world.setChunks(new Chunk[]{c, d, c});


	}

	public void update() {
		world.moveLeft(pixelsPerFrame);
		handlePossibleCharacterCollision();
		character.update();
		if(world.getPosition() < -tileSize * chunkWidth ){
			world.incrementStartIndex();
			world.setPosition(0);
		}
		//move world here or world.update()?

		/*if(isCharacterCollidingFromBelow()){
			handleCollisionFromBelow();
		}
		if(isCharacterCollidingFromRight()){
			handleCollisionFromRight();
		}*/
	}

	public void render(ShapeRenderer sr) {
		renderCharacter(sr);
		renderWorld(sr);
	}

	private void renderCharacter(ShapeRenderer sr) {
		sr.setColor(Color.FOREST);
		sr.rect(character.getPosition().getX(), character.getPosition().getY(), tileSize, tileSize);
	}

	private void handlePossibleCharacterCollision() {
		int indexOfFirstVisibleCol = ((int) Math.abs(world.getPosition()) / tileSize) % chunkWidth;
		out.println(indexOfFirstVisibleCol);
		character.setCollidingWithGround(false);

		for (int col = indexOfFirstVisibleCol; col < world.getChunksInRightOrder()
				[0].getTiles().length; col++) {


			for (int row = 0;
			     row < world.getChunksInRightOrder()[0].getTiles()[col].length;
			     row++) {


				float tileXPos = world.getPosition() + col * tileSize;
				float tileYPos = row * tileSize;
				if (2 * Math.abs(character.getPosition().getX() - tileXPos) <= tileSize
						&& 2 * Math.abs(character.getPosition().getY() - tileYPos) <=
						tileSize
						&& world.getChunksInRightOrder()[0].getTiles()[col][row] !=
						Tile.EMPTY) {
					// handle collision
					character.handleCollisionFromBelow(tileYPos + tileSize - 1);
					character.setCollidingWithGround(true);
				}
			}
		}
		/*for (int row = 0; row < world.getChunksInRightOrder()[1].getTiles()[0]
				.length; row++) {
			float tileXPos = world.getPosition() + world.getChunksInRightOrder()[1]
					.getTiles()[0].length * tileSize;
			float tileYPos = row * tileSize;
			if (2 * Math.abs(character.getPosition().getX() - tileXPos) <= tileSize &&
				2 *	Math.abs(character.getPosition().getY() - tileYPos) <= tileSize &&
					world.getChunksInRightOrder()[1].getTiles()[0][row] != Tile.EMPTY) {
				// handle x
				character.handleCollisionFromBelow(tileYPos + tileSize - 1);
				character.setCollidingWithGround(true);
			}
		}*/
	}

	private void checkCharacterCollision() {
		//if collision
		//send needed data to character:
		//character.collide(yPos)
	}

	private void renderWorld(ShapeRenderer renderer) {
		Chunk[] chunks = getWorld().getChunksInRightOrder();
		for (int i = 0; i < chunks.length; i++) {
			renderChunk(getWorld().getPosition(), chunks[i], i, renderer);
		}
	}

	private void renderChunk(float worldPos, Chunk chunk, int chunkNumber, ShapeRenderer
			renderer) {
		for (int col = 0; col < chunk.getWidth(); col++) {
			for (int row = 0; row < chunk.getHeight(); row++) {
				renderTile(worldPos + col * tileSize + chunkNumber * chunk.getTiles()
								.length *
								tileSize,
						chunk.getTiles()[col][row], col, row, chunkNumber, renderer);
			}
		}
	}

	private void renderTile(float tilePos, Tile tile, int col, int row, int chunkNumber,
	                        ShapeRenderer
			                        renderer) {
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