package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.List;

import com.grupptva.runnergame.ScenePlugin;
import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.services.worldgenerator.GeneratorChunk.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * 
 * @author Mattias
 *
 */
public class GeneratorVisualizer implements ScenePlugin {
	WorldGenerator generator;
	ShapeRenderer sr;

	int logIndex = 0;
	List<Tile[][]> chunkLog;

	public GeneratorVisualizer() {
		generator = new WorldGenerator(3f, 20, 4l, 40, 20, 0, new GameCharacter(30f, 150f, 3f));

		chunkLog = generator.generateChunkLog(10);
	}

	public void update() {
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			logIndex++;
		}
		if (logIndex >= chunkLog.size()) {
			logIndex = 0;
		}
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		renderChunk(sr, chunkLog.get(logIndex));
		sr.end();
	}

	public void renderChunk(ShapeRenderer sr, Tile[][] chunk) {

		for (int y = 0; y < chunk.length; y++) {
			for (int x = 0; x < chunk[0].length; x++) {
				//Select appropriate color depending on what kind of tile it is.
				if (chunk[y][x] != Tile.EMPTY) {
					switch (chunk[y][x]) {
					case FULL:
						sr.setColor(0, 0, 0, 1);
						break;
					case POSSIBLESTAND:
						sr.setColor(0.2f, .2f, 1f, 1);
						break;
					case POSSIBLEHOOK:
						sr.setColor(.6f, .2f, .9f, 1);
						break;
					default:
						sr.setColor(1, 0, 1, 1);
						break;
					}
					sr.rect(x * 10, y * 10, 10, 10);
				}
			}
		}
	}
}
