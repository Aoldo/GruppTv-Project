package com.grupptva.runnergame.worldgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.utils.Array;

public class WorldGenerator {
	final int chunkWidth = 50;
	final int chunkHeight = 20;
	Random rng;

	public enum Tile {
		EMPTY, FULL, POSSIBLEHOOK, POSSIBLESTAND;
	}

	/**
	 * The offsets compared to the "current tile" that the character can attach
	 * it's hook to.
	 */
	List<Integer[]> hookAttachOffsets;
	/**
	 * The offsets compared to the "current tile" that the character can jump
	 * and land on.
	 */
	List<Integer[]> jumpOffsets;
	/**
	 * The offsets compared to the "current tile"-which the hook is attached to
	 * that the character can land on.
	 * 
	 * TODO: This might depend on the radius of the hook and will probably cause
	 * issues later, need to figure out a fix.
	 */
	List<Integer[]> hookJumpOffsets;
	Long seed;

	/**
	 * Creates a new WorldGenerator, parameters are the coordinates in the grid
	 * that the character can reach in different ways, in relation the the tile
	 * the character is currently standing on, or attached to. The offset should
	 * be in integers that represent the index of the tiles in question inside
	 * of a chunk.
	 * 
	 * @param hookAttachOffsets
	 * @param jumpOffsets
	 * @param hookJumpOffsets
	 * @param seed
	 */
	public WorldGenerator(List<Integer[]> hookAttachOffsets, List<Integer[]> jumpOffsets,
			List<Integer[]> hookJumpOffsets, Long seed) {

		//Temporary solution to possible offsets
		jumpOffsets.add(new Integer[] { 1, 0 });
		jumpOffsets.add(new Integer[] { 2, 2 });
		jumpOffsets.add(new Integer[] { 3, 2 });
		jumpOffsets.add(new Integer[] { 3, 1 });
		jumpOffsets.add(new Integer[] { 3, 0 });
		jumpOffsets.add(new Integer[] { 3, -1 });
		jumpOffsets.add(new Integer[] { 4, -1 });
		jumpOffsets.add(new Integer[] { 3, -2 });
		jumpOffsets.add(new Integer[] { 4, -2 });

		hookAttachOffsets.add(new Integer[] { 2, 4 });

		//TODO: is in relation to the attachment point currently, change?
		hookJumpOffsets.add(new Integer[] { 0, -5 });
		hookJumpOffsets.add(new Integer[] { 1, -5 });
		hookJumpOffsets.add(new Integer[] { 2, -5 });
		hookJumpOffsets.add(new Integer[] { 3, -5 });
		hookJumpOffsets.add(new Integer[] { 4, -5 });
		hookJumpOffsets.add(new Integer[] { 5, -5 });
		hookJumpOffsets.add(new Integer[] { 6, -5 });
		hookJumpOffsets.add(new Integer[] { 7, -5 });
		hookJumpOffsets.add(new Integer[] { 8, -5 });
		hookJumpOffsets.add(new Integer[] { 9, -5 });

		hookJumpOffsets.add(new Integer[] { 3, -4 });
		hookJumpOffsets.add(new Integer[] { 4, -4 });
		hookJumpOffsets.add(new Integer[] { 5, -4 });
		hookJumpOffsets.add(new Integer[] { 6, -4 });
		hookJumpOffsets.add(new Integer[] { 7, -4 });
		hookJumpOffsets.add(new Integer[] { 8, -4 });
		hookJumpOffsets.add(new Integer[] { 9, -4 });

		hookJumpOffsets.add(new Integer[] { 5, -3 });
		hookJumpOffsets.add(new Integer[] { 6, -3 });
		hookJumpOffsets.add(new Integer[] { 7, -3 });
		hookJumpOffsets.add(new Integer[] { 8, -3 });

		hookJumpOffsets.add(new Integer[] { 5, -2 });
		hookJumpOffsets.add(new Integer[] { 6, -2 });
		hookJumpOffsets.add(new Integer[] { 7, -2 });

		hookJumpOffsets.add(new Integer[] { 5, -1 });
		hookJumpOffsets.add(new Integer[] { 6, -1 });
		hookJumpOffsets.add(new Integer[] { 7, -1 });

		hookJumpOffsets.add(new Integer[] { 6, 0 });
		hookJumpOffsets.add(new Integer[] { 6, 1 });

		this.hookAttachOffsets = hookAttachOffsets;
		this.jumpOffsets = jumpOffsets;
		this.hookJumpOffsets = hookJumpOffsets;
		rng = new Random(seed);
	}

