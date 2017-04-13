package com.grupptva.runnergame.worldgenerator;

import java.util.ArrayList;
import java.util.List;

import com.grupptva.runnergame.worldgenerator.WorldGenerator.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GeneratorVisualizer {
	WorldGenerator generator;
	ShapeRenderer sr;

	int logIndex = 0;
	List<Tile[][]> chunkLog;

	public GeneratorVisualizer() {
		List<Integer[]> hookAttachOffsets = new ArrayList<Integer[]>();
		List<Integer[]> hookJumpOffsets = new ArrayList<Integer[]>();
		List<Integer[]> jumpOffsets = new ArrayList<Integer[]>();

		generator = new WorldGenerator(hookAttachOffsets, jumpOffsets, hookJumpOffsets, 2l);

		chunkLog = generator.generateChunkLog(5);
	}

	public void update() {
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			logIndex++;
		}
		if (logIndex >= chunkLog.size()) {
			logIndex = 0;
		}
	}

	public void render(SpriteBatch batch) {
		renderChunk(batch, chunkLog.get(logIndex));
	}

	public void renderChunk(SpriteBatch batch, Tile[][] chunk) {
		sr = new ShapeRenderer();
		sr.begin(ShapeType.Filled);

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
						sr.setColor(1,0,1,1);
						break;
					}
					sr.rect(x * 10, y * 10, 10, 10);
				}
			}
		}
		sr.end();
	}
}
