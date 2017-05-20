package com.grupptva.runnergame.game.model.worldgenerator;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.worldgenerator.WorldGenerator;

public class WorldGeneratorTest {
	private WorldGenerator generator;
	
	@Before
	public void initGenerator()
	{
		float pixelsPerFrame = 3f;
		int tileSize = 20;
		int chunkWidth = 40;
		int chunkHeight = 30;
		
		GameCharacter character = new GameCharacter(30, 150, pixelsPerFrame);
		generator = new WorldGenerator(pixelsPerFrame, tileSize, 4l, chunkWidth, chunkHeight, 0, character);
	}
	
	@Test
	public void testFinalTileOnChunks() {
		boolean noFail = true;
		for (int i = 0; i < 50; i++) {
			if (!generateChunk_ShouldHaveTileAtRightmostColumn()) {
				noFail = false;
				break;
			}
		}
		assertTrue(noFail);
	}
	@Test
	public void testFirstTileOnChunks() {
		boolean noFail = true;
		for (int i = 0; i < 50; i++) {
			if (!generateChunk_ShouldHaveTileAtLeftmostColumn()) {
				noFail = false;
				break;
			}
		}
		assertTrue(noFail);
	}

	private boolean generateChunk_ShouldHaveTileAtLeftmostColumn() {
		Tile[][] chunk = generator.generateChunk().getTiles();
	
		int x = 0;

		for (int y = 0; y < chunk[0].length; y++) {
			if (chunk[x][y] == Tile.OBSTACLE) {
				return true;
			}
		}
		return false;
	}

	private boolean generateChunk_ShouldHaveTileAtRightmostColumn() {
		Tile[][] chunk = generator.generateChunk().getTiles();
	
		int x = chunk.length - 1;

		for (int y = 0; y < chunk[0].length; y++) {
			if (chunk[x][y] == Tile.OBSTACLE) {
				return true;
			}
		}
		return false;
	}
}
