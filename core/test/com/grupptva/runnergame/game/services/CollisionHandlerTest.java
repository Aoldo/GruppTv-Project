package com.grupptva.runnergame.game.services;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
}
