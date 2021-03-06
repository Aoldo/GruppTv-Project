package com.grupptva.runnergame.game.model.world;

/**
 * Created on 2017-04-03.
 *
 * Responsibility: Represents the world in the game and provides functionality
 * to manipulate it.
 *
 * Used by:
 * @see com.grupptva.runnergame.game.model.CollisionLogic
 * @see com.grupptva.runnergame.game.model.GameLogic
 * @see com.grupptva.runnergame.game.model.HookLogic
 * @see com.grupptva.runnergame.game.view.GameRenderer
 *
 * Uses:
 * @see Chunk
 * @see Tile
 *
 *
 * @author Karl 'NaN' Wikström
 * Revised by: Mattias
 */
public final class WorldModel {

	/**
	 * The container which contains the world separated into {@link Chunk}s.
	 */
	private Chunk[] chunks;

	/**
	 * The position of the grid.
	 */
	private float position;

	/**
	 * Points to the element in the grid which should act as the first element in the
	 * world.
	 */
	private int startIndex = 0;

	public WorldModel() {
		this(new Chunk[3], 0f);
	}

	private WorldModel(Chunk[] chunks, float position) {
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

	public void updateChunk(Chunk chunk){
		chunks[(startIndex + chunks.length - 1) % chunks.length ] = chunk;
	}

	/**
	 * Moves the chunks in the world a distance to the left.
	 * @param distance  the distance to move the world.
	 */
	public void moveLeft(float distance){
		setPosition(getPosition() - distance);
	}
	
	/**
	 * Returns the nth column of Tiles counting from the left.
	 * @param n  the number of the column to get.
	 *
	 * @return the nth column.
	 */
	public Tile[] getColumn(int n){
		return getChunksInRightOrder()[(n - 1) / getChunksInRightOrder()[0].getWidth()]
				.getTiles()[(n - 1) % getChunksInRightOrder()[0].getWidth()];
	}

	// Getters and setters

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	private int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	private Chunk[] getChunks() {
		return chunks;
	}

	public void setChunks(Chunk[] chunks) {
		this.chunks = chunks;
	}
}
