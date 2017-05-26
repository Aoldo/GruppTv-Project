package com.grupptva.runnergame.game.model;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import com.grupptva.runnergame.game.model.worldgenerator.WorldGenerator;
import com.grupptva.runnergame.game.services.collision.CollisionChecker;
import com.grupptva.runnergame.game.services.collision.ICollisionChecker;
import com.grupptva.runnergame.utils.HighScore;
import com.grupptva.runnergame.utils.HighScoresData;

/**
 * @author Mattias revised by Karl and Agnes
 */
public class GameLogic {
	// private character
	public GameCharacter character;
	private WorldModel world;
	private WorldGenerator generator;
	private ICollisionChecker collisionChecker;
	private CollisionLogic collisionLogic;
	private HookLogic hookLogic;

	private int chunkWidth = 40;
	private int chunkHeight = 20;

	Chunk c = new Chunk(chunkWidth, chunkHeight);
	Chunk d = new Chunk(chunkWidth, chunkHeight);

	public int tileSize = 20;

	private float pixelsPerFrame = 3f;

	public int score = 0;
	
	//
	public GameLogic() {

		character = new GameCharacter(30, 150, pixelsPerFrame);
		world = new WorldModel();

		collisionChecker = new CollisionChecker();
		collisionLogic = new CollisionLogic(character, world, tileSize, collisionChecker);

		hookLogic = new HookLogic(character, world, tileSize);

		generator = new WorldGenerator(pixelsPerFrame, tileSize, 4l, chunkWidth,
				chunkHeight, 0, character);
		

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
		if (character.isDead()) {
			character.setDead(false);
			reset();
		} else {
			score++;
			world.moveLeft(pixelsPerFrame);
			collisionLogic.handlePossibleCollision();
			character.update();
			searchHook();
			if (world.getPosition() < -tileSize * chunkWidth) {
				world.incrementStartIndex();
				world.setPosition(0);
				world.updateChunk(generator.generateChunk());
			}
			if(character.getPosition().getY() <= tileSize*-2)
			{
				character.setDead(true);
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

		generator = new WorldGenerator(pixelsPerFrame, tileSize, 5l, chunkWidth,
				chunkHeight, 0, character);

		world.setChunks(
				new Chunk[] { c, generator.generateChunk(), generator.generateChunk() });
		world.setPosition(0);
		world.setStartIndex(0);
		collisionLogic.setGameCharacter(character);
		hookLogic = new HookLogic(character, world, tileSize);

	}

	private int hookCounter = 0;
	private int maxHookCounter = 10;

	private void startHook() {
		hookCounter = 0;
	}

	private void searchHook() {
		if (character.isAttachedWithHook())
			hookCounter = maxHookCounter;
		if (hookCounter < maxHookCounter) {
			hookLogic.castHook();
			hookCounter++;
		}
	}

	public void recieveJumpPressed() {
		character.jump();
	}

	public void recieveHookPressed() {
		startHook();
	}

	public void recieveResetPressed() {
		reset();
	}

	public void recieveHookReleased() {
		if (character.isAttachedWithHook())
			character.removeHook();
	}
}