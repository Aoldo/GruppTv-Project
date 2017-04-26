package com.grupptva.runnergame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.world.Chunk;
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
		//character.render(batch);
		//world.render(batch);
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
				renderTile(chunk.getTiles()[col][row], chunkNumber, renderer);
			}
		}
	}

	public WorldModel getWorld() {
		return world;
	}

	public void setWorld(WorldModel world) {
		this.world = world;
	}
}