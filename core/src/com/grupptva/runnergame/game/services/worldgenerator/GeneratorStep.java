package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.List;
import java.util.Random;

import com.grupptva.runnergame.game.services.worldgenerator.GeneratorChunk.Tile;;

abstract class GeneratorStep {
	int currentTileExtraBuffer = 3;
	int chance;

	Random rng;

	public abstract void step(GeneratorChunk chunk, Integer[] currentTile);

	public abstract void step(GeneratorChunk chunk, Integer[] currentTile, List<Tile[][]> chunkLog);

	/**
	 * Removes duplicates from {@param offsets}.
	 * 
	 * @param offsets
	 *            List whose duplicate values should be removed.
	 */
	protected void removeDuplicates(List<Integer[]> offsets) {
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
	 *  * Creates a platform (or line) of tiles all the way to the right edge of
	 * the chunk. Called to make sure there are no impossible to navigate gaps
	 * when a step fails due to there not being enough space left in the chunk.
	 * @param chunk
	 * @param currentTile
	 */
	protected void createPlatformToEdge(GeneratorChunk chunk, Integer[] currentTile) {
		int dx = chunk.width - currentTile[0];
		for (int x = 0; x < dx; x++) {
			chunk.tiles[currentTile[1]][x] = Tile.FULL;
		}
		currentTile[0] = chunk.width-1;
	}
}