	public Tile[][] generateChunk(int y) {

		return null;
	}

	public List<Tile[][]> generateChunkLog(int initY) {
		List<Tile[][]> chunkLog = new ArrayList<Tile[][]>();

		//Initialize the chunk, if the elements aren't set they are null which throws exceptions.
		Tile[][] chunk = new Tile[chunkHeight][chunkWidth];
		for (int y = 0; y < chunk.length; y++) {
			for (int x = 0; x < chunk[0].length; x++) {
				chunk[y][x] = Tile.EMPTY;
			}
		}

		//This is used to "crawl" through the possible paths inside of the chunk to the end of it.
		//x, y
		Integer[] currentTile = { 0, initY };

		//Add a tile for the player to stand on at the start of the chunk.
		chunk[currentTile[1]][currentTile[0]] = Tile.FULL;

		/*
		 * This line creates a copy of the current state of the chunk for
		 * visualization purposes. TODO: Remove every instance of this line and
		 * then copy the entire to generateChunk, and then return the final
		 * version of the chunk.
		 */
		//chunkLog.add(deepCopyChunk(chunk));

		/*
		 * Keep crawling forward step by step until the end of the chunk has
		 * been reached. Inside this loop is where the magic happens.
		 */
		while (currentTile[0] != chunk[0].length - 1) {
			chunk[currentTile[1]][currentTile[0]] = Tile.FULL;

			chunkLog.add(deepCopyChunk(chunk));
			clearPossibilities(chunk); //Used for visualization only!

			if(rng.nextInt(2) == 1)
				hookStep(chunk, currentTile,chunkLog);
			else
				jumpStep(chunk, currentTile);

			chunkLog.add(deepCopyChunk(chunk));
		}
		chunk[currentTile[1]][currentTile[0]] = Tile.FULL;
		chunkLog.add(deepCopyChunk(chunk));
		return chunkLog;
	}

	private void hookStep(Tile[][] chunk, Integer[] currentTile) {
		List<Integer> validHookAttachIndexes = getValidOffsetIndexes(hookAttachOffsets, currentTile);

		//In order to prevent changes to currentTile in case there is no valid path after the hook attachment point has been set.
		Integer[] currentTileCopy = new Integer[] { currentTile[0], currentTile[1] };

		if (validHookAttachIndexes.size() == 0) {
			//Failsafe to prevent infinite loop
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;
			return;
		}
		setValidOffsetsToValue(chunk, hookAttachOffsets, validHookAttachIndexes, currentTileCopy, Tile.POSSIBLEHOOK);

		Integer[] offset = hookAttachOffsets
				.get(validHookAttachIndexes.get(rng.nextInt(validHookAttachIndexes.size())));

		currentTileCopy[0] += offset[0];
		currentTileCopy[1] += offset[1];

		List<Integer> validJumpIndexes = getValidOffsetIndexes(hookJumpOffsets, currentTileCopy);
		if (validJumpIndexes.size() == 0) {
			//Failsafe to prevent infinite loop
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;
			return;
		}
		setValidOffsetsToValue(chunk, hookJumpOffsets, validJumpIndexes, currentTileCopy, Tile.POSSIBLESTAND);

		offset = hookJumpOffsets.get(validJumpIndexes.get(rng.nextInt(validJumpIndexes.size())));

		currentTile[0] = currentTileCopy[0] + offset[0];
		currentTile[1] = currentTileCopy[1] + offset[1];
	}	
	
	
	private void hookStep(Tile[][] chunk, Integer[] currentTile, List<Tile[][]> chunkLog) {
		List<Integer> validHookAttachIndexes = getValidOffsetIndexes(hookAttachOffsets, currentTile);

		//In order to prevent changes to currentTile in case there is no valid path after the hook attachment point has been set.
		Integer[] currentTileCopy = new Integer[] { currentTile[0], currentTile[1] };

		if (validHookAttachIndexes.size() == 0) {
			//Failsafe to prevent infinite loop
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;
			return;
		}
		setValidOffsetsToValue(chunk, hookAttachOffsets, validHookAttachIndexes, currentTileCopy, Tile.POSSIBLEHOOK);
		chunkLog.add(deepCopyChunk(chunk));

		Integer[] offset = hookAttachOffsets
				.get(validHookAttachIndexes.get(rng.nextInt(validHookAttachIndexes.size())));

		currentTileCopy[0] += offset[0];
		currentTileCopy[1] += offset[1];

		List<Integer> validJumpIndexes = getValidOffsetIndexes(hookJumpOffsets, currentTileCopy);
		if (validJumpIndexes.size() == 0) {
			//Failsafe to prevent infinite loop
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;
			return;
		}
		setValidOffsetsToValue(chunk, hookJumpOffsets, validJumpIndexes, currentTileCopy, Tile.POSSIBLESTAND);

		offset = hookJumpOffsets.get(validJumpIndexes.get(rng.nextInt(validJumpIndexes.size())));

		currentTile[0] = currentTileCopy[0] + offset[0];
		currentTile[1] = currentTileCopy[1] + offset[1];
	}

