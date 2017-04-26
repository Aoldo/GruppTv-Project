package com.grupptva.runnergame.world;


import java.awt.*;
import java.util.Arrays;

/**
 * Created by karlwikstrom on 2017-04-03.
 *
 * @author Karl 'NaN' Wikström
 */
public class WorldModel {

	/**
	 * The container which contains the world.
	 */
	private int[][] grid; // Should be replaced by chunks at some point

	/**
	 * The container which contains the world separated into {@link Chunk}s.
	 */
	private Chunk[] chunks;

	/**
	 * The width of the grid
	 */
	private int gridWidth;

	/**
	 * The height of the grid
	 */
	private int gridHeight;

	/**
	 * The position of the grid
	 */
	private Point position;

	/**
	 * Points to the element in the grid which should act as the first element in the
	 * world.
	 */
	private int startIndex = 0;

	/**
	 * The length of the world. <b>Not to be confused with the width of the grid!</b>
	 */
	private int worldLength = 8; // Debugging variable


	public WorldModel() {
		this(new Chunk[3], new Point(0, 0));
	}

	private WorldModel(Chunk[] chunks, Point position) {
		setChunks(chunks);
		setPosition(position);
	}

	/**
	 * Returns a part of a world that wraps around the end of the array to the beginning.
	 * <p>
	 * An example would be:
	 * <p>
	 * world = [1, 2, 3, 4]
	 * startIndex = 2
	 * length = 3
	 * returns -> [3, 4, 1]
	 *
	 * @param world      the world that the part is taken from
	 * @param startIndex the index at which the part should start
	 * @param length     the length of the part of the world
	 *
	 * @return a part of the world starting at startIndex wrapping around the end
	 */
	private int[][] getWorldPart(int[][] world, int startIndex, int length) {
		int[][] worldPart = new int[length][world.length];
		int index;
		for (int i = 0; i < length; i++) {
			index = (startIndex + i) % world.length;
			worldPart[i] = Arrays.copyOf(world[index], world[index].length);
		}
		return worldPart;
	}

	/**
	 * Runs {@link #getWorldPart(int[][], int, int)} with {@link #grid},
	 * {@link #startIndex} and {@link #worldLength} in respective order.
	 *
	 * @return a part of the world starting at the set startIndex wrapping around the end
	 */
	public int[][] getWorldPart() {
		return getWorldPart(grid, startIndex, worldLength);
	}

	/**
	 * Increments the {@link #startIndex} with a given value. Increments causing the
	 * startIndex to go beyond the end of the grid will cause the startIndex to wrap
	 * around to the beginning again.
	 *
	 * @param n the amount which the startIndex should be incremented with
	 */
	public void incrementStartIndexWith(int n) {
		setStartIndex((getStartIndex() + n) % getGridWidth());
	}

	/**
	 * Increments the {@link #startIndex}. If the startIndex points at last element in
	 * the grid when this method is called the startIndex will end up pointing to the
	 * first element.
	 */
	public void incrementStartIndex() {
		incrementStartIndexWith(1);
	}

	/**
	 * Gets the chunks in the order they're supposed to be shown.
	 *
	 * @return an array with the chunks starting with the chunk at {@link #startIndex}.
	 */
	public Chunk[] getChunksInRightOrder() {
		Chunk[] tempArr = new Chunk[getChunks().length];
		for (int i = 0; i < getChunks().length; i++) {
			tempArr[i] = getChunks()[(i + startIndex) % getChunks().length];
		}
		return tempArr;
	}


	// Getters and setters

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	private int getStartIndex() {
		return startIndex;
	}

	private void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	private int getGridWidth() {
		return gridWidth;
	}

	private Chunk[] getChunks() {
		return chunks;
	}

	private void setChunks(Chunk[] chunks) {
		this.chunks = chunks;
	}
}
