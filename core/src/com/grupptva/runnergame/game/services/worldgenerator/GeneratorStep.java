package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.List;
import java.util.Random;

import com.grupptva.runnergame.game.services.worldgenerator.GeneratorChunk.Tile;;

/**
 * GeneratorStep is an abstract class that represents a single step in
 * generating a chunk, or a single "challenge" that the player has to overcome
 * in the world, for example jumping from a platform to another or using the
 * hook to swing across a gap between platforms. In other words a GeneratorStep
 * generates the next part of the world that the player is moving towards,
 * aswell as any extra things the player needs to get there, like an attachment
 * point for the hook.
 * 
 * Contains methods that are used in all sub classes, for example a method that
 * creates a platform that goes all the way to the edge of the chunk being
 * generated. This is for example used as a failsafe in case a step fails due to
 * there not being enough space left in the chunk being generated.
 * 
 * @author Mattias
 *
 */
abstract class GeneratorStep {
	/**
	 * Influences the maximum width of a platform. A higher value will give
	 * larger platforms when {@see createPlatform(..)} is called.
	 */
	int currentTileExtraBuffer = 3;
	/**
	 * The chance of the step occuring.
	 */
	int chance;

	/**
	 * A random number generator.
	 */
	Random rng;

	/**
	 * Modifies the {@param chunk} by creating the next part of the world in it.
	 * What it exactly does depends on implementation in subclasses. Also
	 * modifies {@param currentTile} to a viable position for the character to
	 * be standing at in the newly generated part of the chunk.
	 * 
	 * @param chunk
	 *            The chunk being modified.
	 * @param currentTile
	 *            The current position a player character would have in the
	 *            chunk, upon reaching the start of the newly generated part.
	 */
	public abstract void step(GeneratorChunk chunk, Integer[] currentTile);

	/**
	 * Does the same as the other step method except it also takes a list of the
	 * previous iterations of the tiles in the chunk as a parameter. This list
	 * is updated during the step so that it can be used to show the entire
	 * generation of the chunk step by step with the use of
	 * {@see GeneratorVisualizer}
	 * 
	 * @param chunk
	 *            The chunk being modified.
	 * @param currentTile
	 *            The current position a player character would have in the
	 *            chunk, upon reaching the start of the newly generated part.
	 * @param chunkLog
	 *            A list of previous iterations of the chunk.
	 * @see GeneratorVisualizer
	 */
	public abstract void step(GeneratorChunk chunk, Integer[] currentTile, List<Tile[][]> chunkLog);

	/**
	 * Removes duplicates from {@param offsets}.
	 * 
	 * 
	 * @param offsets
	 *            List whose duplicate values should be removed.
	 */
	protected void removeDuplicates(List<Integer[]> offsets) {
		//TODO: Not currently used in JumpStep due to the silly current implementation which 
		//puts all offsets in a booleangrid, effectively eliminating the use of this method there.
		
		
		//Remove duplicates
		for (int i = 0; i < offsets.size(); i++) {
			for (int u = i + 1; u < offsets.size(); u++) {
				if (offsets.get(i)[0] == offsets.get(u)[0] && offsets.get(i)[1] == offsets.get(u)[1]) {
					offsets.remove(u);
					u--;
				}
			}
		}
	}

	/**
	 * Adds a platform to the chunk, using currentTile as the leftmost tile.
	 * Size depends on currentTileExtraBuffer.
	 * 
	 * @param chunk
	 *            Chunk being generated.
	 * @param currentTile
	 *            Leftmost position of platform. Updates to a tile inside the
	 *            platform.
	 */
	protected void createPlatform(GeneratorChunk chunk, Integer[] currentTile) {
		if (currentTileExtraBuffer > 0) {
			int extraOffset = rng.nextInt(currentTileExtraBuffer) + 1;
			for (int i = 0; i <= extraOffset; i++) {
				if (chunk.isValidIndex(currentTile[0] + i, currentTile[1])) {
					chunk.tiles[currentTile[1]][currentTile[0] + i] = Tile.FULL;
				}
			}
			if (chunk.isValidIndex(currentTile[0] + extraOffset, currentTile[1])) {
				currentTile[0] += extraOffset;
				chunk.tiles[currentTile[1]][currentTile[0]] = Tile.POSSIBLEHOOK; //Used for visualization, turns tile purple

				extraOffset = rng.nextInt(2);
				for (int i = 1; i <= extraOffset; i++) {
					if (chunk.isValidIndex(currentTile[0] + i, currentTile[1])) {
						chunk.tiles[currentTile[1]][currentTile[0] + i] = Tile.FULL;
					}
				}
			}
		}
	}

	/**
	 * * Creates a platform (or line) of tiles all the way to the right edge of
	 * the chunk. Called to make sure there are no impossible to navigate gaps
	 * when a step fails due to there not being enough space left in the chunk.
	 * 
	 * @param chunk
	 * @param currentTile
	 */
	protected void createPlatformToEdge(GeneratorChunk chunk, Integer[] currentTile) {
		int dx = chunk.width - currentTile[0];
		for (int x = 0; x < dx; x++) {
			chunk.tiles[currentTile[1]][x] = Tile.FULL;
		}
		currentTile[0] = chunk.width - 1;
	}
}
