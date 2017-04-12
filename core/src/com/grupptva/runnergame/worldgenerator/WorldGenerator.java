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
		chunkLog.add(deepCopyChunk(chunk));

		/*
		 * Keep crawling forward step by step until the end of the chunk has
		 * been reached. Inside this loop is where the magic happens.
		 */
		while (currentTile[0] != chunk[0].length - 1) {
			chunk[currentTile[1]][currentTile[0]] = Tile.FULL;

			jumpStep(chunk,currentTile);
			
			chunkLog.add(deepCopyChunk(chunk));
		}
		chunk[currentTile[1]][currentTile[0]] = Tile.FULL;
		chunkLog.add(deepCopyChunk(chunk));
		return chunkLog;
	}
	
	private void hookStep(Tile[][] chunk, Integer[] currentTile)
	{		

	}
	
	private List<Integer> getValidOffsetIndexes(List<Integer[]> offsets, Integer[] currentTile)
	{
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
	 * Called if the current step in generation is a jump.
	 * TODO: Better doc
	 * @param chunk 
	 * @param currentTile
	 */
	private void jumpStep(Tile[][] chunk, Integer[] currentTile)
	{
		List<Integer> validJumpIndexes = getValidOffsetIndexes(jumpOffsets, currentTile);
		/*List<Integer> validJumpIndexes = new ArrayList<Integer>();

		for (int i = 0; i < jumpOffsets.size(); i++) {
			int x = jumpOffsets.get(i)[0] + currentTile[0];
			int y = jumpOffsets.get(i)[1] + currentTile[1];

			if (isValidIndex(x, y)) {
				chunk[y][x] = Tile.POSSIBLESTAND;
				validJumpIndexes.add(i);
			}
		}*/
		if (validJumpIndexes.size() == 0)
			return;

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
