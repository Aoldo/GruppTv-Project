package com.grupptva.runnergame.game.services;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agnesmardh on 2017-05-16.
 */
public class CollisionHandlerTest {
	private CollisionHandler collisionHandler;
	private GameCharacter gameCharacter;
	private WorldModel world;
	private int tileSize = 20;


	@Before
	public void setUp() {
		gameCharacter = new GameCharacter(30, 150, 3f);
		world = new WorldModel();
		collisionHandler = new CollisionHandler(gameCharacter, world, tileSize);
	}

	@Test
	public void testCalculateColumnCharacterIsIn() {
		world.setPosition(-11);
		Assert.assertEquals(2, collisionHandler.calculateWhichColumnCharacterIsIn());
		world.setPosition(-71);
		Assert.assertEquals(5, collisionHandler.calculateWhichColumnCharacterIsIn());
		world.setPosition(-10);
		Assert.assertEquals(2, collisionHandler.calculateWhichColumnCharacterIsIn());
	}

	@Test
	public void testHandleCollisionWithTile() {
		Tile tile = Tile.OBSTACLE;
		float tileXPos = 45;
		float tileYPos = 30;
		gameCharacter.getPosition().setY(30);
		collisionHandler.handleCollisionWithTile(tile, tileXPos, tileYPos);
		Assert.assertTrue(gameCharacter.isDead());
		gameCharacter.setDead(false);
		tileXPos = 45;
		tileYPos = 25;
		gameCharacter.getPosition().setY(30);
		collisionHandler.handleCollisionWithTile(tile, tileXPos, tileYPos);
		Assert.assertTrue(gameCharacter.isDead());

	}

	@Test
	public void testGetColumnstoCheck() {
		Chunk[] chunks = new Chunk[3];
		chunks[0] = new Chunk(10,10);
		chunks[1] = new Chunk(10,10);
		chunks[2] = new Chunk(10,10);

		Tile[][] tmp = new Tile[10][10];
		for(int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				if(j % 2 == 0){
					tmp[i][j] = Tile.OBSTACLE;
				}
			}
		}

		chunks[2].setTiles(tmp);
		world = new WorldModel();
		world.setChunks(chunks);
		collisionHandler.setWorld(world);

		int characterColumnNumber = 1;
		List<Tile[]> columnsToCheck = collisionHandler.getColumnsToCheck(characterColumnNumber);

		Assert.assertEquals(Tile.EMPTY, columnsToCheck.get(0)[0]);
		Assert.assertEquals(Tile.OBSTACLE, columnsToCheck.get(1)[0]);
	}

	@Test
	public void testHandleCollisionWithColumns() {
	}

	@Test
	public void testHandlePossibleCollision() {

	}

	@Test
	public void testHandleTopCollision() {
		collisionHandler.handleTopCollision();
		Assert.assertTrue(gameCharacter.isDead());
	}

	@Test
	public void testHandleLeftCollision() {
		float tileYPos = 20;
		collisionHandler.handleLeftCollision(tileYPos);
		Assert.assertTrue(!gameCharacter.isDead());
		Assert.assertTrue(gameCharacter.isCollidingWithGround());
		Assert.assertEquals(40, (int)gameCharacter.getPosition().getY(), 1);
	}

	@Test
	public void testHandleRightCollision() {
		collisionHandler.handleTopCollision();
		Assert.assertTrue(gameCharacter.isDead());
	}

	@Test
	public void testHandleBottomCollision() {
		float tileYPos = 20;
		collisionHandler.handleLeftCollision(tileYPos);
		Assert.assertTrue(!gameCharacter.isDead());
		Assert.assertTrue(gameCharacter.isCollidingWithGround());
		Assert.assertEquals(40, (int)gameCharacter.getPosition().getY(), 1);
	}
}
