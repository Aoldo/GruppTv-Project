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
		int[] currentTile = { 0, initY };

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
			currentTile[0]++;
			chunk[currentTile[1]][currentTile[0]] = Tile.FULL;

			for (Integer[] offset : jumpOffsets) {
				if (currentTile[1] + offset[1] < chunkHeight && currentTile[0] + offset[0] < chunkWidth) {
					chunk[currentTile[1] + offset[1]][currentTile[0] + offset[0]] = Tile.POSSIBLESTAND;
				}
			}

			chunkLog.add(deepCopyChunk(chunk));
		}

		return chunkLog;
	}

	private boolean isValidIndex(Integer[] index) {
		if (index[1] < chunkHeight && index[0] < chunkWidth)
			return true;
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
