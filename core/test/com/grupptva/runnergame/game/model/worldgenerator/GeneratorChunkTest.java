package com.grupptva.runnergame.game.model.worldgenerator;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.grupptva.runnergame.game.model.worldgenerator.GeneratorChunk;
import com.grupptva.runnergame.game.model.worldgenerator.GeneratorChunk.Tile;



public class GeneratorChunkTest {
	@Test
	public void deepCopyChunk_ShouldNotBeReference() {
		GeneratorChunk chunk = new GeneratorChunk(2,2);

		Tile[][] tilesCopy = chunk.deepCopyTiles();
		
		chunk.tiles[0][0] = Tile.FULL;

		assertFalse(chunk.tiles[0][0] == tilesCopy[0][0]);
	}
}
