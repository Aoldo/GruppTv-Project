package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Chunk {
	Tile[][] tiles;

	/**
	 * Different kinds of tiles used in generation of chunks. Some are used for
	 * visualizing the generation, and the rest represent values used in the
	 * actual gameworld.
	 * 
	 * @author Mattias
	 */
	public enum Tile {
		EMPTY, FULL, POSSIBLEHOOK, POSSIBLESTAND, HOOKTARGET;
	}

	
	
	public Chunk(int width, int height) {
		//TODO: Failsafe if width/height < 1?
		tiles = new Tile[height][width];
		initTiles();
	}

	/**
	 * Initializes every index in tiles in order to prevent
	 * nullpointerexceptions.
	 */
	private void initTiles() {
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				tiles[y][x] = Tile.EMPTY;
			}
		}
	}

	/**
	 * Sets all tiles that aren't FULL or HOOKTARGET to EMPTY, used to clear
	 * previous possibilities for the visualization.
	 */
	private void clearPossibilities() {
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				if (tiles[y][x] != Tile.FULL && tiles[y][x] != Tile.HOOKTARGET) {
					tiles[y][x] = Tile.EMPTY;
				}
			}
		}
	}

	/**
	 * Checks if a given index is inside of the bounds of chunkHeight &
	 * chunkWidth.
	 * 
	 * @param x
	 *            the x coordinate of the index (second dimension).
	 * @param y
	 *            the y coordinate of the index (first dimension).
	 * @return True if the given index is inside of the chunks bounds, otherwise
	 *         false.
	 */
	private boolean isValidIndex(int x, int y) {
		return (y < tiles.length && x < tiles[0].length && x >= 0 && y >= 0);
	}

	/**
	 * Returns a list of integers containing every the index of every offset
	 * that is inside the bounds of the chunk, in relation to
	 * {@param currentTile}.
	 * 
	 * @See chunkWidth, chunkHeight
	 * @param offsets
	 *            A list of offsets whose validity should be checked.
	 * @param currentTile
	 *            The position that the offsets are in relation to.
	 * @return An integer list with the index of every valid offset.
	 */
	public List<Integer> getValidOffsetIndexes(List<Integer[]> offsets,
			Integer[] currentTile) {
		List<Integer> validIndexes = new ArrayList<Integer>();

		for (int i = 0; i < offsets.size(); i++) {
			int x = offsets.get(i)[0] + currentTile[0];
			int y = offsets.get(i)[1] + currentTile[1];

			if (isValidIndex(x, y)) {
				validIndexes.add(i);
			}
		}
		return validIndexes;
	}

	/**
	 * Creates a copy of {@param chunk} that doesn't reference the initial
	 * chunk's values.
	 * 
	 * @param chunk
	 *            The Tile[][] to copy.
	 * @return The copy.
	 */
	private Tile[][] deepCopyTiles() {
		Tile[][] tilesCopy = new Tile[tiles.length][tiles[0].length];
		for (int y = 0; y < tiles.length; y++) {
			tilesCopy[y] = Arrays.copyOf(tiles[y], tiles[y].length);
		}
		return tilesCopy;
	}
}