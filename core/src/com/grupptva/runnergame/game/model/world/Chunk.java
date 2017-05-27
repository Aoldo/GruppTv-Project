package com.grupptva.runnergame.game.model.world;

/**
 * Created on 2017-04-24
 *
 * Responsibility: The Chunk class represents a piece of the world. It's built up out of
 * {@link Tile}s and should be the buffer for the {@link WorldModel}.
 *
 * Used by:
 * @see WorldModel
 * @see com.grupptva.runnergame.game.model.GameLogic
 * @see com.grupptva.runnergame.game.model.worldgenerator.WorldGenerator
 * @see com.grupptva.runnergame.game.view.GameRenderer
 *
 * Uses:
 * @see Tile
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

	public Chunk(int width, int height) {
		setWidth(width);
		setHeight(height);
		tiles = new Tile[getWidth()][getHeight()];
		fillWith(Tile.EMPTY);
	}
	
	public void fillWith(Tile tile){
		for(int i = 0; i < getWidth(); i++){
			for(int j = 0; j < getHeight(); j++){
				setTile(i,j,tile);
			}
		}
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	final public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
		setWidth(tiles.length);
		setHeight(tiles[0].length);
	}

	final public int getWidth(){
		return width;
	}
	
	final private void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private void setHeight(int height) {
		this.height = height;
	}
	
	public void setTile(int col, int row, Tile tile){
		tiles[col][row] = tile;
	}
}
