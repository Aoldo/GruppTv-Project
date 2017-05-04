package com.grupptva.runnergame.game.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import com.grupptva.runnergame.game.services.WorldGenerator;
import com.grupptva.runnergame.game.services.WorldGenerator.Tile;

/**
 * 
 * @author Mattias
 *
 */
public class WorldGeneratorTest {
	WorldGenerator wg;

	@Before
	public void init() {
		wg = new WorldGenerator(new ArrayList<Integer[]>(), new ArrayList<Integer[]>(),
				new ArrayList<Integer[]>(), 2l, 40, 20, 0);
	}

	@Test
	public void deepCopyChunk_ShouldNotBeReference() {
		Tile[][] chunk = new Tile[][] { { Tile.EMPTY, Tile.EMPTY },
				{ Tile.EMPTY, Tile.EMPTY } };

		Tile[][] chunkCopy = wg.deepCopyChunk(chunk);
		chunk[0][0] = Tile.FULL;

		assertFalse(chunk[0][0] == chunkCopy[0][0]);
	}
	

	@Test
	public void testFinalTileOnMultipleSeeds() {
		boolean noFail = true;
		for (int i = 0; i < 50; i++) {
			if (!generateChunk_ShouldHaveTileAtRightmostColumn()) {
				noFail = false;
				break;
			}
		}
		assertTrue(noFail);
	}

	public boolean generateChunk_ShouldHaveTileAtRightmostColumn() {
		//TODO: Use generate chunk instead of chunklog, once implemented.
		List<Tile[][]> chunkLog = wg.generateChunkLog(10);
		Tile[][] chunk = chunkLog.get(chunkLog.size() - 1);

		int x = chunk[0].length - 1;
		for (int y = 0; y < chunk.length; y++) {
			if (chunk[y][x] == Tile.FULL) {
				return true;
			}
		}
		return false;
	}
}
