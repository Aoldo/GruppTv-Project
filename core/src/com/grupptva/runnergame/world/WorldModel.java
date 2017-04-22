package com.grupptva.runnergame.world;


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
	
}