	/**
	 * Sets all tiles that aren't FULL or POSSIBLEHOOK to EMPTY, used to clear previous
	 * possibilities for the visualization.
	 * TODO: Change POSSIBLEHOOK to HOOKTARGET or something IF there are multiple hook possibilities.
	 * @param chunk
	 */
	private void clearPossibilities(Tile[][] chunk) {
		for (int y = 0; y < chunk.length; y++) {
			for (int x = 0; x < chunk[0].length; x++) {
				if (chunk[y][x] != Tile.FULL && chunk[y][x] != Tile.POSSIBLEHOOK) {
					chunk[y][x] = Tile.EMPTY;
				}
			}
		}
	}

	/**
	 * Called if the current step in generation is a jump. TODO: Better doc
	 * 
	 * @param chunk
	 * @param currentTile
	 */
	private void jumpStep(Tile[][] chunk, Integer[] currentTile) {
		List<Integer> validJumpIndexes = getValidOffsetIndexes(jumpOffsets, currentTile);
		if (validJumpIndexes.size() == 0) {
			//Failsafe to prevent infinite loop
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;

			return;
		}
		setValidOffsetsToValue(chunk, jumpOffsets, validJumpIndexes, currentTile, Tile.POSSIBLESTAND);

		Integer[] offset = jumpOffsets.get(validJumpIndexes.get(rng.nextInt(validJumpIndexes.size())));
		currentTile[0] += offset[0];
		currentTile[1] += offset[1];
	}

	private boolean isValidIndex(int x, int y) {
		if (y < chunkHeight && x < chunkWidth && x >= 0 && y >= 0) {
			return true;
		}
		return false;
	}

	private void setValidOffsetsToValue(Tile[][] chunk, List<Integer[]> offsets, List<Integer> validIndexes,
			Integer[] currentTile, Tile value) {
		for (Integer index : validIndexes) {
			int x = offsets.get(index)[0] + currentTile[0];
			int y = offsets.get(index)[1] + currentTile[1];

			chunk[y][x] = value;
		}
	}

	private List<Integer> getValidOffsetIndexes(List<Integer[]> offsets, Integer[] currentTile) {
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
	 * TODO: May not actually be a deep copy, but it works
	 * 
	 * @param chunk
	 * @return
	 */
	private Tile[][] deepCopyChunk(Tile[][] chunk) {
		Tile[][] newChunk = new Tile[chunk.length][chunk[0].length];
		for (int y = 0; y < chunk.length; y++) {
			newChunk[y] = Arrays.copyOf(chunk[y], chunk[y].length);
		}
		return newChunk;
	}
}
