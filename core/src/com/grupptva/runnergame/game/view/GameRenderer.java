package com.grupptva.runnergame.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.game.model.GameOverMenu;
import com.grupptva.runnergame.game.model.ScoreRenderer;
import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;

/**
 * Created by Karl 'NaN' Wikström on 2017-05-03.
 */
public class GameRenderer {
	
	GameOverMenu gameOverMenu = new GameOverMenu();
	ScoreRenderer scoreRenderer = new ScoreRenderer();

	public void renderCharacter(int tileSize, GameCharacter gameCharacter, ShapeRenderer
			sr) {
		sr.setColor(Color.FOREST);
		sr.rect(gameCharacter.getPosition().getX(), gameCharacter.getPosition().getY(), tileSize,
				tileSize);
		if (gameCharacter.isAttachedWithHook()) {
			sr.line(gameCharacter.getPosition().getX() + tileSize/2, gameCharacter.getPosition().getY() + tileSize/2,
					gameCharacter.getHookPosition().getX(), gameCharacter.getHookPosition().getY());
		}
	}

	public void renderWorld(int tileSize, WorldModel world, ShapeRenderer renderer) {
		Chunk[] chunks = world.getChunksInRightOrder();
		for (int i = 0; i < chunks.length; i++) {
			renderChunk(tileSize, world.getPosition(), chunks[i], i, renderer);
		}
	}

	private void renderChunk(int tileSize, float worldPos, Chunk chunk, int chunkNumber,
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

	private void renderTile(int tileSize, float tilePos, Tile tile, int col, int row, int
			chunkNumber,
	                        ShapeRenderer renderer) {
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
	
	public void renderScore(SpriteBatch batch, ShapeRenderer sr) {
		scoreRenderer.render(batch, sr);
	}

	public void renderGameOverMenu(SpriteBatch batch, ShapeRenderer sr) {
		// kalla på gameOverMenu render metod här 
		//enterGameOverMenu();
		gameOverMenu.render(batch, sr);
		gameOverMenu.update();
	}
}
