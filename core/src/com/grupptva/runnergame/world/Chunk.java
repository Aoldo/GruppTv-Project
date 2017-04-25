package com.grupptva.runnergame.world;

/**
 * Created on 4/24/17.
 *
 * The Chunk class represents a piece of the world. It's built up out of {@link Tile}s
 * and should be the buffer for the {@link WorldModel}.
 *
 * @author Karl 'NaN' Wikström
 */
public class Chunk {
	
	private Tile[][] tiles;
	
	private int width;
	
	private int height;
	
	public Chunk(Tile[][] tiles) {
		setTiles(tiles);
	}

	private boolean subarraysHasSameLength(Tile[][] tiles) {
		if(tiles.length == 0){
			return true;
		}
		int length = tiles[0].length;
		for (Tile[] arr : tiles) {
			if(arr.length != length){
				return false;
			}
		}
		return true;
	}

	public Chunk(int width, int height) {
		setWidth(width);
		setHeight(height);
		setTiles(new Tile[getWidth()][getHeight()]);
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
		setWidth(tiles.length);
		setHeight(tiles[0].length);
	}

	public int getWidth(){
		return width;
	}
	
	private void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private void setHeight(int height) {
		this.height = height;
	}
}
