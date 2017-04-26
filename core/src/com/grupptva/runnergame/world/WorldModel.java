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
	 * The container which contains the world separated into {@link Chunk}s.
	 */
	private Chunk[] chunks;

	/**
	 * The position of the grid.
	 */
	private Point position;

	/**
	 * Points to the element in the grid which should act as the first element in the
	 * world.
	 */
	private int startIndex = 0;

	public WorldModel() {
		this(new Chunk[3], new Point(0, 0));
	}

	private WorldModel(Chunk[] chunks, Point position) {
		setChunks(chunks);
		setPosition(position);
	}

	/**
	 * Increments the {@link #startIndex} with a given value. Increments causing the
	 * startIndex to go beyond the end of the chunk array will cause the startIndex to
	 * wrap around to the beginning again.
	 *
	 * @param n the amount which the startIndex should be incremented with
	 */
	private void incrementStartIndexWith(int n) {
		setStartIndex((getStartIndex() + n) % getChunks().length);
	}

	/**
	 * Increments the {@link #startIndex}. If the startIndex points at last element in
	 * the chunk array when this method is called the startIndex will end up pointing to
	 * the first element.
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

	private Chunk[] getChunks() {
		return chunks;
	}

	private void setChunks(Chunk[] chunks) {
		this.chunks = chunks;
	}
}
