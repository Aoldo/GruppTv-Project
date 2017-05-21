package com.grupptva.runnergame.game.model;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.gamecharacter.Point;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by agnesmardh on 2017-05-20.
 */
public class HookLogicTest {
	HookLogic hookLogic;
	int tileSize = 20;

	@Before
	public void setUp() throws Exception {
		GameCharacter gameCharacter = new GameCharacter(0, 0, 3f);
		WorldModel world;
		Chunk[] chunks = new Chunk[3];
		chunks[0] = new Chunk(10,10);
		chunks[1] = new Chunk(10,10);
		chunks[2] = new Chunk(10,10);

		Tile[][] tmp = new Tile[10][10];
		for(int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				if(i == 1 && j == 2){
					tmp[i][j] = Tile.OBSTACLE;
				}else{
					tmp[i][j] = Tile.EMPTY;
				}
			}
		}

		chunks[0].setTiles(tmp);
		world = new WorldModel();
		world.setChunks(chunks);

		final int tileSize = 20;
		final int chunkWidth = 10;
		final int chunkHeight = 10;
		hookLogic = new HookLogic(gameCharacter, world, tileSize, chunkWidth,
				chunkHeight);
	}

	@Test
	public void testCheckIfLineIntersectsTile() {
		Point hookEndPos = new Point(2.5f * tileSize, 4.5f * tileSize);
		Assert.assertEquals(true, hookLogic.checkIfLineIntersectsTile(hookEndPos,
				tileSize, 2 * tileSize));
		hookEndPos = new Point(-2.5f * tileSize, 4.5f * tileSize);
		Assert.assertEquals(false, hookLogic.checkIfLineIntersectsTile(hookEndPos,
				tileSize, 2 * tileSize));
	}
}
