package com.grupptva.runnergame.game.model;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.gamecharacter.Point;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import com.grupptva.runnergame.game.services.collision.CollisionChecker;
import com.grupptva.runnergame.game.services.collision.ICollisionChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agnesmardh on 2017-05-19.
 */
public class CollisionLogicTest {

	private CollisionLogic collisionLogic;
	private GameCharacter gameCharacter;
	private WorldModel world;
	private int tileSize;
	private ICollisionChecker collisionChecker;

	@Before
	public void setUp() {
		gameCharacter = new GameCharacter(30, 150, 3);
		world = new WorldModel();
		tileSize = 20;
		collisionChecker = new CollisionChecker();
		collisionLogic = new CollisionLogic(gameCharacter, world, tileSize, collisionChecker);
	}

	@Test
	public void testCalculateColumnCharacterIsIn() {
		world.setPosition(-11);
		Assert.assertEquals(2, collisionLogic.calculateWhichColumnCharacterIsIn());
		world.setPosition(-71);
		Assert.assertEquals(5, collisionLogic.calculateWhichColumnCharacterIsIn());
		world.setPosition(-10);
		Assert.assertEquals(2, collisionLogic.calculateWhichColumnCharacterIsIn());
	}

	@Test
	public void testGetColumnsToCheck() {
		Chunk[] chunks = new Chunk[3];
		chunks[0] = new Chunk(10,10);
		chunks[1] = new Chunk(10,10);
		chunks[2] = new Chunk(10,10);

		Tile[][] tmp = new Tile[10][10];
		for(int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				if(i % 2 == 0){
					tmp[i][j] = Tile.OBSTACLE;
				}else{
					tmp[i][j] = Tile.EMPTY;
				}
			}
		}

		chunks[0].setTiles(tmp);
		world = new WorldModel();
		world.setChunks(chunks);
		collisionLogic.setWorld(world);

		int characterColumnNumber = 1;
		List<Tile[]> columnsToCheck = collisionLogic.getColumnsToCheck
				(characterColumnNumber);

		Assert.assertEquals(Tile.EMPTY, columnsToCheck.get(0)[0]);
		Assert.assertEquals(Tile.OBSTACLE, columnsToCheck.get(1)[0]);
	}

	@Test
	public void testHandleCollisionWithColumns() {
		List<Tile[]> columns = new ArrayList<Tile[]>();
		columns.add(new Tile[] {
				Tile.EMPTY,
				Tile.EMPTY,
				Tile.EMPTY,
				Tile.EMPTY,
				Tile.EMPTY
		});
		columns.add(new Tile[] {
				Tile.EMPTY,
				Tile.OBSTACLE,
				Tile.EMPTY,
				Tile.EMPTY,
				Tile.OBSTACLE
		});
		float firstColumnXValue = 0;
		gameCharacter.setDead(false);
		gameCharacter.setPosition(new Point(tileSize / 2f, 3.5f * tileSize));
		collisionLogic.handleCollisionWithColumns(columns, firstColumnXValue);
		Assert.assertEquals(true, gameCharacter.isDead());
		gameCharacter.setDead(false);

		gameCharacter.setPosition(new Point(tileSize / 2f, 1.75f * tileSize));
		collisionLogic.handleCollisionWithColumns(columns, firstColumnXValue);
		Assert.assertEquals(false, gameCharacter.isDead());
	}

	@Test
	public void testHandlePossibleCollision() {
		Chunk[] chunks = new Chunk[3];
		chunks[0] = new Chunk(10,10);
		chunks[1] = new Chunk(10,10);
		chunks[2] = new Chunk(10,10);

		Tile[][] tmp = new Tile[10][10];
		for(int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				if((i + j) % 3 == 1){
					tmp[i][j] = Tile.OBSTACLE;
				}else{
					tmp[i][j] = Tile.EMPTY;
				}
			}
		}
		chunks[0].setTiles(tmp);
		world.setChunks(chunks);

		// Creates diagonals with obstacles like this:
		// (Shown as they would be portrayed in-game)
		//
		// X = Obstacle
		//
		//      0   1   2   3   4   5   6   7   8   9
		//    |---|---|---|---|---|---|---|---|---|---|
		//  9 |   | X |   |   | X |   |   | X |   |   |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  8 |   |   | X |   |   | X |   |   | X |   |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  7 | X |   |   | X |   |   | X |   |   | X |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  6 |   | X |   |   | X |   |   | X |   |   |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  5 |   |   | X |   |   | X |   |   | X |   |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  4 | X |   |   | X |   |   | X |   |   | X |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  3 |   | X |   |   | X |   |   | X |   |   |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  2 |   |   | X |   |   | X |   |   | X |   |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  1 | X |   |   | X |   |   | X |   |   | X |
		//    |---|---|---|---|---|---|---|---|---|---|
		//  0 |   | X |   |   | X |   |   | X |   |   |
		//    |---|---|---|---|---|---|---|---|---|---|

		gameCharacter.setPosition(new Point(1.5f * tileSize, 1.5f * tileSize));
		collisionLogic.handlePossibleCollision();
		Assert.assertEquals(true, gameCharacter.isDead());

	}

	@Test
	public void testHandleTopCollision() {
		collisionLogic.handleTopCollision();
		Assert.assertTrue(gameCharacter.isDead());
	}

	@Test
	public void testHandleLeftCollision() {
		float tileYPos = 20;
		collisionLogic.handleLeftCollision(tileYPos);
		Assert.assertTrue(!gameCharacter.isDead());
		Assert.assertTrue(gameCharacter.isCollidingWithGround());
		Assert.assertEquals(40, (int)gameCharacter.getPosition().getY(), 1);
	}

	@Test
	public void testHandleRightCollision() {
		collisionLogic.handleTopCollision();
		Assert.assertTrue(gameCharacter.isDead());
	}

	@Test
	public void testHandleBottomCollision() {
		float tileYPos = 20;
		collisionLogic.handleBottomCollision(tileYPos);
		Assert.assertTrue(!gameCharacter.isDead());
		Assert.assertTrue(gameCharacter.isCollidingWithGround());
		Assert.assertEquals(40, (int)gameCharacter.getPosition().getY(), 1);
	}
}
