package com.grupptva.runnergame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.world.Chunk;
import com.grupptva.runnergame.world.Tile;
import com.grupptva.runnergame.world.WorldModel;

public class GameLogic {
	// private character
	private WorldModel world;

	private int tileSize = 50;

	//TODO: Decide how to deal with the world moving, move it in this class or actually move it inside of the world class?
	public GameLogic() {

	}

	public void update() {
		//character.update();
		//move world here or world.update()?
	}

	public void render(ShapeRenderer renderer) {
		renderWorld(renderer);
	}

	private void checkCharacterCollision() {
		//if collision
		//send needed data to character:
		//character.collide(yPos)
	}

	private void renderWorld(ShapeRenderer renderer){
		Chunk[] chunks = getWorld().getChunksInRightOrder();
		for(int i = 0; i < chunks.length; i++){
			renderChunk(chunks[i], i, renderer);
		}
	}

	private void renderChunk(Chunk chunk, int chunkNumber, ShapeRenderer renderer) {
		for(int col = 0; col < chunk.getWidth(); col++){
			for(int row = 0; row < chunk.getHeight(); row++){
				renderTile(chunk.getTiles()[col][row], col, row, chunkNumber, renderer);
			}
		}
	}

	private void renderTile(Tile tile, int col, int row, int chunkNumber, ShapeRenderer
			renderer) {
		switch(tile){
			case OBSTACLE:
				renderer.setColor(new Color(0,0,0,1));
				renderer.rect(col * tileSize * chunkNumber, row * tileSize, tileSize,
						tileSize);
				return;
			case EMPTY:
			default:
				return;
		}

	}

	public WorldModel getWorld() {
		return world;
	}

	public void setWorld(WorldModel world) {
		this.world = world;
	}
}