package com.grupptva.runnergame.game.model.world;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.grupptva.runnergame.game.model.world.Tile.EMPTY;
import static com.grupptva.runnergame.game.model.world.Tile.OBSTACLE;
import static java.lang.System.out;

/**
 * Created on 5/15/17.
 *
 * @author Karl 'NaN' Wikström
 */
public class WorldModelTest {
	
	private WorldModel world;
	private Chunk[] chunks = new Chunk[3];
	
	@Before
	public void setUp() throws Exception {
		chunks[0] = new Chunk(10,10);
		chunks[1] = new Chunk(10,10);
		chunks[2] = new Chunk(10,10);
		
		Tile[][] tmp = new Tile[10][10];
		for(int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				if(i == 2){
					tmp[i][j] = Tile.OBSTACLE;
				}
			}
		}
		
		chunks[2].setTiles(tmp);
		world = new WorldModel();
		world.setChunks(chunks);
	}
	
	@Test
	public void testGetColumn() throws Exception {
		out.println(Arrays.toString(world.getColumn(23)));
	}
}
