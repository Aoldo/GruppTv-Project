package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A chunk used during world generation. Contains a grid of tiles aswell as
 * methods to manipulate them.
 * 
 * @author Mattias
 *
 */
class GeneratorChunk {
	public Tile[][] tiles;
	public int width;
	public int height;

	/**
	 * Different kinds of tiles used in generation of chunks. Some are used for
	 * visualizing the generation, and the rest represent values used in the
	 * actual gameworld:
	 * 
	 * Visual: POSSIBLEHOOK POSSIBLESTAND
	 * 
	 * Actual: EMPTY FULL HOOKTARGET TODO: When character's hook actually checks
	 * for collision.
	 * 
	 * @author Mattias
	 */
	enum Tile {
		EMPTY, FULL, POSSIBLEHOOK, POSSIBLESTAND, HOOKTARGET;
	}

	public GeneratorChunk(int width, int height) {
		//TODO: Failsafe if width/height < 1?
		tiles = new Tile[height][width];
		initTiles();
		this.width = width;
		this.height = height;
	}

	/**
	 * Takes every valid index from {@param offsets}, listed in
	 * {@param validIndexes}, individually adds them to {@param currentTile} and
	 * then changes the new index, represented by said sum, inside of
	 * {@param chunk} to {@param value}, if they aren't FULL.
	 * 
	 * @param chunk
	 *            The chunk whose values should be changed.
	 * @param offsets
	 *            A list containing offsets in relation to {@param currentTile}.
	 * @param validIndexes
	 *            A list containing every index inside {@param offsets} that is
	 *            valid, and should be used.
	 * @param currentTile
	 *            The position that every offset should be in relation to.
	 * @param value
	 *            The type of tile to set every valid chunk index to.
	 */
	void setValidOffsetsToValue(List<Integer[]> offsets, List<Integer> validIndexes, Integer[] currentTile,
			Tile value) {
		for (Integer index : validIndexes) {
			int x = offsets.get(index)[0] + currentTile[0];
			int y = offsets.get(index)[1] + currentTile[1];

			if (tiles[y][x] != Tile.FULL)
				tiles[y][x] = value;
		}
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
	public void clearPossibilities() {
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
	boolean isValidIndex(int x, int y) {
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
	List<Integer> getValidOffsetIndexes(List<Integer[]> offsets, Integer[] currentTile) {
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
	 * Creates a copy of tiles that doesn't reference the initial chunk's
	 * values, so that either version can be modified without affecting the
	 * other.
	 * 
	 * @param chunk
	 *            The Tile[][] to copy.
	 * @return The new copy.
	 */
	Tile[][] deepCopyTiles() {
		Tile[][] tilesCopy = new Tile[tiles.length][tiles[0].length];
		for (int y = 0; y < tiles.length; y++) {
			tilesCopy[y] = Arrays.copyOf(tiles[y], tiles[y].length);
		}
		return tilesCopy;
	}
}