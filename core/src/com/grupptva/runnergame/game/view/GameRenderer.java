package com.grupptva.runnergame.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.grupptva.runnergame.game.model.GameLogic;
import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;

/**
 * Created on 2017-05-03.
 *
 * Responsibility: Renders the game.
 *
 * Used by:
 * @see com.grupptva.runnergame.game.controller.GameController
 *
 * Uses:
 * @see com.grupptva.runnergame.game.model.GameLogic
 * @see com.grupptva.runnergame.game.model.gamecharacter.GameCharacter
 * @see com.grupptva.runnergame.game.model.world.Chunk
 * @see com.grupptva.runnergame.game.model.world.Tile
 * @see com.grupptva.runnergame.game.model.world.WorldModel
 *
 * @author Karl
 * Revised by Mattias
 * 
 * Uses:
 * @see com.grupptva.runnergame.game.model.gamecharacter.GameCharacter
 * @see com.grupptva.runnergame.game.model.world.WorldModel
 * @see com.grupptva.runnergame.game.model.GameLogic
 * 
 * Used by:
 * @see com.grupptva.runnergame.game.controller.GameController
 */
public final class GameRenderer {
	private GameRenderer() //Class only has methods.
	{
		
	}
	public static void render(SpriteBatch batch, ShapeRenderer sr, GameLogic model) {
		sr.begin(ShapeType.Filled);
		renderCharacter(model.tileSize, model.character, sr);
		renderWorld(model.tileSize, model.getWorld(), sr);
		sr.end();
	}

	public static void renderCharacter(int tileSize, GameCharacter gameCharacter,
			ShapeRenderer sr) {
		sr.setColor(Color.FOREST);
		sr.rect(gameCharacter.getPosition().getX(), gameCharacter.getPosition().getY(),
				tileSize, tileSize);
		if (gameCharacter.isAttachedWithHook()) {
			sr.setColor(Color.CHARTREUSE);

			sr.line(gameCharacter.getPosition().getX() + tileSize / 2,
					gameCharacter.getPosition().getY() + tileSize / 2,
					gameCharacter.getHook().getPosition().getX() + tileSize / 2,
					gameCharacter.getHook().getPosition().getY() + tileSize / 2);
		}

	}

	public static void renderWorld(int tileSize, WorldModel world, ShapeRenderer renderer) {
		Chunk[] chunks = world.getChunksInRightOrder();
		for (int i = 0; i < chunks.length; i++) {
			renderChunk(tileSize, world.getPosition(), chunks[i], i, renderer);
		}
	}

	private static void renderChunk(int tileSize, float worldPos, Chunk chunk, int chunkNumber,
			ShapeRenderer renderer) {
		for (int col = 0; col < chunk.getWidth(); col++) {
			for (int row = 0; row < chunk.getHeight(); row++) {
				renderTile(tileSize,
						worldPos + col * tileSize
								+ chunkNumber * chunk.getTiles().length * tileSize,
						chunk.getTiles()[col][row], col, row, chunkNumber, renderer);
			}
		}
	}

	private static void renderTile(int tileSize, float tilePos, Tile tile, int col, int row,
			int chunkNumber, ShapeRenderer renderer) {
		switch (tile) {
		case OBSTACLE:
			renderer.setColor(new Color(0, 0, 0, 1));
			renderer.rect(tilePos, row * tileSize, tileSize, tileSize);
			return;
		case EMPTY:
		default:
			return;
		}
	}
}
