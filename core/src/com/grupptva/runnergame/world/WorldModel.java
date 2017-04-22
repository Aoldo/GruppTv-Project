package com.grupptva.runnergame.world;


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
	private int[][] grid;
	
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
	private Position position;
	
	/**
	 * Points to the element in the grid which should act as the first element in the
	 * world.
	 */
	private int startIndex = 0;
	
	/**
	 * The length of the world. <b>Not to be confused with the width of the grid!</b>
	 */
	private int worldLength = 8; // Debugging variable
	
	/**
	 * The speed of the world
	 */
	private int speed = 10;
	
	public WorldModel() {
		this(1, 1, new Position());
	}
	
	public WorldModel(int width, int height, Position position) {
		gridWidth = width;
		gridHeight = height;
		grid = new int[gridWidth][gridHeight];
		this.position = position;
	}
	
	public WorldModel(int width, int height) {
		this(width, height, new Position());
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
	
}
